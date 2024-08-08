package vn.attendance.service.attendance.service;

import org.springframework.data.domain.Page;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Attendance;
import vn.attendance.service.attendance.request.*;
import vn.attendance.service.attendance.response.AttendanceDTO;
import vn.attendance.service.attendance.response.IAttendanceDTO;
import vn.attendance.service.attendance.response.ListAttendenceResponse;
import vn.attendance.service.classRoom.response.ClassRoomDto;
import vn.attendance.service.classRoom.response.IClassDto;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {

    List<AttendanceDto> attendanceReport(Integer semesterId, Integer classId) throws Exception;

    List<AttendanceDTO> listStudentAttendance(Integer scheduleId) throws Exception;

    List<EditAttendanceRequest> checkAttendance(List<EditAttendanceRequest> requestList, Integer scheduleId) throws Exception;

    AutoAttendance autoCheckAttendance(AutoAttendance request) throws AmsException;

    Attendance checkAttendance(AutoAttendance request) throws AmsException;

    void updateAttendance(List<AttendanceUpdateDto> dto);

    byte[] exportAttendance(Integer semesterId, Integer userId, Integer classId) throws AmsException;
    List<AddAttendanceRequest> importAttendances(List<AddAttendanceRequest> attendanceRequests);
    Page<ListAttendenceResponse> getList(LocalDate date, Integer classId, Integer page, Integer size);
    List<AttendanceDto> findAttendance(Integer semesterId, Integer userId, Integer classId);

    List<IClassDto> attendanceClassDropdown(Integer semesterId);
}

