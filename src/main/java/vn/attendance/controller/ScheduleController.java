package vn.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.attendance.helper.ApiResponse;
import vn.attendance.service.schedule.request.AddScheduleRequest;
import vn.attendance.service.schedule.request.EditScheduleRequest;
import vn.attendance.service.schedule.service.ScheduleService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/schedule")
public class ScheduleController {

    @Autowired
    ScheduleService scheduleService;

    @GetMapping("view-schedule")
    public ApiResponse<?> getSchedule(@RequestParam LocalDate date) throws Exception {
        return ApiResponse.okStatus(scheduleService.findScheduleByUserId(date));
    }

    @PostMapping("import_schedule")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ApiResponse<?> importSchedule(@RequestBody List<AddScheduleRequest> scheduleList,
                                         @RequestParam String semesterName) throws Exception {
        return ApiResponse.okStatus(scheduleService.importSchedule(scheduleList, semesterName));
    }

    @PostMapping("add-schedule")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ApiResponse<?> addSchedule(@RequestBody AddScheduleRequest request,
                                      @RequestParam String semesterName,
                                      @RequestParam LocalDate date) throws Exception {
        return ApiResponse.okStatus(scheduleService.addSchedule(1, request, semesterName, date));
    }

    @PutMapping("edit-schedule")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ApiResponse<?> editSchedule(@RequestBody EditScheduleRequest editScheduleRequest,
                                       @RequestParam LocalDate date) throws Exception {
        return ApiResponse.okStatus(scheduleService.editSchedule(editScheduleRequest, date));
    }

    @GetMapping("schedule-details")
    public ApiResponse<?> getScheduleDetails(@RequestParam Integer id) throws Exception {
        return ApiResponse.okStatus(scheduleService.getScheduleById(id));
    }

    @DeleteMapping("delete-schedule")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ApiResponse<?> deleteSchedule(@RequestParam Integer id) throws Exception {
        return ApiResponse.okStatus(scheduleService.deleteSchedule(id));
    }

    @GetMapping("student-schedule")
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public ApiResponse<?> getSchedule(@RequestParam LocalDate from,
                                      @RequestParam LocalDate to) throws Exception {
        return ApiResponse.okStatus(scheduleService.findScheduleByStudentId(from, to));
    }

    @GetMapping("export-schedule")
    public ApiResponse<?> exportSchedule() throws Exception {
        return ApiResponse.okStatus(scheduleService.exportSchedules());
    }
}
