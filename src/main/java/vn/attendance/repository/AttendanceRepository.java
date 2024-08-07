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
import vn.attendance.service.classRoom.response.IClassDto;
import vn.attendance.service.schedule.response.ExportScheduleDto;

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
            "where a.scheduleId = :scheduleId ")
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


    @Query(value = "select atten.id     as attendenceId, " +
            "       u.first_name  as  firstName, " +
            "       u.last_name  as lastName, " +
            "       r.role_name  as roleName, " +
            "       u.email      as email, " +
            "       u.phone      as phoneNum, " +
            "       u.gender     as gender, " +
            "       DATE_FORMAT(u.dob, '%d/%m/%Y')        as dob, " +
            "       u.avata as image, " +
            "       atten.status as status " +
            "from attendance atten " +
            "         inner join schedules s on atten.schedule_id = s.id and s.status = 'ACTIVE' " +
            "         inner join users u on atten.user_id = u.id and u.status = 'ACTIVE' " +
            "         inner join roles r on u.role_id = r.id and r.status = 'ACTIVE' " +
            "where (:classId is null or s.class_id = :classId)  " +
            "  and date(s.learn_timestamp) = :date1 " +
            "order by u.first_name , u.last_name",
            countQuery = "select atten.id     as attendenceId, " +
            "       u.first_name  as  firstName, " +
            "       u.last_name  as lastName, " +
            "       r.role_name  as roleName, " +
            "       u.email      as email, " +
            "       atten.status as status " +
            "from attendance atten " +
            "         inner join schedules s on atten.schedule_id = s.id and s.status = 'ACTIVE' " +
            "         inner join users u on atten.user_id = u.id and u.status = 'ACTIVE' " +
            "         inner join roles r on u.role_id = r.id and r.status = 'ACTIVE' " +
            "where (:classId is null or s.class_id = :classId) " +
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

    @Query("select new vn.attendance.service.schedule.response.ExportScheduleDto(a.id, " +
            "       dayname(s.learnTimestamp), " +
            "       rc.roomName, " +
            "       c.className, " +
            "       sb.code, " +
            "       tc.username, " +
            "       t.slotName) " +
            "from Attendance a " +
            "        inner join Users u on u.id = a.userId and u.status = 'ACTIVE' " +
            "        inner join Schedule s on a.scheduleId = s.id and s.status = 'ACTIVE' " +
            "        inner join Users tc on tc.id = s.userId and tc.status = 'ACTIVE' " +
            "        inner join Semester sm on s.semester = sm.id and sm.status = 'ACTIVE' " +
            "        inner join ClassRoom c on c.id = s.classId and c.status = 'ACTIVE' " +
            "        inner join Room rc on rc.id = s.roomId and rc.status = 'ACTIVE' " +
            "        inner join Subject sb on s.subjectId = sb.id and sb.status = 'ACTIVE' " +
            "        inner join TimeSlot t on s.timeSlotId = t.id and t.status = 'ACTIVE' " +
            "        inner join StudentClass sc on a.userId = sc.studentId and sc.classId = s.classId " +
            "where (:userId is null or a.userId = :userId) and sm.id = :semesterId " +
            "order by  s.learnTimestamp")
    List<ExportScheduleDto> exportSchedules(Integer userId,
                                            Integer semesterId);


    @Query(value = "select DISTINCT sr.id as id, sr.class_name as className from class_room sr " +
            " inner join schedules s on s.status = 'ACTIVE' and s.class_id = sr.id" +
            " inner join Attendance a on a.schedule_id = s.id " +
            " where s.semester_id = :semesterId and a.user_id = :userId ", nativeQuery = true)
    List<IClassDto> attendanceClassDropdown(Integer semesterId, Integer userId);
}