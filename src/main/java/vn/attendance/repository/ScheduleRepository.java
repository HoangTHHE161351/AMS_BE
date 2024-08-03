package vn.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.attendance.model.Schedule;
import vn.attendance.service.schedule.response.IScheduleDto;
import vn.attendance.service.schedule.response.ScheduleDto;
import vn.attendance.service.server.dto.IScheduleCamDto;

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

    @Query(value = "select s.id as id, sm.semester_name as semester, c.class_name as className, sb.code as subjectCode, " +
            "s.user_id as usersId, u.user_name as teacherCode, s.learn_timestamp as date, t.slot_name as time, " +
            "r.room_name as room, s.description as description, s.status as status,  " +
            "s.created_at as createdAt, uc.user_name as createdBy, s.modified_at as modifiedAt, um.user_name as modifiedBy " +
            "from Schedules s " +
            "join Semester sm on s.semester_id = sm.id and sm.status = 'ACTIVE' " +
            "join Class_Room c on s.class_id = c.id and c.status = 'ACTIVE' " +
            "join Rooms r on s.room_id = r.id and r.status = 'ACTIVE' " +
            "join Subjects sb on s.subject_id = sb.id and sb.status = 'ACTIVE' " +
            "join Time_Slot t on s.time_slot_id = t.id and t.status = 'ACTIVE' " +
            "join Users u on u.id = s.user_id and u.status = 'ACTIVE' " +
            "LEFT JOIN Users uc ON s.created_by = uc.id and uc.status = 'ACTIVE' " +
            "LEFT JOIN Users um ON s.modified_by = um.id and um.status = 'ACTIVE' " +
            "where s.status = 'ACTIVE' " +
            "and s.id = :id", nativeQuery = true)
    IScheduleDto findScheduleById(@Param("id") Integer id);


    @Query("select new vn.attendance.service.schedule.response.ScheduleDto(s.id, sm.semesterName, c.className, sb.code," +
            "s.userId, u.username, s.learnTimestamp, t.slotName, s.roomId, r.roomName, s.description, s.status,s.createdAt," +
            "uc.username, s.modifiedAt, um.username, a.status) " +
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
            "and DATE(s.learnTimestamp) = DATE(:date)" +
            "and s.semester = :semesterId and (:userId is null or s.userId = :userId)")
    List<ScheduleDto> findAllScheduleDto(@Param("date") LocalDate date,
                                         @Param("semesterId") Integer semesterId,
                                         @Param("userId") Integer userId);


    @Query("select s from Schedule s where s.status = 'ACTIVE' and date(s.learnTimestamp) = date(:date)" +
            " and s.semester = :semesterId")
    List<Schedule> findAllSchedules(@Param("date") LocalDate date,
                                    @Param("semesterId") Integer semesterId);

    @Query("select s from Schedule s where s.status = 'ACTIVE' and s.semester = :semesterId")
    List<Schedule> finScheduleBySemester(@Param(("semesterId")) Integer semesterId);

    @Query(value = "SELECT s.* FROM schedules s WHERE s.status = 'ACTIVE' AND DATE(s.learn_timestamp) = DATE(:date) AND s.time_slot_id = :slotId", nativeQuery = true)
    List<Schedule> findScheduleByDateAndSlot(@Param("date") LocalDate date, @Param("slotId") Integer slotId);

    @Query(value = "select s.id as id, s.time_slot_id as timeSlot, c.id as cameraId from schedules s " +
            " inner join cameras c on c.room_id = s.room_id and c.status = 'ACTIVE' and c.camera_type = 'LCD' " +
            " inner join class_room cr on s.class_id = cr.id and cr.status='ACTIVE' " +
            " inner join student_class sc on cr.id = sc.class_id and sc.status='ACTIVE' and sc.student_id = :userId " +
            " where s.status = 'ACTIVE' " +
            " and DATE(s.learn_timestamp) = DATE(:date)",nativeQuery = true)
    List<IScheduleCamDto> getDeviceForStudentSchedule(@Param("userId") Integer userId, @Param("date") LocalDate date);

    @Query(value = "select s.id as id, s.time_slot_id as timeSlot, c.id as cameraId from schedules s " +
            " join cameras c on c.room_id = s.room_id and c.status = 'ACTIVE' and c.camera_type = 'LCD' " +
            " where s.status = 'ACTIVE' " +
            " and DATE(s.learn_timestamp) = DATE(:date) " +
            " and s.user_id = :userId ",nativeQuery = true)
    List<IScheduleCamDto> getDeviceForTeacherSchedule(@Param("userId") Integer userId, @Param("date") LocalDate date);

    @Query(value = "select count(*) from schedules s where s.room_id = :roomId and Date(s.learn_timestamp) >= Date(:now) and s.status = 'ACTIVE' ",nativeQuery = true)
    Integer countScheduleByRoom(Integer roomId, LocalDate now);

    @Query(value = "select count(*) from schedules s where s.subject_id = :subjectId and Date(s.learn_timestamp) >= Date(:now) and s.status = 'ACTIVE' ",nativeQuery = true)
    Integer countScheduleBySubject(Integer subjectId, LocalDate now);

    @Query(value = "select count(*) from schedules s where s.time_slot_id = :timeSlotId and Date(s.learn_timestamp) >= Date(:now) and s.status = 'ACTIVE' ",nativeQuery = true)
    Integer countScheduleByTimeSlot(Integer timeSlotId, LocalDate now);



    @Query(value = "select count(*) from schedules s where s.user_id = :teacherId and Date(s.learn_timestamp) >= Date(:now) and s.status = 'ACTIVE' ",nativeQuery = true)
    Integer countScheduleByTeacher(Integer teacherId, LocalDate now);
}