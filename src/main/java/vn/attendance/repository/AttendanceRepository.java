package vn.attendance.repository;

import org.apache.tomcat.jni.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.attendance.model.Attendance;
import vn.attendance.service.attendance.request.IAttendanceDto;
import vn.attendance.service.attendance.response.AttendanceDto;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

    @Query(value = "select a.id as id, a.user_id as userId, DAYNAME(s.learn_timestamp) as day, date(s.learn_timestamp) as learnDate,\n" +
            " concat(u.last_name, ' ', u.first_name) as userName, tc.user_name as teacherCode, c.class_name as className,\n" +
            " r.room_name as roomName, t.description as slot, sb.code as subjectCode, a.description as description,\n" +
            " a.status as status, a.created_at as createAt, uc.user_name as createBy, a.modified_at as modifiedAt, um.user_name as modifiedBy\n" +
            "from attendance a\n" +
            "join users u on a.user_id = u.id and u.status = 'ACTIVE'\n" +
            "join schedules s on a.schedule_id = s.id\n" +
            "join semester sm on s.semester_id = sm.id and sm.status = 'ACTIVE'\n" +
            "join time_slot t on s.time_slot_id = t.id and t.status = 'ACTIVE'\n" +
            "join subjects sb on s.subject_id = sb.id and sb.status = 'ACTIVE'\n" +
            "join class_room c on c.id = s.class_id and c.status = 'ACTIVE'\n" +
            "join student_class sc on a.user_id = sc.student_id and c.id = sc.class_id\n" +
            "join rooms r on r.id = s.room_id and r.status = 'ACTIVE'\n" +
            "left join Users uc on a.created_by = uc.id and u.status = 'ACTIVE'\n" +
            "left join Users um on a.modified_by = um.id and u.status = 'ACTIVE'\n" +
            "left join Users tc on s.user_id = tc.id and tc.status = 'ACTIVE'\n" +
            "where (:semesterName is null or sm.semester_name = :semesterName)\n" +
            "  and (:userId is null or a.user_id = :userId) and c.id = :classId\n", nativeQuery = true)
    List<IAttendanceDto> attendanceReportStudent(@Param("semesterName") String semesterName,
                                                 @Param("userId") Integer userId,
                                                 @Param("classId") Integer classId);

    @Query(value = "select a.id as id, a.user_id as userId, DAYNAME(s.learn_timestamp) as day, date(s.learn_timestamp) as learnDate, " +
            " concat(u.last_name, ' ', u.first_name) as userName, tc.user_name as teacherCode, c.class_name as className,  " +
            " r.room_name as roomName, t.description as slot, sb.code as subjectCode, a.description as description, " +
            " a.status as status, a.created_at as createdAt, uc.user_name as createdBy, a.modified_at as modifiedAt, um.user_name as modifiedBy " +
            "from attendance a " +
            "join users u on a.user_id = u.id and u.status = 'ACTIVE' " +
            "join schedules s on a.schedule_id = s.id " +
            "join semester sm on s.semester_id = sm.id and sm.status = 'ACTIVE' " +
            "join time_slot t on s.time_slot_id = t.id and t.status = 'ACTIVE' " +
            "join subjects sb on s.subject_id = sb.id and sb.status = 'ACTIVE' " +
            "join class_room c on c.id = s.class_id and c.status = 'ACTIVE' " +
            "join rooms r on r.id = s.room_id and r.status = 'ACTIVE' " +
            "left join Users uc on a.created_by = uc.id and u.status = 'ACTIVE' " +
            "left join Users um on a.modified_by = um.id and u.status = 'ACTIVE' " +
            "left join Users tc on s.user_id = tc.id and tc.status = 'ACTIVE' " +
            "where (:semesterName is null or sm.semester_name = :semesterName) " +
            "  and (:userId is null or a.user_id = :userId) and c.id = :classId ", nativeQuery = true)
    List<IAttendanceDto> attendanceReportTeacher(@Param("semesterName") String semesterName,
                                                 @Param("userId") Integer userId,
                                                 @Param("classId") Integer classId);

    @Query(value = "select a.id as id, a.user_id as userId, DAYNAME(s.learn_timestamp) as day, date(s.learn_timestamp) as learnDate, " +
            " concat(u.last_name, ' ', u.first_name) as userName, tc.user_name as teacherCode, c.class_name as className,  " +
            " r.room_name as roomName, t.description as slot, sb.code as subjectCode, a.description as description, " +
            " a.status as status, a.created_at as createdAt, uc.user_name as createdBy, a.modified_at as modifiedAt, um.user_name as modifiedBy " +
            "from attendance a " +
            "join users u on a.user_id = u.id and u.status = 'ACTIVE' " +
            "join schedules s on a.schedule_id = s.id " +
            "join semester sm on s.semester_id = sm.id and sm.status = 'ACTIVE' " +
            "join time_slot t on s.time_slot_id = t.id and t.status = 'ACTIVE' " +
            "join subjects sb on s.subject_id = sb.id and sb.status = 'ACTIVE' " +
            "join class_room c on c.id = s.class_id and c.status = 'ACTIVE' " +
            "join rooms r on r.id = s.room_id and r.status = 'ACTIVE' " +
            "left join Users uc on a.created_by = uc.id and u.status = 'ACTIVE' " +
            "left join Users um on a.modified_by = um.id and u.status = 'ACTIVE' " +
            "left join Users tc on s.user_id = tc.id and tc.status = 'ACTIVE' " +
            "where (:semesterName is null or sm.semester_name = :semesterName) " +
            "  and (:userId is null or a.user_id = :userId) " +
            "  and date(s.learn_timestamp) between :from and :to", nativeQuery = true)
    List<IAttendanceDto> findAttendanceInDate(@Param("semesterName") String semesterName,
                                              @Param("userId") Integer userId,
                                              @Param("from") LocalDate from,
                                              @Param("to") LocalDate to);

    @Query("select a from Attendance a join Schedule s on a.scheduleId = s.id and s.status = 'ACTIVE' " +
            "where a.scheduleId = :scheduleId and a.userId = :userId and s.semester = :semesterId and a.status = 'ACTIVE'")
    Attendance findByUserIdAndScheduleId(@Param("userId") Integer userId,
                                         @Param("scheduleId") Integer scheduleId,
                                         @Param("semesterId") Integer semesterId);

    @Query("select a " +
            "from Attendance a " +
            "    join Users u on u.id = a.userId and u.status = 'ACTIVE' " +
            "    join Schedule s on a.scheduleId = s.id and s.status = 'ACTIVE' " +
            "    left join StudentClass sc on a.userId = sc.studentId and sc.classId = s.classId " +
            "where date(s.learnTimestamp) = :date")
    List<Attendance> findAttendanceByDate(@Param("date") LocalDate date);

    @Query(value = "select a " +
            "from attendance a " +
            "    join users u on u.id = a.user_id and u.status = 'ACTIVE' " +
            "    join schedules s on a.schedule_id = s.id and s.status = 'ACTIVE' " +
            "    left join student_class sc on a.user_id = sc.student_id and sc.class_id = s.class_id " +
            "where date(s.learn_timestamp) = :date and a.user_id = :userId", nativeQuery = true)
    Attendance findAttendanceByDateAndUserId(@Param("date") LocalDate date,
                                             @Param("userId") Integer userId);
}