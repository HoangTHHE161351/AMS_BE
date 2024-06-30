package vn.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.attendance.helper.ApiResponse;
import vn.attendance.service.attendance.request.AutoAttendance;
import vn.attendance.service.attendance.request.EditAttendanceRequest;
import vn.attendance.service.attendance.service.AttendanceService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/attendance")
public class AttendanceController {

    @Autowired
    AttendanceService attendanceService;

    @GetMapping("attendance-report")
    public ApiResponse<?> attendanceReport(@RequestParam String semesterName,
                                           @RequestParam Integer classId) throws Exception {
        return ApiResponse.okStatus(attendanceService.attendanceReport(semesterName, classId));
    }

//    @GetMapping("list-attendance")
//    public ApiResponse<?> viewAttendance(@RequestParam String semesterName,
//                                         @RequestParam(required = false) Integer userId,
//                                         @RequestParam(required = false) Integer classId) throws Exception {
//        return ApiResponse.okStatus(attendanceService.findAttendance(semesterName, userId, classId));
//    }

    @PutMapping("check-attendance")
    public ApiResponse<?> checkAttendance(@RequestBody List<EditAttendanceRequest> requestList,
                                          @RequestParam(required = false) LocalDate date) throws Exception {
        return ApiResponse.okStatus(attendanceService.checkAttendance(requestList, date));
    }

    @PutMapping("auto-check-attendance")
    public ApiResponse<?> autoCheckAttendance(@RequestBody AutoAttendance request) throws Exception {
        return ApiResponse.okStatus(attendanceService.autoCheckAttendance(request));
    }
}
