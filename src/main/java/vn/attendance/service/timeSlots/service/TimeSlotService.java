package vn.attendance.service.timeSlots.service;

import org.springframework.data.domain.Page;
import vn.attendance.exception.AmsException;
import vn.attendance.model.TimeSlot;
import vn.attendance.service.timeSlots.request.AddTimeSlotRequest;
import vn.attendance.service.timeSlots.request.EditTimeSlotRequest;
import vn.attendance.service.timeSlots.response.TimeSlotDto;

import java.util.List;
import java.util.Optional;

public interface TimeSlotService {
    Page<TimeSlotDto> findAllTimeSlots(int page, int size);

    AddTimeSlotRequest addTimeSLot(AddTimeSlotRequest request) throws AmsException;

    Optional<TimeSlot> editTimeSlot(Integer id, EditTimeSlotRequest request) throws AmsException;

    Optional<TimeSlot> deleteSlot(Integer id) throws AmsException;

    List<TimeSlot> findAllTimeSlots() throws AmsException;
   // byte[] exportTimeSlot() throws AmsException;
}
