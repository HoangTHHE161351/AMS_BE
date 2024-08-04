package vn.attendance.service.timeSlots.service;

import org.springframework.data.domain.Page;
import vn.attendance.exception.AmsException;
import vn.attendance.model.TimeSlot;
import vn.attendance.service.timeSlots.request.AddTimeSlotRequest;
import vn.attendance.service.timeSlots.request.EditTimeSlotRequest;
import vn.attendance.service.timeSlots.response.TimeSlotDto;

import java.util.List;

public interface TimeSlotService {
    Page<TimeSlotDto> findAllTimeSlots(String search, String status, int page, int size);

    AddTimeSlotRequest addTimeSLot(AddTimeSlotRequest request, Integer option) throws AmsException;

    TimeSlot editTimeSlot(EditTimeSlotRequest request) throws AmsException;

    TimeSlot deleteSlot(Integer id) throws AmsException;

    List<TimeSlot> findAllTimeSlots() throws AmsException;

    List<AddTimeSlotRequest> importTimeSlots(List<AddTimeSlotRequest> timeSlotList) throws AmsException;

    byte[] exportTimeSlot() throws AmsException;
}
