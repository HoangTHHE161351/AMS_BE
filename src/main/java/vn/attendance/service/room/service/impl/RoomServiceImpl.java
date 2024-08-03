package vn.attendance.service.room.service.impl;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Room;
import vn.attendance.model.Users;
import vn.attendance.repository.CameraRepository;
import vn.attendance.repository.RoomRepository;
import vn.attendance.repository.ScheduleRepository;
import vn.attendance.service.room.request.AddRoomRequest;
import vn.attendance.service.room.request.EditRoomRequest;
import vn.attendance.service.room.response.RoomDto;
import vn.attendance.service.room.service.RoomService;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    RoomRepository roomRepository;
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    CameraRepository cameraRepository;

    @Override
    public Room findById(Integer id) throws AmsException {
        Room room = roomRepository.findById(id).orElse(null);
        if (room == null || room.getStatus().equals(Constants.STATUS_TYPE.DELETED))
            throw new AmsException(MessageCode.ROOM_NOT_FOUND);
        return room;
    }

    @Override
    public Page<RoomDto> findAll(String search, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return roomRepository.findAllRoom(search, status, pageable);
    }

    @Override
    public List<Room> finAllRoom() {
        return roomRepository.findAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AddRoomRequest addRoom(AddRoomRequest request, Integer option) throws AmsException {

        Users users = BaseUserDetailsService.USER.get();
        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }
        Room room = roomRepository.findByName(request.getRoomName());

        if (room!=null) {
            if(option == 1) throw new AmsException(MessageCode.ROOM_ALREADY_EXISTS);
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            request.setErrorMess(MessageCode.ROOM_ALREADY_EXISTS.getCode());
            return request;
        }
        try {
            Room newRoom = new Room();
            newRoom.setRoomName(request.getRoomName());
            newRoom.setDescription(request.getDescription());
            newRoom.setCreatedAt(LocalDateTime.now());
            newRoom.setCreatedBy(users.getId());
            newRoom.setStatus(Constants.STATUS_TYPE.ACTIVE);
            roomRepository.save(newRoom);
            request.setStatus(Constants.REQUEST_STATUS.SUCCESS);
        } catch (Exception e) {
            if(option == 1) throw new AmsException(MessageCode.ADD_ROOM_FAIL);
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            request.setErrorMess(MessageCode.ADD_ROOM_FAIL.getCode());
            return request;
        }

        return request;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EditRoomRequest updateRoom(EditRoomRequest request) throws AmsException {
        Users user = BaseUserDetailsService.USER.get();
        if(user == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        Room room = roomRepository.findById(request.getId()).orElse(null);
        if(room == null || room.getStatus().equals(Constants.STATUS_TYPE.DELETED)){
            throw new AmsException(MessageCode.ROOM_NOT_FOUND);
        }

        Room checkRoom = roomRepository.findByName(request.getRoomName());

        if (checkRoom != null && !checkRoom.getId().equals(request.getId())) {
            throw new AmsException(MessageCode.ROOM_NAME_ALREADY_EXISTS);
        }

        room.setRoomName(request.getRoomName());
        room.setDescription(request.getDescription());
        room.setModifiedAt(LocalDateTime.now());
        room.setModifiedBy(user.getId());
        room.setStatus(request.getStatus());
        roomRepository.save(room);

        request.setStatus(Constants.REQUEST_STATUS.SUCCESS);
        return request;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Room deleteRoom(Integer id) throws AmsException {
        Users users =BaseUserDetailsService.USER.get();
        if (users==null)throw new AmsException(MessageCode.USER_NOT_FOUND);
        Room room = roomRepository.findById(id).orElse(null);
        if (room == null || room.getStatus().equals(Constants.STATUS_TYPE.DELETED))
            throw new AmsException(MessageCode.ROOM_NOT_FOUND);
        //check schedule
        if(roomRepository.countScheduleByRoom(id, LocalDate.now())>0) throw new AmsException(MessageCode.SCHEDULES_EXIST_FOR_ROOM);
        //check camera
        if(cameraRepository.countCameraByRoom(id)>0) throw new AmsException(MessageCode.CAMERAS_EXIST_FOR_ROOM);

        room.setStatus(Constants.STATUS_TYPE.DELETED);
        room.setModifiedAt(LocalDateTime.now());
        room.setModifiedBy(users.getId());

        return roomRepository.save(room);
    }

    @Override
    public List<AddRoomRequest> importRooms(List<AddRoomRequest> roomsList) throws AmsException {
        for (AddRoomRequest request : roomsList) {
            addRoom(request, 0);
        }
        return roomsList;
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public byte[] exportRoom(String search) throws AmsException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        try {
            List<RoomDto> rooms = roomRepository.findAll(search);
            if (rooms.size() == 0) {
                throw new AmsException(MessageCode.CONTENT_EMPTY);
            }

            Sheet sheet = workbook.createSheet("Room List");
            // Tạo tiêu đề cho các cột
            Row headerRow = sheet.createRow(0);

            String[] headers = {"No", "Room Name", "Description", "Status", "Created By", "Created At", "Modified By", "Modified At"};
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            int rowNum = 1;
            for (RoomDto room : rooms) {
                Row dataRow = sheet.createRow(rowNum++);
                dataRow.createCell(0).setCellValue(rowNum - 1);
                dataRow.createCell(1).setCellValue(room.getRoomName());
                dataRow.createCell(2).setCellValue(Objects.requireNonNullElse(room.getDescription(), "N/A"));
                dataRow.createCell(3).setCellValue(room.getStatus());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                dataRow.createCell(4).setCellValue(room.getCreatedBy() != null ? room.getCreatedBy() : "N/A");
                dataRow.createCell(5).setCellValue(room.getCreatedAt() != null ? room.getCreatedAt().format(formatter) : "N/A");
                dataRow.createCell(6).setCellValue(room.getModifiedBy() != null ? room.getModifiedBy() : "N/A");
                dataRow.createCell(7).setCellValue(room.getModifiedAt() != null ? room.getModifiedAt().format(formatter) : "N/A");
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            workbook.close();

            byte[] fileContent = out.toByteArray();

            return fileContent;
        } catch (Exception e) {
            throw new AmsException(MessageCode.EXPORT_FAIL);
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                throw new AmsException(MessageCode.EXPORT_FAIL);
            }
        }
    }


}
