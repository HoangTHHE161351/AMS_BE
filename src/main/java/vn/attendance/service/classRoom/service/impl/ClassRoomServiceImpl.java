package vn.attendance.service.classRoom.service.impl;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.ClassRoom;
import vn.attendance.model.Users;
import vn.attendance.repository.ClassRoomRepository;
import vn.attendance.service.classRoom.request.AddClassRoomRequest;
import vn.attendance.service.classRoom.request.EditClassRoomRequest;
import vn.attendance.service.classRoom.response.ClassRoomDto;
import vn.attendance.service.classRoom.response.IClassDto;
import vn.attendance.service.classRoom.service.ClassRoomService;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
public class ClassRoomServiceImpl implements ClassRoomService {

    @Autowired
    ClassRoomRepository classRoomRepository;


    @Override
    public Page<ClassRoomDto> findAllClassRoom(String search, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return classRoomRepository.findAllClasses(search, search, pageable);
    }

    @Override
    public List<IClassDto> findAllClassRoom(Integer subjectId) {
        return classRoomRepository.findAllClasses(subjectId);
    }

    @Override
    public List<ClassRoom> findAll() {
        return classRoomRepository.findAllClass();
    }

    @Override
    public ClassRoom findById(Integer id) throws AmsException {
        ClassRoom classRoom = classRoomRepository.findById(id).orElse(null);
        if (classRoom == null || classRoom.getStatus().equals(Constants.STATUS_TYPE.DELETED))
            throw new AmsException(MessageCode.NOT_FOUND);
        return classRoom;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AddClassRoomRequest addClassRoom(AddClassRoomRequest request, int option) throws AmsException {
        // Get user info
        Users users = BaseUserDetailsService.USER.get();
        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        List<ClassRoom> classRooms = classRoomRepository.findAll();
        for (ClassRoom cr : classRooms) {
            if (cr.getClassName().equals(request.getClassName())) {
                request.setStatus(Constants.REQUEST_STATUS.FAILED);
                request.setErrorMess(MessageCode.CLASS_SUBJECT_EXISTED.getCode());
                if (option == 1) {
                    throw new AmsException(MessageCode.CLASSNAME_ALREADY_EXISTED);
                }
                return request;
            }
        }

        try {
            ClassRoom classRoom = new ClassRoom();
            classRoom.setId(null);
            classRoom.setClassName(request.getClassName());
            classRoom.setDescription(request.getDescription());
            classRoom.setStatus(Constants.STATUS_TYPE.ACTIVE);
            classRoom.setCreatedBy(users.getId());
            classRoom.setCreatedAt(LocalDateTime.now());
            // Save camera
            classRoomRepository.save(classRoom);
            request.setStatus(Constants.REQUEST_STATUS.SUCCESS);
        } catch (Exception e) {
            throw new AmsException(MessageCode.ADD_CLASSROOM_FAIL);
        }

        return request;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editClassRoom(Integer id, EditClassRoomRequest request) throws AmsException {
        Users user = BaseUserDetailsService.USER.get();
        if (user == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        ClassRoom classRoom = classRoomRepository.getById(id);
        if (classRoom == null) {
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            throw new AmsException(MessageCode.CLASSROOM_NOT_FOUND);
        }

        classRoom.setClassName(request.getClassName());
        classRoom.setDescription(request.getDescription());
        classRoom.setModifiedBy(user.getId());
        classRoom.setModifiedAt(LocalDateTime.now());
        request.setStatus(Constants.REQUEST_STATUS.SUCCESS);
        classRoomRepository.save(classRoom);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteClassRoom(Integer id) throws AmsException {
        Users user = BaseUserDetailsService.USER.get();
        if (user == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        ClassRoom classRoom = classRoomRepository.getById(id);
        classRoom.setStatus(Constants.STATUS_TYPE.DELETED);
        classRoom.setModifiedAt(LocalDateTime.now());
        classRoom.setModifiedBy(user.getId());
        classRoomRepository.save(classRoom);
    }

    public byte[] exportClassRoom(String search) throws AmsException {
        HSSFWorkbook workbook = new HSSFWorkbook();

        try {
            List<ClassRoomDto> classrooms = classRoomRepository.findAllToExport(search);
            if (classrooms.isEmpty()) {
                throw new AmsException(MessageCode.CONTENT_EMPTY);
            }
            Sheet sheet = workbook.createSheet("Classroom List");

            // Create header row
            Row headerRow = sheet.createRow(0);

            // Define headers
            String[] headers = {"No", "Class Name", "Description", "Status", "Created By", "Created At", "Modified By", "Modified At"};
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            int rowNum = 1;
            for (ClassRoomDto classroom : classrooms) {
                Row dataRow = sheet.createRow(rowNum++);

                dataRow.createCell(0).setCellValue(rowNum - 1);
                dataRow.createCell(1).setCellValue(Objects.requireNonNullElse(classroom.getClassName(), "N/A"));
                dataRow.createCell(2).setCellValue(Objects.requireNonNullElse(classroom.getDescription(), "N/A"));
                dataRow.createCell(3).setCellValue(Objects.requireNonNullElse(classroom.getStatus(), "N/A"));
                dataRow.createCell(4).setCellValue(Objects.requireNonNullElse(classroom.getCreatedBy(), "N/A"));
                dataRow.createCell(5).setCellValue(classroom.getCreatedAt() != null ? classroom.getCreatedAt().format(formatter) : "N/A");
                dataRow.createCell(6).setCellValue(Objects.requireNonNullElse(classroom.getModifiedBy(), "N/A"));
                dataRow.createCell(7).setCellValue(classroom.getModifiedAt() != null ? classroom.getModifiedAt().format(formatter) : "N/A");
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            workbook.close();

            return out.toByteArray();
        } catch (IOException e) {
            throw new AmsException(MessageCode.EXPORT_FAIL);
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                throw new AmsException(MessageCode.EXPORT_FAIL);
            }
        }
    }

    @Override
    public List<AddClassRoomRequest> importClassRoom(List<AddClassRoomRequest> classRoomRequests) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();
        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        for (AddClassRoomRequest request : classRoomRequests) {
            addClassRoom(request, 2);
        }
        return classRoomRequests;
    }

    @Override
    public List<IClassDto> searchClassRoomForSchedule(Integer subjectId, LocalDate date, Integer slotId) {
        return classRoomRepository.searchClassRoomForSchedule(subjectId,date,slotId);
    }


}
