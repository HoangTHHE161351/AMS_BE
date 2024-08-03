package vn.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.attendance.exception.AmsException;
import vn.attendance.helper.ApiResponse;
import vn.attendance.service.attendance.request.AddAttendanceRequest;
import vn.attendance.service.attendance.request.AttendanceUpdateDto;
import vn.attendance.service.attendance.request.AutoAttendance;
import vn.attendance.service.attendance.request.EditAttendanceRequest;
import vn.attendance.service.attendance.service.AttendanceService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/attendance")
public class AttendanceController {

    @Autowired
    AttendanceService attendanceService;

    @GetMapping("attendance-report")
    public ApiResponse<?> attendanceReport(@RequestParam Integer semesterId,
                                           @RequestParam Integer classId) throws Exception {
        return ApiResponse.okStatus(attendanceService.attendanceReport(semesterId, classId));
    }

    @GetMapping("attendance-class")
    public ApiResponse<?> checkAttendanceInClass(@RequestParam Integer scheduleId) throws Exception {
        return ApiResponse.okStatus(attendanceService.listStudentAttendance(scheduleId));
    }

    @PutMapping("check-attendance")
    @PreAuthorize("hasAuthority('ADMIN, TEACHER, STAFF')")
    public ApiResponse<?> checkAttendance(@RequestBody List<EditAttendanceRequest> requestList,
                                          @RequestParam Integer scheduleId) throws Exception {
        return ApiResponse.okStatus(attendanceService.checkAttendance(requestList, scheduleId));
    }

    @PutMapping("auto-check-attendance")
    public ApiResponse<?> autoCheckAttendance(@RequestBody AutoAttendance request) throws Exception {
        return ApiResponse.okStatus(attendanceService.autoCheckAttendance(request));
    }

    @PutMapping("update-attendance")
    public ApiResponse<?> updateAttendance(@RequestBody @Valid List<AttendanceUpdateDto> dto) {
        attendanceService.updateAttendance(dto);
        return ApiResponse.okStatus();
    }

    @GetMapping("view-attendance")
    public ApiResponse<?> viewAttendance(@RequestParam Integer semesterId,
                                         @RequestParam Integer userId,
                                         @RequestParam Integer classId) {
        return ApiResponse.okStatus(attendanceService.findAttendance(semesterId, userId, classId));
    }

    @PostMapping("import-attendance")
    public ApiResponse<?> importAttendance(@RequestBody List<AddAttendanceRequest> attendanceRequests) {
        return ApiResponse.okStatus(attendanceService.importAttendances(attendanceRequests));
    }

    @GetMapping("export-attendance")
    public ResponseEntity<byte[]> exportAttendance(@RequestParam(required = false) Integer semesterId,
                                                   @RequestParam(required = false) Integer userId,
                                                   @RequestParam(required = false) Integer classId) throws AmsException {
        byte[] bytes = attendanceService.exportAttendance(semesterId, userId, classId);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=attendances.xls");
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(bytes.length));

        return ResponseEntity.ok().headers(headers).body(bytes);
    }

    @GetMapping("list-student-class")
    public ApiResponse<?> listStudentsInClass(@RequestParam(required = false) Integer classId,
                                              @RequestParam(required = false) LocalDate date,
                                              @RequestParam(defaultValue = "1") int page,
                                              @RequestParam(defaultValue = "10") int size) throws Exception {
        return ApiResponse.okStatus(attendanceService.getList(date, classId, page, size));
    }
}
