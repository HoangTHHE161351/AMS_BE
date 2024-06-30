package vn.attendance.service.attendance.service;

import vn.attendance.exception.AmsException;
import vn.attendance.service.attendance.request.AddAttendanceRequest;
import vn.attendance.service.attendance.request.AutoAttendance;
import vn.attendance.service.attendance.request.EditAttendanceRequest;
import vn.attendance.service.attendance.request.AttendanceDto;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {

    List<AttendanceDto> attendanceReport(String semesterName, Integer classId) throws Exception;

//    List<AttendanceDto> findAttendance(String semesterName, Integer userId, Integer classId) throws Exception;

    List<AddAttendanceRequest> addAttendance(List<AddAttendanceRequest> requestList) throws Exception;

    List<EditAttendanceRequest> checkAttendance(List<EditAttendanceRequest> requestList, LocalDate date) throws Exception;

    AutoAttendance autoCheckAttendance(AutoAttendance request) throws AmsException;
}
