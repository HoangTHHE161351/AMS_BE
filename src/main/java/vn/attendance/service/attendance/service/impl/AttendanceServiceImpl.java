package vn.attendance.service.attendance.service.impl;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.*;
import vn.attendance.repository.*;
import vn.attendance.service.attendance.request.*;
import vn.attendance.service.attendance.response.AttendanceDTO;
import vn.attendance.service.attendance.response.IAttendanceDTO;
import vn.attendance.service.attendance.response.IListAttendenceResponse;
import vn.attendance.service.attendance.response.ListAttendenceResponse;
import vn.attendance.service.attendance.service.AttendanceService;
import vn.attendance.service.classRoom.response.IClassDto;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    TimeSlotRepository timeSlotRepository;

    @Override
    public List<AttendanceDto> attendanceReport(Integer semesterId, Integer classId) throws Exception {
        Users users = BaseUserDetailsService.USER.get();

        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        List<IAttendanceDto> attendanceDtos = new ArrayList<>();
        List<AttendanceDto> attendanceDto = new ArrayList<>();
        if (users.getRoleId() == 4) {
            attendanceDtos = attendanceRepository.attendanceReportStudent(semesterId, users.getId(), classId);
            attendanceDto = attendanceDtos.stream().map(AttendanceDto::new).collect(Collectors.toList());
        } else if (users.getRoleId() == 1 || users.getRoleId() == 2 || users.getRoleId() == 3) {
            attendanceDtos = attendanceRepository.attendanceReportTeacher(semesterId, users.getId(), classId);
            attendanceDto = attendanceDtos.stream().map(AttendanceDto::new).collect(Collectors.toList());
        }
        return attendanceDto;
    }

    @Override
    public List<AttendanceDTO> listStudentAttendance(Integer scheduleId) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();

        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }
        List<IAttendanceDTO> list = attendanceRepository.findAttendanceByClass(scheduleId);

        return list.stream()
                .map(AttendanceDTO::new) // Convert each IAttendanceDTO to AttendanceDTO
                .collect(Collectors.toList());
    }

    @Override
    public List<EditAttendanceRequest> checkAttendance(List<EditAttendanceRequest> requestList, Integer scheduleId) throws AmsException {

        Users users = BaseUserDetailsService.USER.get();

        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }
/*
        if (users.getRoleId() != 1 && users.getRoleId() != 2 && users.getRoleId() != 3) {
            throw new AmsException(MessageCode.USER_NOT_AUTHORITY);
        }*/

        LocalTime checkTime = LocalTime.now();

        if (users.getRoleId() == 3 && checkTime.isAfter(LocalTime.of(23, 59)))
            throw new AmsException(MessageCode.CHECK_ATTEND_INVALID_TIME);

        List<Attendance> attendanceList = attendanceRepository.findAttendanceByScheduleId(scheduleId);
        Map<Integer, Object> attendanceMap = attendanceList.stream().collect(Collectors.toMap(Attendance::getId,
                attendance -> attendance));

        for (EditAttendanceRequest request : requestList) {
            if (attendanceMap.containsKey(request.getId())) {
                Attendance attendance = (Attendance) attendanceMap.get(request.getId());
                attendance.setStatus(request.getStatus());
                attendance.setModifiedBy(users.getId());
                attendance.setModifiedAt(LocalDateTime.now());
//                request.setStatus(Constants.STATUS_TYPE.ATTEND);
                attendanceRepository.save(attendance);
//                attendanceList.remove(attendance);
            }
        }

        return requestList;
    }

    @Override
    public AutoAttendance autoCheckAttendance(AutoAttendance request) throws AmsException {

        LocalTime checkTime = request.getTime().toLocalTime();

        Attendance attendance = checkAttendance(request);

        if (attendance == null) {
            throw new AmsException(MessageCode.ATTENDANCE_NOT_FOUND);
        }

        Schedule schedule = scheduleRepository.findById(attendance.getScheduleId()).orElse(null);
        if (schedule == null || schedule.getStatus().equals(Constants.STATUS_TYPE.DELETED)) {
            throw new AmsException(MessageCode.SCHEDULE_NOT_FOUND);
        }


        TimeSlot timeSlot = timeSlotRepository.findById(schedule.getTimeSlotId()).orElse(null);
        if (schedule == null || schedule.getStatus().equals(Constants.STATUS_TYPE.DELETED)) {
            throw new AmsException(MessageCode.TIMESLOT_NOT_FOUND);
        }

        LocalTime startTime = LocalTime.of(timeSlot.getStartTime().getHour(),
                timeSlot.getStartTime().getMinute() - 5);

        LocalTime endTime = LocalTime.of(timeSlot.getStartTime().getHour(),
                timeSlot.getStartTime().getMinute() + 15);

        if(!attendance.getStatus().equals(Constants.STATUS_TYPE.NOT_YET)){
            return request;
        }
        if (checkTime.isAfter(startTime) && checkTime.isBefore(endTime)) {
            attendance.setStatus(Constants.STATUS_TYPE.ATTEND);
            attendanceRepository.save(attendance);
        } else {
            attendance.setStatus(Constants.STATUS_TYPE.ABSENT);
            attendanceRepository.save(attendance);
        }
        return request;
    }

    public Attendance checkAttendance(AutoAttendance request) throws AmsException {
        Room room = roomRepository.findRoomByCameraId(request.getCameraId());
        if (room == null) {
            return null;
        }
        Users users = usersRepository.findByUsername(request.getUsername()).orElse(null);
        if (users == null || room == null) {
            return null;
        }
        LocalDate date = request.getTime().toLocalDate();
        Optional<TimeSlot> timeSlot = timeSlotRepository.findByTime1(request.getTime());
        if(!timeSlot.isPresent()) return null;
        return attendanceRepository.findAttendanceByDateAndUserId(date, users.getId(), room.getId(), timeSlot.get().getId());
    }

    @Override
    public void updateAttendance(@NotEmpty List<AttendanceUpdateDto> dto) {
        Map<Integer, Attendance> attendanceMap = attendanceRepository.findAllById(dto.stream().map(AttendanceUpdateDto::getAttendanceId).collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(Attendance::getId, u -> u));
        for (AttendanceUpdateDto attendanceUpdateDto : dto) {
            attendanceMap.get(attendanceUpdateDto.getAttendanceId()).setStatus(attendanceUpdateDto.getStatus());
        }
        attendanceRepository.saveAll(attendanceMap.values());
    }


    @Override
    public Page<ListAttendenceResponse> getList(LocalDate date, Integer classId, Integer page, Integer size) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD");
