package vn.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.attendance.exception.AmsException;
import vn.attendance.helper.ApiResponse;
import vn.attendance.service.notify.service.NotifyService;

import java.time.LocalDate;

@RestController
@RequestMapping("api/v1/notify")
public class NotifyController {

    @Autowired
    NotifyService notifyService;

    @GetMapping("get-notifies")
    public ApiResponse<?> getNotifies(@RequestParam(required = false) String search,
                                      @RequestParam(required = false) Integer roomId,
                                      @RequestParam(required = false) LocalDate date) throws AmsException {
        return ApiResponse.okStatus(notifyService.findAll(search, roomId, date));
    }

    @GetMapping("check-read-notify")
    public ApiResponse<?>  checkReadNotify() throws AmsException {
        return ApiResponse.okStatus(notifyService.checkReadNotify());
    }

    @PostMapping("set-read-notify")
    public ApiResponse<?> setReadNotify(@RequestParam(required = false) Integer notifyId) throws AmsException {
        notifyService.setReadNotify(notifyId);
        return ApiResponse.okStatus("set read success");
    }
}

