package vn.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.attendance.exception.AmsException;
import vn.attendance.helper.ApiResponse;
import vn.attendance.service.server.service.LogAccessService;

import java.time.LocalDate;

@RestController
@RequestMapping("api/v1/log")
public class LogController {
    @Autowired
    LogAccessService logAccessService;

    @GetMapping("get-logs")
    @PreAuthorize("hasAuthority('ADMIN, STAFF')")
    public ApiResponse<?> getLogs(@RequestParam(required = false) String search,
                                  @RequestParam(required = false) Integer type,
                                  @RequestParam(required = false) Integer roomId,
                                  @RequestParam(required = false) LocalDate date,
                                  @RequestParam(required = false) Integer notType,
                                  @RequestParam(defaultValue = "1") Integer page,
                                  @RequestParam(defaultValue = "20") Integer size) {

        if(date == null){
            date = LocalDate.now();
        }
        return ApiResponse.okStatus(logAccessService.findLog(search, type, roomId, date, notType, page, size));
    }

    @GetMapping("get-stranger-by-schedule")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ApiResponse<?> getStrangerBySchedule(@RequestParam(required = false) Integer scheduleId,
                                                @RequestParam(defaultValue = "1") Integer page,
                                                @RequestParam(defaultValue = "20") Integer size) throws AmsException {
        return ApiResponse.okStatus(logAccessService.findStrangerBySchedule(scheduleId, page, size));
    }
}