//        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
        Page<IListAttendenceResponse> listAttendenceResponses = attendanceRepository.findAttendanceByClassIdAndDate(classId, date, PageRequest.of(page, size));
        return listAttendenceResponses.map(ListAttendenceResponse::new);
    }

    @Override
    public byte[] exportAttendance(Integer semesterId, Integer userId, Integer classId) throws AmsException {
        HSSFWorkbook workbook = new HSSFWorkbook();

        try {
            List<AttendanceDto> attendances = attendanceRepository.findAttendance(semesterId, userId, classId).stream().map(AttendanceDto::new).collect(Collectors.toList());
            if (attendances.isEmpty()) {
                throw new AmsException(MessageCode.CONTENT_EMPTY);
            }
            Sheet sheet = workbook.createSheet("Attendance List");

            // Create header row
            Row headerRow = sheet.createRow(0);

            // Define headers
            String[] headers = {
                    "No", "Semester Name", "Day", "User Name", "Subject Code", "Description",
                    "Status", "Created By", "Created At", "Modified By", "Modified At"
            };
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            int rowNum = 1;
            for (AttendanceDto attendance : attendances) {
                Row dataRow = sheet.createRow(rowNum++);

                dataRow.createCell(0).setCellValue(rowNum - 1);
               // dataRow.createCell(1).setCellValue(Objects.requireNonNullElse(attendance.getSemesterName(), "N/A"));
                dataRow.createCell(2).setCellValue(Objects.requireNonNullElse(attendance.getDay(), "N/A"));
                dataRow.createCell(3).setCellValue(Objects.requireNonNullElse(attendance.getUserName(), "N/A"));
                dataRow.createCell(4).setCellValue(Objects.requireNonNullElse(attendance.getSubjectCode(), "N/A"));
                dataRow.createCell(5).setCellValue(Objects.requireNonNullElse(attendance.getDescription(), "N/A"));
                dataRow.createCell(6).setCellValue(Objects.requireNonNullElse(attendance.getStatus(), "N/A"));
                dataRow.createCell(7).setCellValue(Objects.requireNonNullElse(attendance.getCreatedBy(), "N/A"));
                dataRow.createCell(8).setCellValue(attendance.getCreatedAt() != null ? attendance.getCreatedAt().format(formatter) : "N/A");
                dataRow.createCell(9).setCellValue(Objects.requireNonNullElse(attendance.getModifiedBy(), "N/A"));
                dataRow.createCell(10).setCellValue(attendance.getModifiedAt() != null ? attendance.getModifiedAt().format(formatter) : "N/A");
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
    public List<AddAttendanceRequest> importAttendances(List<AddAttendanceRequest> attendanceRequests) {
        List<AddAttendanceRequest> attendances = new ArrayList<>();
        for (AddAttendanceRequest request : attendanceRequests) {
            attendances.add(request);
        }
        return attendances;
    }

    @Override
    public List<AttendanceDto> findAttendance(Integer semesterId, Integer userId, Integer classId) {
        return attendanceRepository.findAttendance(semesterId, userId, classId).stream().map(AttendanceDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<IClassDto> attendanceClassDropdown(Integer semesterId) {
        Users users = BaseUserDetailsService.USER.get();
        if(users == null) return new ArrayList<>();

        return attendanceRepository.attendanceClassDropdown(semesterId, users.getId());
    }

}
