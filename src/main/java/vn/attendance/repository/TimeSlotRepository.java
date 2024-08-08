package vn.attendance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.attendance.model.TimeSlot;
import vn.attendance.service.timeSlots.response.TimeSlotDto;

import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Integer> {

    @Query("select count(ts.id) from TimeSlot ts where ts.status = 'ACTIVE' ")
    int countTimeSlot();

    @Query("select ts from TimeSlot ts where ts.status = 'ACTIVE'")
    List<TimeSlot> findAllTimeSlots();

    @Query(value = "select count(*) from schedules s where s.time_slot_id = :timeSlotId and s.status = 'ACTIVE' ",nativeQuery = true)
    Integer countScheduleByTimeSlot(Integer timeSlotId);

    @Query("SELECT new vn.attendance.service.timeSlots.response.TimeSlotDto (ts.id, ts.startTime, ts.endTime," +
            " ts.slotName, ts.description, ts.status, ts.createdAt, u.username, ts.modifiedAt, um.username) " +
            "from TimeSlot ts left join Users u on ts.createdBy = u.id " +
            "left join Users um on ts.modifiedBy = um.id " +
            "where (coalesce(:search, '') = '' or ts.slotName LIKE CONCAT('%', :search, '%')) " +
            "and (coalesce(:status, '') = '' or ts.status = :status)")
    Page<TimeSlotDto> findAllDto(@Param("search") String search,
                                 @Param("status") String status,
                                 Pageable pageable);

    @Query("SELECT ts FROM TimeSlot ts WHERE ts.startTime = :start AND ts.endTime = :end")
    Optional<TimeSlot> findByTime(LocalTime start, LocalTime end);

    @Query("SELECT ts FROM TimeSlot ts WHERE lower(ts.slotName) = lower(:name)")
    Optional<TimeSlot> findByName(String name);


    @Query("SELECT ts FROM TimeSlot ts WHERE ts.slotName = :name and ts.status = 'ACTIVE' ")
    TimeSlot findByTimeSlotName(String name);

//    @Query("select * from time_slot wher ")

    @Query("select ts from TimeSlot ts")
    List<TimeSlot> findAllTime();

    @Query("select new vn.attendance.service.timeSlots.response.TimeSlotDto (ts.id, ts.startTime, ts.endTime, ts.slotName, " +
            "ts.description, ts.status, ts.createdAt, u.username, ts.modifiedAt, um.username) " +
            "from TimeSlot ts left join Users u on ts.createdBy = u.id " +
            "left join Users um on ts.modifiedBy = um.id ")
    List<TimeSlotDto> findAllTimeSlotsDto();

    @Query(value = "select * from time_slot where (start_time - INTERVAL '10' MINUTE) <= TIME(:time1) and end_time >= (:time1)", nativeQuery = true)
    Optional<TimeSlot> findByTime1(LocalDateTime time1);

    @Query(value = "select * from time_slot where (start_time - INTERVAL '10' MINUTE) <= TIME(:time) and end_time > (:time)", nativeQuery = true)
    TimeSlot findByStartTime(LocalDateTime time);

    @Query(value = "SELECT * FROM time_slot WHERE start_time <= TIME(:time) AND (end_time + INTERVAL '10' MINUTE) > TIME(:time)", nativeQuery = true)
    TimeSlot findByEndTime(@Param("time") LocalDateTime time);
}