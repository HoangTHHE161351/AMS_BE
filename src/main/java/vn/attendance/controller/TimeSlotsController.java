package vn.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.attendance.exception.AmsException;
import vn.attendance.helper.ApiResponse;
import vn.attendance.service.timeSlots.request.AddTimeSlotRequest;
import vn.attendance.service.timeSlots.request.EditTimeSlotRequest;
import vn.attendance.service.timeSlots.response.TimeSlotDto;
import vn.attendance.service.timeSlots.service.TimeSlotService;
import vn.attendance.util.DataUtils;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/timeslots")
public class TimeSlotsController {
    @Autowired
    TimeSlotService timeSlotService;

    @GetMapping("all-timeslot")
    public ApiResponse<Page<TimeSlotDto>> findAllTimeSlotDto(@RequestParam(required = false) String search,
                                                             @RequestParam(required = false) String status,
                                                             @RequestParam(defaultValue = "1") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.okStatus(timeSlotService.findAllTimeSlots(search, status, page, size));
    }

    @PostMapping("add-timeslot")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<?> addTimeSLot(@Valid @RequestBody AddTimeSlotRequest request) throws AmsException {

        return ApiResponse.okStatus(timeSlotService.addTimeSLot(request, 1));
    }

    @PutMapping("edit-timeslot")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<?> editTimeSLot(@Valid @RequestBody EditTimeSlotRequest request) throws AmsException {
        timeSlotService.editTimeSlot(request);
        return ApiResponse.okStatus("Update Time Slot Success");
    }

    @DeleteMapping("delete-timeslot")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<?> deleteTimeSlot(@RequestParam Integer id) throws AmsException {
        timeSlotService.deleteSlot(id);
        return ApiResponse.okStatus("Delete Time Slot Success");
    }

    @GetMapping("dropdown-timeslot")
    public ApiResponse<?> dropdownTimeSlot() throws AmsException {
        return ApiResponse.okStatus(timeSlotService.findAllTimeSlots());
    }

    @GetMapping("weeks")
    public ApiResponse<?> getWeekOfYear(@RequestParam Integer year) {
        return ApiResponse.okStatus(DataUtils.getWeeksInYear(year));
    }

    @PostMapping("import-timeslot")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<?> importTimeSlot(@RequestBody List<AddTimeSlotRequest> timeSlotList) throws AmsException {
        return ApiResponse.okStatus(timeSlotService.importTimeSlots(timeSlotList));
    }

    @GetMapping("export-timeslot")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<byte[]> exportTimeSlot() throws AmsException {
        byte[] bytes = timeSlotService.exportTimeSlot();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=TimeSlot.xls");
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(bytes.length));

        return ResponseEntity.ok().headers(headers).body(bytes);
    }


}
