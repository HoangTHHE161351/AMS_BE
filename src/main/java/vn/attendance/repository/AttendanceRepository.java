package vn.attendance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.attendance.model.Attendance;
import vn.attendance.service.attendance.request.AttendanceDto;
import vn.attendance.service.attendance.request.IAttendanceDto;
import vn.attendance.service.attendance.response.AttendanceDTO;
import vn.attendance.service.attendance.response.IAttendanceDTO;
import vn.attendance.service.attendance.response.IListAttendenceResponse;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

    @Query(value = "select a.id as id, a.user_id as userId, DAYNAME(s.learn_timestamp) as day, date(s.learn_timestamp) as learnDate, " +
            " concat(u.last_name, ' ', u.first_name) as userName, tc.user_name as teacherCode, c.class_name as className, " +
            " r.room_name as roomName, t.description as slot, sb.code as subjectCode, a.description as description, " +
            " a.status as status, a.created_at as createAt, uc.user_name as createBy, a.modified_at as modifiedAt, um.user_name as modifiedBy " +
            "from attendance a " +
            "join users u on a.user_id = u.id and u.status = 'ACTIVE' " +
            "join schedules s on a.schedule_id = s.id " +
            "join semester sm on s.semester_id = sm.id and sm.status = 'ACTIVE' " +
            "join time_slot t on s.time_slot_id = t.id and t.status = 'ACTIVE' " +
            "join subjects sb on s.subject_id = sb.id and sb.status = 'ACTIVE' " +
            "join class_room c on c.id = s.class_id and c.status = 'ACTIVE' " +
            "join student_class sc on a.user_id = sc.student_id and c.id = sc.class_id " +
            "join rooms r on r.id = s.room_id and r.status = 'ACTIVE' " +
            "left join Users uc on a.created_by = uc.id and u.status = 'ACTIVE' " +
            "left join Users um on a.modified_by = um.id and u.status = 'ACTIVE' " +
            "left join Users tc on s.user_id = tc.id and tc.status = 'ACTIVE' " +
            "where (:semesterId is null or sm.id = :semesterId) " +
            "  and (:userId is null or a.user_id = :userId) and c.id = :classId ", nativeQuery = true)
    List<IAttendanceDto> attendanceReportStudent(@Param("semesterId") Integer semesterId,
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
            "where (:semesterId is null or sm.id = :semesterId) " +
            "  and (:userId is null or a.user_id = :userId) and c.id = :classId ", nativeQuery = true)
    List<IAttendanceDto> attendanceReportTeacher(@Param("semesterId") Integer semesterId,
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
            "    join ClassRoom c on c.id = s.classId and c.status = 'ACTIVE' " +
            "    join ClassSubject cs on cs.classId = s.classId and cs.subjectId = s.subjectId " +
            "    join StudentClass sc on a.userId = sc.studentId and sc.classId = s.classId " +
            "where s.id = :scheduleId ")
    List<Attendance> findAttendanceByScheduleId(@Param("scheduleId") Integer scheduleId);

    @Query(value = "select a.* " +
            "from attendance a " +
            "    join users u on u.id = a.user_id and u.status = 'ACTIVE' " +
            "    join schedules s on a.schedule_id = s.id and s.status = 'ACTIVE' " +
            "    left join student_class sc on a.user_id = sc.student_id and sc.class_id = s.class_id " +
            "where s.time_slot_id = :timeSlotId and date(s.learn_timestamp) = :date and a.user_id = :userId and s.room_id = :roomId ", nativeQuery = true)
    Attendance findAttendanceByDateAndUserId(@Param("date") LocalDate date,
                                             @Param("userId") Integer userId,
                                             @Param("roomId") Integer roomId,
                                             Integer timeSlotId);

    @Query(value = "SELECT a.id AS id, u.user_name AS userName, CONCAT(u.last_name, ' ', u.first_name) AS name, " +
            "c.class_name AS className, sb.code AS subjectCode, a.description as description, t.slot_name AS slot, " +
            "a.status AS status, a.created_at AS createdAt, uc.user_name AS createdBy, " +
            "a.modified_at AS modifiedAt, um.user_name AS modifiedBy, fa.image AS avatar " +
            "FROM attendance a " +
            "JOIN users u ON u.id = a.user_id AND u.status = 'ACTIVE' " +
            "JOIN schedules s ON a.schedule_id = s.id AND s.status = 'ACTIVE' " +
            "JOIN semester sm ON s.semester_id = sm.id AND sm.status = 'ACTIVE' " +
            "JOIN class_room c ON c.id = s.class_id AND c.status = 'ACTIVE' " +
            "JOIN subjects sb ON s.subject_id = sb.id AND sb.status = 'ACTIVE' " +
            "JOIN time_slot t ON s.time_slot_id = t.id AND t.status = 'ACTIVE' " +
            "JOIN student_class sc ON a.user_id = sc.student_id AND sc.class_id = s.class_id " +
            "LEFT JOIN users uc ON a.created_by = uc.id AND uc.status = 'ACTIVE' " +
            "LEFT JOIN users um ON a.modified_by = um.id AND um.status = 'ACTIVE' " +
            "LEFT JOIN facial fa ON u.id = fa.userId AND fa.status = 'ACTIVE' " +
            "WHERE s.id = :scheduleId AND DATE(s.learn_timestamp) = DATE(NOW())", nativeQuery = true)
    List<IAttendanceDTO> findAttendanceByClass(@Param("scheduleId") Integer scheduleId);


    @Query(value = "select atten.id     as attendenceId,\n" +
            "       u.first_name  as  firstName,\n" +
            "       u.last_name  as lastName,\n" +
            "       r.role_name  as roleName,\n" +
            "       u.email      as email,\n" +
            "       u.phone      as phoneNum,\n" +
            "       u.gender     as gender,\n" +
            "       DATE_FORMAT(u.dob, '%d/%m/%Y')        as dob,\n" +
            "       u.avata as image,\n" +
            "       atten.status as status\n" +
            "from attendance atten\n" +
            "         inner join schedules s on atten.schedule_id = s.id and s.status = 'ACTIVE'\n" +
            "         inner join users u on atten.user_id = u.id and u.status = 'ACTIVE'\n" +
            "         inner join roles r on u.role_id = r.id and r.status = 'ACTIVE'\n" +
            "where (:classId is null or s.class_id = :classId) \n" +
            "  and date(s.learn_timestamp) = :date1\n" +
            "order by u.first_name , u.last_name", countQuery = "select atten.id     as attendenceId,\n" +
            "       u.first_name  as  firstName,\n" +
            "       u.last_name  as lastName,\n" +
            "       r.role_name  as roleName,\n" +
            "       u.email      as email,\n" +
            "       atten.status as status\n" +
            "from attendance atten\n" +
            "         inner join schedules s on atten.schedule_id = s.id and s.status = 'ACTIVE'\n" +
            "         inner join users u on atten.user_id = u.id and u.status = 'ACTIVE'\n" +
            "         inner join roles r on u.role_id = r.id and r.status = 'ACTIVE'\n" +
            "where (:classId is null or s.class_id = :classId)\n" +
            "  and date(s.learn_timestamp) = date(:date1)", nativeQuery = true)
    Page<IListAttendenceResponse> findAttendanceByClassIdAndDate(Integer classId,
                                                                 LocalDate date1,
                                                                 Pageable pageable);

    @Query(value = "SELECT a.id AS id, " +
            "u.id AS userId, " +
            "CONCAT(DAYNAME(s.learn_timestamp), ' (', DATE_FORMAT(s.learn_timestamp, '%d/%m/%Y'), ')') AS day, " +
            "DATE(s.learn_timestamp) AS learnDate, " +
            "CONCAT(u.last_name, ' ', u.first_name) AS name, " +
            "u.user_name as teacherCode, " +
            "c.class_name as className, " +
            "r.room_name as roomName, " +
            "sb.code AS subjectCode, " +
            "t.slot_name AS slot, " +
            "a.description AS description, " +
            "a.status AS status, " +
            "a.created_at AS createdAt, uc.user_name AS createdBy, a.modified_at AS modifiedAt, um.user_name AS modifiedBy " +
            "FROM attendance a " +
            "JOIN users u ON a.user_id = u.id AND u.status = 'ACTIVE' " +
            "JOIN schedules s ON a.schedule_id = s.id " +
            "JOIN class_room c ON c.id = s.class_id AND c.status = 'ACTIVE' " +
            "JOIN semester sm ON s.semester_id = sm.id AND sm.status = 'ACTIVE' " +
            "JOIN subjects sb ON s.subject_id = sb.id AND sb.status = 'ACTIVE' " +
            "join rooms r on r.id = s.room_id and r.status = 'ACTIVE' " +
            "JOIN time_slot t ON s.time_slot_id = t.id AND t.status = 'ACTIVE' " +
            "LEFT JOIN users uc ON a.created_by = uc.id AND uc.status = 'ACTIVE' " +
            "LEFT JOIN users um ON a.modified_by = um.id AND um.status = 'ACTIVE' " +
            "WHERE (:semesterId IS NULL OR sm.id = :semesterId) " +
            "AND (:userId IS NULL OR a.user_id = :userId) " +
            "AND (:classId IS NULL OR s.class_id = :classId) " +
            "ORDER BY a.id", nativeQuery = true)
    List<IAttendanceDto> findAttendance(@Param("semesterId") Integer semesterId,
                                        @Param("userId") Integer userId,
                                        @Param("classId") Integer classId);

}