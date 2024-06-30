package vn.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.attendance.exception.AmsException;
import vn.attendance.helper.ApiResponse;
import vn.attendance.model.TimeSlot;
import vn.attendance.service.timeSlots.request.AddTimeSlotRequest;
import vn.attendance.service.timeSlots.request.EditTimeSlotRequest;
import vn.attendance.service.timeSlots.response.TimeSlotDto;
import vn.attendance.service.timeSlots.service.TimeSlotService;
import vn.attendance.util.DataUtils;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/timeslots")
public class TimeSlotsController {
    @Autowired
    TimeSlotService timeSlotService;

    @GetMapping("all-timeslot")
    public ApiResponse<Page<TimeSlotDto>> findAllTimeSlotDto(@RequestParam(defaultValue = "1") int page,
                                                                @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.okStatus(timeSlotService.findAllTimeSlots(page,size));
    }

    @PostMapping("add-timeslot")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<?> addTimeSLot(@Valid @RequestBody AddTimeSlotRequest request) throws AmsException {
        return ApiResponse.okStatus(timeSlotService.addTimeSLot(request));
    }

    @PutMapping("edit-timeslot")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<?> editTimeSLot(@Valid @RequestBody EditTimeSlotRequest request, @PathVariable Integer id) throws AmsException {
        Optional<TimeSlot> timeSlotUpdated = timeSlotService.editTimeSlot(id,request);
        if (timeSlotUpdated.isPresent())
            return ApiResponse.okStatus(timeSlotUpdated.get());
        else
            return ApiResponse.errorStatus("Timeslot not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("delete-timeslot")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<?> deleteTimeSlot(@PathVariable Integer id) throws AmsException {
        Optional<TimeSlot> deleteTimeSlot = timeSlotService.deleteSlot(id);
        if (deleteTimeSlot.isPresent())
            return ApiResponse.okStatus(deleteTimeSlot.get());
        else
            return ApiResponse.errorStatus("Timeslot not found!", HttpStatus.NOT_FOUND);
    }

    @GetMapping("dropdown-timeslot")
    public ApiResponse<?> dropdownTimeSlot() throws AmsException {
        return ApiResponse.okStatus(timeSlotService.findAllTimeSlots());
    }

    @GetMapping("weeks")
    public ApiResponse<?> getWeekOfYear(@RequestParam Integer year){
        return ApiResponse.okStatus(DataUtils.getWeeksInYear(year));
    }



}
