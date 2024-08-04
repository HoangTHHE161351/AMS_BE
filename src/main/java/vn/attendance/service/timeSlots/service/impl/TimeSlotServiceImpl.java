package vn.attendance.service.timeSlots.service.impl;

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
import vn.attendance.model.TimeSlot;
import vn.attendance.model.Users;
import vn.attendance.repository.ScheduleRepository;
import vn.attendance.repository.TimeSlotRepository;
import vn.attendance.service.timeSlots.request.AddTimeSlotRequest;
import vn.attendance.service.timeSlots.request.EditTimeSlotRequest;
import vn.attendance.service.timeSlots.response.TimeSlotDto;
import vn.attendance.service.timeSlots.service.TimeSlotService;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;

@Service
public class TimeSlotServiceImpl implements TimeSlotService {
    @Autowired
    TimeSlotRepository timeSlotRepository;
    @Autowired
    ScheduleRepository scheduleRepository;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public Page<TimeSlotDto> findAllTimeSlots(String search, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
//        Page<TimeSlotDto> timeSlotDtos = timeSlotRepository.findAllDto(search, status, pageable);
        return timeSlotRepository.findAllDto(search, status, pageable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AddTimeSlotRequest addTimeSLot(AddTimeSlotRequest request, Integer option) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();
        if (users == null)
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        try {

            LocalTime startTime;
            LocalTime endTime;
            try {
                startTime = LocalTime.parse(request.getStartTime(),formatter);
                endTime =  LocalTime.parse(request.getEndTime(),formatter);

                System.out.println("Start Time: " + startTime);
                System.out.println("End Time: " + endTime);
            } catch (DateTimeParseException e) {
                if(option == 1) throw new AmsException("Error parsing time");
                request.setStatusAdd(Constants.REQUEST_STATUS.FAILED);
                request.setErrorMess("Error parsing time");
                return request;
            }

//            if(!startTime.isBefore(endTime)){
//                if(option == 1) throw new AmsException(MessageCode.INVALID_TIME_RANGE);
//                request.setStatusAdd(Constants.REQUEST_STATUS.FAILED);
//                request.setErrorMess(MessageCode.INVALID_TIME_RANGE.getCode());
//                return request;
//            }

            if (timeSlotRepository.findByTime(startTime, endTime).isPresent()) {
                if(option == 1) throw new AmsException(MessageCode.TIMESLOT_TIME_ALREADY_EXISTS);
                request.setStatusAdd(Constants.REQUEST_STATUS.FAILED);
                request.setErrorMess(MessageCode.TIMESLOT_TIME_ALREADY_EXISTS.getCode());
                return request;
            }

            if (timeSlotRepository.findByName(request.getSlotName()).isPresent()) {
                if(option == 1) throw new AmsException(MessageCode.TIMESLOT_NAME_ALREADY_EXISTS);
                request.setStatusAdd(Constants.REQUEST_STATUS.FAILED);
                request.setErrorMess(MessageCode.TIMESLOT_NAME_ALREADY_EXISTS.getCode());
                return request;
            }

            TimeSlot timeSlotNew = new TimeSlot();
            timeSlotNew.setSlotName(request.getSlotName());
            timeSlotNew.setStartTime(startTime);
            timeSlotNew.setEndTime(endTime);
            timeSlotNew.setDescription(request.getDescription());
            timeSlotNew.setStatus(Constants.STATUS_TYPE.ACTIVE);
            timeSlotNew.setCreatedAt(LocalDateTime.now());
            timeSlotNew.setCreatedBy(users.getId());
            request.setStatusAdd(Constants.REQUEST_STATUS.SUCCESS);
            timeSlotRepository.save(timeSlotNew);
        } catch (AmsException e) {
            request.setStatusAdd(Constants.REQUEST_STATUS.FAILED);
            request.setErrorMess(MessageCode.ADD_TIMESLOT_FAIL.getCode());
        }
        return request;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TimeSlot editTimeSlot(EditTimeSlotRequest request) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();
        if (users == null)
            throw new AmsException(MessageCode.USER_NOT_FOUND);

        TimeSlot timeSlot = timeSlotRepository.findById(request.getId()).orElse(null);

        if (timeSlot == null || timeSlot.getStatus().equals(Constants.STATUS_TYPE.DELETED))
            throw new AmsException(MessageCode.TIMESLOT_NOT_FOUND);

        LocalTime startTime;
        LocalTime endTime;
        try {
            startTime = LocalTime.parse(request.getStartTime(),formatter);
            endTime =  LocalTime.parse(request.getEndTime(),formatter);

        } catch (DateTimeParseException e) {
            throw new AmsException("Error parsing time");
        }

        if(timeSlotRepository.countScheduleByTimeSlot(request.getId())>0){
            throw new AmsException(MessageCode.TIMESLOT_ALREADY_ASSIGNED);
        }

        TimeSlot old = timeSlotRepository.findByTime(startTime, endTime).orElse(null);
        if (old!=null && !old.getId().equals(request.getId())) {
            throw new AmsException(MessageCode.TIMESLOT_TIME_ALREADY_EXISTS);
        }

        old = timeSlotRepository.findByTimeSlotName(request.getSlotName());
        if (old!=null && !old.getId().equals(request.getId())) {
            throw new AmsException(MessageCode.TIMESLOT_NAME_ALREADY_EXISTS);
        }


        timeSlot.setModifiedBy(users.getId());
        timeSlot.setSlotName(request.getSlotName());
        timeSlot.setModifiedAt(LocalDateTime.now());
        timeSlot.setStartTime(startTime);
        timeSlot.setEndTime(endTime);
        timeSlot.setDescription(request.getDescription());
        return timeSlotRepository.save(timeSlot);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TimeSlot deleteSlot(Integer id) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();
        if (users == null)
            throw new AmsException(MessageCode.USER_NOT_FOUND);

        TimeSlot timeSlot = timeSlotRepository.findById(id).orElse(null);
        if (timeSlot == null || timeSlot.getStatus().equals(Constants.STATUS_TYPE.DELETED)) {
            throw new AmsException(MessageCode.TIMESLOT_NOT_FOUND);
        }

        if(timeSlotRepository.countScheduleByTimeSlot(id)>0) throw new AmsException(MessageCode.SCHEDULES_EXIST_FOR_TIME_SLOT) ;

        timeSlot.setStatus(Constants.STATUS_TYPE.DELETED);
        timeSlot.setModifiedAt(LocalDateTime.now());
        timeSlot.setModifiedBy(users.getId());


        return timeSlotRepository.save(timeSlot);
    }

    @Override
    public List<TimeSlot> findAllTimeSlots() throws AmsException {
        return timeSlotRepository.findAllTimeSlots().stream().toList();
    }

    @Override
    public List<AddTimeSlotRequest> importTimeSlots(List<AddTimeSlotRequest> timeSlotList) throws AmsException {
        for (AddTimeSlotRequest request : timeSlotList) {
            addTimeSLot(request, 0);
        }
        return timeSlotList;
    }

    @Override
    public byte[] exportTimeSlot() throws AmsException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        try {
            List<TimeSlotDto> timeSlots = timeSlotRepository.findAllTimeSlotsDto();
            if (timeSlots.size() == 0) {
                throw new AmsException(MessageCode.CONTENT_EMPTY);
            } else {
                System.out.println(timeSlots.size());
            }

            Sheet sheet = workbook.createSheet("Time slot List");
            // Tạo tiêu đề cho các cột
            Row headerRow = sheet.createRow(0);

            String[] headers = {"No", "Start time", "End time", "Status", "Description", "Created By", "Created At", "Modified By", "Modified At"};
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            int rowNum = 1;
            for (TimeSlotDto timeSlot : timeSlots) {
                Row dataRow = sheet.createRow(rowNum++);
                dataRow.createCell(0).setCellValue(rowNum - 1);
                dataRow.createCell(1).setCellValue(timeSlot.getStartTime().format(timeFormatter));
                dataRow.createCell(2).setCellValue(timeSlot.getEndTime().format(timeFormatter));
                dataRow.createCell(3).setCellValue(timeSlot.getStatus());
                dataRow.createCell(4).setCellValue(Objects.requireNonNullElse(timeSlot.getDescription(), "N/A"));
                dataRow.createCell(5).setCellValue(timeSlot.getCreatedBy() != null ? timeSlot.getCreatedBy() : "N/A");
                dataRow.createCell(6).setCellValue(timeSlot.getCreatedAt() != null ? timeSlot.getCreatedAt().format(dateTimeFormatter) : "N/A");
                dataRow.createCell(7).setCellValue(timeSlot.getModifiedBy() != null ? timeSlot.getModifiedBy() : "N/A");
                dataRow.createCell(8).setCellValue(timeSlot.getModifiedAt() != null ? timeSlot.getModifiedAt().format(dateTimeFormatter) : "N/A");
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            workbook.close();

            byte[] fileContent = out.toByteArray();

            return fileContent;
        } catch (Exception e) {
            throw new AmsException(MessageCode.EXPORT_FAIL, e.getMessage());
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                throw new AmsException(MessageCode.EXPORT_FAIL, e.getMessage());
            }
        }
    }
}
