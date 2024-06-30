package vn.attendance.service.attendance.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.*;
import vn.attendance.repository.*;
import vn.attendance.service.attendance.request.*;
import vn.attendance.service.attendance.service.AttendanceService;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    @Autowired
    DailyLogRepository dailyLogRepository;

    @Override
    public List<AttendanceDto> attendanceReport(String semesterName, Integer classId) throws Exception {
        Users users = BaseUserDetailsService.USER.get();

        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        List<IAttendanceDto> attendanceDtos = new ArrayList<>();
        List<AttendanceDto> attendanceDto =  new ArrayList<>();
        if (users.getRoleId() == 4) {
            attendanceDtos = attendanceRepository.attendanceReportStudent(semesterName, users.getId(), classId);
            attendanceDto = attendanceDtos.stream().map(AttendanceDto::new).collect(Collectors.toList());
        } else if (users.getRoleId() == 1 || users.getRoleId() == 2 || users.getRoleId() == 3) {
            attendanceDtos = attendanceRepository.attendanceReportTeacher(semesterName, users.getId(), classId);
            attendanceDto = attendanceDtos.stream().map(AttendanceDto::new).collect(Collectors.toList());
        }
        return attendanceDto;
    }

//    @Override
//    public List<AttendanceDto> findAttendance(String semesterName, Integer userId, Integer classId) {
//        return attendanceRepository.findAttendance(semesterName, userId, classId)
//                .stream().map(AttendanceDto::new).collect(Collectors.toList());
////        return null;
//    }

    @Override
    public List<AddAttendanceRequest> addAttendance(List<AddAttendanceRequest> requestList) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();

        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        if (users.getRoleId() != 1 || users.getRoleId() != 2) {
            throw new AmsException(MessageCode.USER_NOT_AUTHORITY);
        }


        return List.of();
    }

    @Override
    public List<EditAttendanceRequest> checkAttendance(List<EditAttendanceRequest> requestList, LocalDate date) throws AmsException {

        Users users = BaseUserDetailsService.USER.get();

        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        if (users.getRoleId() != 1 && users.getRoleId() != 2 && users.getRoleId() != 3) {
            throw new AmsException(MessageCode.USER_NOT_AUTHORITY);
        }

        LocalTime checkTime = LocalTime.now();

        if (users.getRoleId() == 3 && checkTime.isAfter(LocalTime.of(23, 59)))
            throw new AmsException(MessageCode.CHECK_ATTEND_INVALID_TIME);

        List<Attendance> attendanceList = attendanceRepository.findAttendanceByDate(date);
        Map<Integer, Object> attendanceMap = attendanceList.stream().collect(Collectors.toMap(Attendance::getId,
                attendance -> attendance));

        for (EditAttendanceRequest request : requestList) {
            if (attendanceMap.containsKey(request.getId())) {
                Attendance attendance = (Attendance) attendanceMap.get(request.getId());
                attendance.setStatus(Constants.STATUS_TYPE.ATTEND);
                request.setStatus(Constants.STATUS_TYPE.ATTEND);
                attendanceRepository.save(attendance);
            }
        }
        return requestList;
    }

    @Override
    public AutoAttendance autoCheckAttendance(AutoAttendance request) throws AmsException {

//        Room room = roomRepository.findRoomByRoomName(request.getRoom());
        Users users = usersRepository.findByUsername(request.getUserName()).orElse(null);

        LocalDate date = request.getTime().toLocalDate();
        LocalTime checkTime = request.getTime().toLocalTime();

        Attendance attendance = attendanceRepository.findAttendanceByDateAndUserId(date, users.getId());

        Schedule schedule = scheduleRepository.findById(attendance.getScheduleId()).orElse(null);

        TimeSlot timeSlot = timeSlotRepository.findById(schedule.getTimeSlotId()).orElse(null);

        LocalTime startTime = LocalTime.of(timeSlot.getStartTime().getHour(),
                timeSlot.getStartTime().getMinute() - 15);

        LocalTime endTime = LocalTime.of(timeSlot.getStartTime().getHour(),
                timeSlot.getStartTime().getMinute() + 5);

        if (attendance == null) {
            throw new AmsException(MessageCode.ATTENDANCE_NOT_FOUND);
        }

        if (timeSlot.getStatus().equals(Constants.STATUS_TYPE.ACTIVE)) {
            if (checkTime.isAfter(startTime) && checkTime.isBefore(endTime)) {
                attendance.setStatus(Constants.STATUS_TYPE.ATTEND);
                attendanceRepository.save(attendance);
            } else {
                attendance.setStatus(Constants.STATUS_TYPE.ABSENT);
                attendanceRepository.save(attendance);
            }
        }

        DailyLog dailyLog = new DailyLog();
        dailyLog.setActivityTimestamp(request.getTime());
        dailyLog.setUserId(users.getId());

        dailyLogRepository.save(dailyLog);
        return request;
    }
}
