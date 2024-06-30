package vn.attendance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.attendance.model.TimeSlot;
import vn.attendance.service.timeSlots.response.TimeSlotDto;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Integer> {

    @Query("select count(ts.id) from TimeSlot ts")
    int countTimeSlot();

    @Query("select ts from TimeSlot ts where ts.status = 'ACTIVE'")
    List<TimeSlot> findAllTimeSlots();

    @Query("SELECT new vn.attendance.service.timeSlots.response.TimeSlotDto(t.id,t.startTime," +
            "t.endTime,t.description, t.status) FROM TimeSlot t")
    Page<TimeSlotDto> findAllDto(Pageable pageable);

    @Query("SELECT ts FROM TimeSlot ts WHERE ts.startTime = :start AND ts.endTime = :end")
    Optional<TimeSlot> findByTime(LocalTime start, LocalTime end);

    @Query("select ts from TimeSlot ts")
    List<TimeSlot> findAllTime();
}