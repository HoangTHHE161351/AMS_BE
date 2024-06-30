package vn.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.attendance.model.Schedule;
import vn.attendance.service.schedule.response.ScheduleDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

    @Query("select s " +
            "from Schedule s join Semester sm on s.semester = sm.id and sm.status = 'ACTIVE'  " +
            "join ClassRoom c on s.classId = c.id and c.status = 'ACTIVE' " +
            "join Room r on s.roomId = r.id and r.status = 'ACTIVE' " +
            "join Subject sb on s.subjectId = sb.id and sb.status = 'ACTIVE' " +
            "join TimeSlot t on s.timeSlotId = t.id and t.status = 'ACTIVE' " +
            "join Users u on u.id = s.userId and u.status = 'ACTIVE' " +
            "LEFT JOIN Users uc ON s.createdBy = uc.id and uc.status = 'ACTIVE' " +
            "LEFT JOIN Users um ON s.modifiedBy = um.id and um.status = 'ACTIVE' " +
            "where s.status = 'ACTIVE' " +
            "and s.id = :id")
    Optional<Schedule> findById(@Param("id") Integer id);

    @Query("select new vn.attendance.service.schedule.response.ScheduleDto(s.id, sm.semesterName, c.className, sb.code, " +
            "s.userId, u.username, s.learnTimestamp, t.description, r.roomName, s.description, s.status," +
            " a.status, s.createdAt, uc.username, s.modifiedAt, um.username) " +
            "from Schedule s join Semester sm on s.semester = sm.id and sm.status = 'ACTIVE' " +
            "join Attendance a on a.scheduleId = s.id " +
            "join ClassRoom c on s.classId = c.id and c.status = 'ACTIVE' " +
            "join Room r on s.roomId = r.id and r.status = 'ACTIVE' " +
            "join Subject sb on s.subjectId = sb.id and sb.status = 'ACTIVE' " +
            "join TimeSlot t on s.timeSlotId = t.id and t.status = 'ACTIVE' " +
            "join Users u on u.id = s.userId and u.status = 'ACTIVE' " +
            "LEFT JOIN Users uc ON s.createdBy = uc.id and uc.status = 'ACTIVE' " +
            "LEFT JOIN Users um ON s.modifiedBy = um.id and um.status = 'ACTIVE' " +
            "where s.status = 'ACTIVE' " +
            "and s.id = :id")
    ScheduleDto findScheduleById(@Param("id") Integer id);

    @Query("select new vn.attendance.service.schedule.response.ScheduleDto(s.id, sm.semesterName, c.className, sb.code, " +
            "s.userId, u.username, s.learnTimestamp, t.description, r.roomName, s.description, s.status," +
            " a.status, s.createdAt, uc.username, s.modifiedAt, um.username) " +
            "from Schedule s join Semester sm on s.semester = sm.id and sm.status = 'ACTIVE' " +
            "join Attendance a on a.scheduleId = s.id and a.userId = s.userId " +
            "join ClassRoom c on s.classId = c.id and c.status = 'ACTIVE' " +
            "join Room r on s.roomId = r.id and r.status = 'ACTIVE' " +
            "join Subject sb on s.subjectId = sb.id and sb.status = 'ACTIVE' " +
            "join TimeSlot t on s.timeSlotId = t.id and t.status = 'ACTIVE' " +
            "join Users u on u.id = s.userId and u.status = 'ACTIVE' " +
            "LEFT JOIN Users uc ON s.createdBy = uc.id and uc.status = 'ACTIVE' " +
            "LEFT JOIN Users um ON s.modifiedBy = um.id and um.status = 'ACTIVE' " +
            "where s.status = 'ACTIVE' " +
            "and date(s.learnTimestamp) = date(:date) " +
            "and s.semester = :semesterId and (:userId is null or s.userId = :userId)")
    List<ScheduleDto> findAllScheduleDto(@Param("date") LocalDate date,
                                         @Param("semesterId") Integer semesterId,
                                         @Param("userId") Integer userId);

    @Query("select s from Schedule s where s.status = 'ACTIVE' and date(s.learnTimestamp) = date(:date)" +
            " and s.semester = :semesterId")
    List<Schedule> findAllSchedules(@Param("date") LocalDate date,
                                    @Param("semesterId") Integer semesterId);

    @Query("select new vn.attendance.service.schedule.response.ScheduleDto(s.id, sm.semesterName, c.className, sb.code, " +
            "s.userId, u.username, s.learnTimestamp, t.description, r.roomName, s.description, s.status," +
            " a.status, s.createdAt, uc.username, s.modifiedAt, um.username) " +
            "from Schedule s join Semester sm on s.semester = sm.id and sm.status = 'ACTIVE' " +
            "join Attendance a on a.scheduleId = s.id " +
            "join ClassRoom c on s.classId = c.id and c.status = 'ACTIVE' " +
            "join Room r on s.roomId = r.id and r.status = 'ACTIVE' " +
            "join Subject sb on s.subjectId = sb.id and sb.status = 'ACTIVE' " +
            "join TimeSlot t on s.timeSlotId = t.id and t.status = 'ACTIVE' " +
            "join Users u on u.id = s.userId and u.status = 'ACTIVE' " +
            "LEFT JOIN Users uc ON s.createdBy = uc.id and uc.status = 'ACTIVE' " +
            "LEFT JOIN Users um ON s.modifiedBy = um.id and um.status = 'ACTIVE' " +
            "where s.status = 'ACTIVE' " +
            "and s.semester = :semesterId")
    List<ScheduleDto> findAllScheduleBySemester(@Param("semesterId") Integer semesterId);


}