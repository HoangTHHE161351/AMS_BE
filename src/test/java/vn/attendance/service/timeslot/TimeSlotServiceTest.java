package vn.attendance.service.timeslot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.TimeSlot;
import vn.attendance.model.Users;
import vn.attendance.repository.TimeSlotRepository;
import vn.attendance.service.timeSlots.request.AddTimeSlotRequest;
import vn.attendance.service.timeSlots.request.EditTimeSlotRequest;
import vn.attendance.service.timeSlots.service.impl.TimeSlotServiceImpl;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TimeSlotServiceTest {
    @Mock
    private TimeSlotRepository timeSlotRepository;

    @InjectMocks
    private TimeSlotServiceImpl timeSlotService;

    private Users testUser;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @BeforeEach
    public void setUp() {
        testUser = new Users();
        testUser.setRoleId(1);
        testUser.setId(1);
        BaseUserDetailsService.USER.set(testUser);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addTimeSlotTestSuccess() throws AmsException {

        AddTimeSlotRequest request = new AddTimeSlotRequest(
                "07:30:00",
               "09:30:00",
                "slot2",
                "new timeslot",
                null,
                null

        );

        AddTimeSlotRequest response = timeSlotService.addTimeSLot(request, 1);

        Assertions.assertNotNull(response);
        assertEquals("SUCCESS", response.getStatusAdd());
        assertNull(response.getErrorMess());
    }

    @Test
    void deleteTimeSlot() throws AmsException {
        int id = 1;
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setId(id);
        timeSlot.setStatus(Constants.STATUS_TYPE.ACTIVE);

        when(timeSlotRepository.findById(id)).thenReturn(Optional.of(timeSlot));
        timeSlotService.deleteSlot(id);

        assertEquals(Constants.STATUS_TYPE.DELETED, timeSlot.getStatus());
    }
    @Test
    void updateTimeSlotSuccess() throws AmsException {
        Integer id = 3;
        EditTimeSlotRequest request = new EditTimeSlotRequest();
        request.setId(id);
        request.setStartTime("09:30:00");
        request.setEndTime("10:30:00");
        request.setSlotName("Slot23");
        request.setDescription("New Description");

        TimeSlot existingTimeSlot = new TimeSlot();
        existingTimeSlot.setId(id);
        existingTimeSlot.setStatus(Constants.STATUS_TYPE.ACTIVE);
        existingTimeSlot.setStartTime(LocalTime.of(8, 0));
        existingTimeSlot.setEndTime(LocalTime.of(9, 0));
        when(timeSlotRepository.findById(id)).thenReturn(Optional.of(existingTimeSlot));
        when(timeSlotRepository.findByTime(LocalTime.parse("09:30:00", formatter), LocalTime.parse("10:30:00", formatter)))
                .thenReturn(Optional.empty());
        when(timeSlotRepository.findByTimeSlotName("Slot23")).thenReturn(null);
        when(timeSlotRepository.save(any(TimeSlot.class))).thenAnswer(i -> i.getArguments()[0]);
        TimeSlot response = timeSlotService.editTimeSlot(request);
        assertEquals(Constants.STATUS_TYPE.ACTIVE, response.getStatus());
        assertEquals("Slot23", response.getSlotName());
        assertEquals("09:30", response.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        assertEquals("10:30", response.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        assertEquals("New Description", response.getDescription());

        verify(timeSlotRepository).save(existingTimeSlot);
        verify(timeSlotRepository).findById(id);
        verify(timeSlotRepository).countScheduleByTimeSlot(id);
        verify(timeSlotRepository).findByTime(LocalTime.parse("09:30:00", formatter), LocalTime.parse("10:30:00", formatter));
        verify(timeSlotRepository).findByTimeSlotName("Slot23");
    }

    @Test
    void addTimeSlotTestDuplicateTime() throws AmsException {
        AddTimeSlotRequest request = new AddTimeSlotRequest(
                "07:30:00",
                "09:30:00",
                "slot2",
                "new timeslot",
                null,
                null
        );

        when(timeSlotRepository.findByTime(LocalTime.parse("07:30:00", formatter), LocalTime.parse("09:30:00", formatter)))
                .thenReturn(Optional.of(new TimeSlot()));

        AddTimeSlotRequest response = timeSlotService.addTimeSLot(request,2);

        assertEquals("FAILED", response.getStatusAdd());
        assertEquals(MessageCode.TIMESLOT_ALREADY_EXISTS.getCode(), response.getErrorMess());
    }

    // Test case to verify adding a time slot with an already existing name
    @Test
    void addTimeSlotTestDuplicateName() throws AmsException {
        AddTimeSlotRequest request = new AddTimeSlotRequest(
                "07:30:00",
                "09:30:00",
                "slot2",
                "new timeslot",
                null,
                null
        );

        when(timeSlotRepository.findByName("slot2")).thenReturn(Optional.of(new TimeSlot()));

        AddTimeSlotRequest response = timeSlotService.addTimeSLot(request,2);

        assertEquals("FAILED", response.getStatusAdd());
        assertEquals(MessageCode.TIMESLOT_ALREADY_EXISTS.getCode(), response.getErrorMess());
    }

    // Test case to verify deleting a non-existing time slot
    @Test
    void deleteNonExistingTimeSlot() throws AmsException {
        int id = 999;
        when(timeSlotRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(AmsException.class, () -> {
            timeSlotService.deleteSlot(id);
        });

        assertEquals(MessageCode.TIMESLOT_NOT_FOUND.getCode(), exception.getMessage());
    }

    // Test case to verify updating a non-existing time slot
    @Test
    void updateNonExistingTimeSlot() throws AmsException {
        Integer id = 999;
        EditTimeSlotRequest request = new EditTimeSlotRequest();
        request.setId(id);
        request.setStartTime("09:30:00");
        request.setEndTime("10:30:00");
        request.setSlotName("Slot999");
        request.setDescription("Non-existing Description");

        when(timeSlotRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(AmsException.class, () -> {
            timeSlotService.editTimeSlot(request);
        });

        assertEquals(MessageCode.TIMESLOT_NOT_FOUND.getCode(), exception.getMessage());
    }

    // Test case to verify fetching all time slots
    @Test
    void findAllTimeSlotsTest() throws AmsException {
        List<TimeSlot> timeSlots = new ArrayList<>();
        timeSlots.add(new TimeSlot());
        when(timeSlotRepository.findAllTimeSlots()).thenReturn(timeSlots);

        List<TimeSlot> result = timeSlotService.findAllTimeSlots();
        assertEquals(1, result.size());
        verify(timeSlotRepository).findAllTimeSlots();
    }


}