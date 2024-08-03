package vn.attendance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.attendance.model.AccessLog;
import vn.attendance.service.accessLog.response.IAccessLogDTO;
import vn.attendance.service.accessLog.response.IAccessLogInRoomDto;
import vn.attendance.service.server.dto.IAccessLogDto;
import vn.attendance.service.server.dto.ILogDto;
import vn.attendance.service.server.service.dto.IImageDto;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface AccessLogRepository extends JpaRepository<AccessLog, Integer> {

    @Query(value = "WITH FirstLastTimes AS ( " +
            "    SELECT " +
            "        u.user_name AS userName, " +
            "        rc.id AS roomId, " +
            "        rc.room_name AS roomName, " +
            "        CONCAT(u.last_name, ' ', u.first_name) AS name, " +
            "        DATE(a.time_stamp) AS dateTime, " +
            "        MIN(CASE WHEN c.check_type = 'OUT' THEN a.time_stamp ELSE NULL END) AS firstTimeStamp, " +
            "        MAX(CASE WHEN c.check_type = 'IN' THEN a.time_stamp ELSE NULL END) AS lastTimeStamp " +
            "    FROM access_log a " +
            "    JOIN users u ON a.user_id = u.id " +
            "    JOIN cameras c ON a.camera_id = c.id AND c.camera_type = 'LCD' " +
            "    JOIN rooms rc ON rc.id = c.room_id AND c.status = 'ACTIVE' " +
            "    WHERE (COALESCE(:search, '') = '' OR rc.room_name LIKE CONCAT('%', :search, '%') " +
            "           OR u.first_name LIKE CONCAT('%', :search, '%') " +
            "           OR u.last_name LIKE CONCAT('%', :search, '%') " +
            "           OR u.user_name LIKE CONCAT('%', :search, '%')) " +
            "      AND (COALESCE(:roomId, '') = '' OR rc.id = :roomId) " +
            "      AND (COALESCE(:date1, '') = '' OR DATE(a.time_stamp) = DATE(:date1)) " +
            "    GROUP BY u.user_name, rc.id, rc.room_name, CONCAT(u.last_name, ' ', u.first_name), DATE(a.time_stamp) " +
            ") " +
            "SELECT " +
            "    fl.roomId AS roomId, " +
            "    fl.roomName AS roomName, " +
            "    fl.userName AS userName, " +
            "    fl.name AS name, " +
            "    fl.dateTime AS dateTime, " +
            "    (SELECT TIME(a.time_stamp) " +
            "     FROM access_log a " +
            "     JOIN cameras c ON a.camera_id = c.id AND c.camera_type = 'LCD' " +
            "     WHERE a.user_id = u.id AND c.room_id = fl.roomId AND a.time_stamp = fl.firstTimeStamp AND c.check_type = 'OUT' " +
            "     LIMIT 1) AS checkIn, " +
            "    (SELECT TIME(a.time_stamp) " +
            "     FROM access_log a " +
            "     JOIN cameras c ON a.camera_id = c.id AND c.camera_type = 'LCD' " +
            "     WHERE a.user_id = u.id AND c.room_id = fl.roomId AND a.time_stamp = fl.lastTimeStamp AND c.check_type = 'IN' " +
            "     LIMIT 1) AS checkOut " +
            "FROM FirstLastTimes fl " +
            "JOIN users u ON u.user_name = fl.userName " +
            "ORDER BY fl.roomId, fl.userName",
            countQuery = "WITH FirstLastTimes AS ( " +
                    "    SELECT " +
                    "        u.user_name AS userName, " +
                    "        rc.id AS roomId, " +
                    "        rc.room_name AS roomName, " +
                    "        CONCAT(u.last_name, ' ', u.first_name) AS name, " +
                    "        DATE(a.time_stamp) AS dateTime, " +
                    "        MIN(CASE WHEN c.check_type = 'OUT' THEN a.time_stamp ELSE NULL END) AS firstTimeStamp, " +
                    "        MAX(CASE WHEN c.check_type = 'IN' THEN a.time_stamp ELSE NULL END) AS lastTimeStamp " +
                    "    FROM access_log a " +
                    "    JOIN users u ON a.user_id = u.id " +
                    "    JOIN cameras c ON a.camera_id = c.id AND c.camera_type = 'LCD' " +
                    "    JOIN rooms rc ON rc.id = c.room_id AND c.status = 'ACTIVE' " +
                    "    WHERE (COALESCE(:search, '') = '' OR rc.room_name LIKE CONCAT('%', :search, '%') " +
                    "           OR u.first_name LIKE CONCAT('%', :search, '%') " +
                    "           OR u.last_name LIKE CONCAT('%', :search, '%') " +
                    "           OR u.user_name LIKE CONCAT('%', :search, '%')) " +
                    "      AND (COALESCE(:roomId, '') = '' OR rc.id = :roomId) " +
                    "      AND (COALESCE(:date1, '') = '' OR DATE(a.time_stamp) = DATE(:date1)) " +
                    "    GROUP BY u.user_name, rc.id, rc.room_name, CONCAT(u.last_name, ' ', u.first_name), DATE(a.time_stamp) " +
                    ") " +
                    "SELECT " +
                    "    fl.roomId AS roomId, " +
                    "    fl.roomName AS roomName, " +
                    "    fl.userName AS userName, " +
                    "    fl.name AS name, " +
                    "    fl.dateTime AS dateTime, " +
                    "    (SELECT TIME(a.time_stamp) " +
                    "     FROM access_log a " +
                    "     JOIN cameras c ON a.camera_id = c.id AND c.camera_type = 'LCD' " +
                    "     WHERE a.user_id = u.id AND c.room_id = fl.roomId AND a.time_stamp = fl.firstTimeStamp AND c.check_type = 'OUT' " +
                    "     LIMIT 1) AS checkIn, " +
                    "    (SELECT TIME(a.time_stamp) " +
                    "     FROM access_log a " +
                    "     JOIN cameras c ON a.camera_id = c.id AND c.camera_type = 'LCD' " +
                    "     WHERE a.user_id = u.id AND c.room_id = fl.roomId AND a.time_stamp = fl.lastTimeStamp AND c.check_type = 'IN' " +
                    "     LIMIT 1) AS checkOut " +
                    "FROM FirstLastTimes fl " +
                    "JOIN users u ON u.user_name = fl.userName " +
                    "ORDER BY fl.roomId, fl.userName", nativeQuery = true)
    Page<IAccessLogInRoomDto> historyLogOfRoom(@Param("search") String search,
                                               @Param("roomId") Integer roomId,
                                               @Param("date1") LocalDate date1,
                                               Pageable pageable);

    @Query(value = "select a.id as id, a.face_image as faceImage, u.user_name as userName," +
            " concat(u.last_name, ' ', u.first_name) as name, rm.room_name as roomName, " +
            "       concat(dayname(date(a.time_stamp)),' (', DATE_FORMAT(a.time_stamp, '%d/%m/%Y'),')') as day,  " +
            "       time(a.time_stamp) as checkTime, " +
            "       case c.check_type when 'IN' then 'Out' else 'In' end as checkType " +
            "from access_log a " +
            "         inner join users u on a.user_id = u.id " +
            "         inner join roles r on u.role_id = r.id and r.status = 'ACTIVE' " +
            "         inner join cameras c on a.camera_id = c.id and c.camera_type = 'LCD' " +
            "         inner join rooms rm on c.room_id = rm.id and rm.status = 'ACTIVE' " +
            "where (:roomId is null or rm.Id = :roomId) " +
            "and (coalesce(:userName,'') = ''  or u.user_name = :userName) " +
            "and (coalesce(:date1,'') = ''  or date(a.time_stamp) = date(:date1))", nativeQuery = true)
    List<IAccessLogDTO> findByRoomIdAndUserId(@Param("roomId") Integer roomId,
                                              @Param("userName") String userName,
                                              @Param("date1") LocalDate date1);

    @Query(" select a.id as id," +
            " a.timeStamp as time, " +
            " c.id as cameraId," +
            " c.cameraType as cameraType," +
            " c.checkType as checkType " +
            " from AccessLog a " +
            " join Camera c on a.cameraId = c.id " +
            " where a.id = :logId ")
    ILogDto getLogById(Integer logId);


    @Modifying
    @Transactional
    @Query("update AccessLog a set a.userId = :userId, a.similarity = :similarity, a.type = :type where a.id = :id")
    void updateLog(@Param("id") Integer id, @Param("userId") Integer userId, @Param("similarity") Integer similarity, @Param("type") Integer type);

    @Query(value =
            "SELECT a.id AS logId, a.face_image AS faceImage, a.time_stamp AS timeStamp, " +
                    "    CASE " +
                    "        WHEN a.type = 0 THEN 'Unidentified' " +
                    "        WHEN a.type = 1 THEN 'Identified' " +
                    "        WHEN a.type = 2 THEN 'Unauthorized' " +
                    "        ELSE 'Unknown' " +
                    "    END AS type, " +
                    "    a.similarity AS similarity, a.user_id AS userId, " +
                    "    CONCAT(u.last_name, ' ', u.first_name) AS fullName, " +
                    "    r.role_name AS roleName, c.id AS cameraId, c.ip_tcip AS cameraIp, " +
                    "c.check_type AS checkType, ro.id AS roomId, ro.room_name AS roomName " +
                    "FROM access_log a " +
                    "LEFT JOIN users u ON a.user_id = u.id " +
                    "LEFT JOIN roles r ON u.role_id = r.id " +
                    "JOIN cameras c ON a.camera_id = c.id " +
                    "JOIN rooms ro ON c.room_id = ro.id " +
                    "WHERE " +
                    "    ( a.user_id is null or LOWER(CONCAT(u.last_name, ' ', u.first_name)) LIKE LOWER(CONCAT('%', :search, '%')) OR :search IS NULL) " +
                    "    AND (a.type = :type OR :type IS NULL) " +
                    "    AND (ro.id = :roomId OR :roomId IS NULL) " +
                    "    AND (DATE(a.time_stamp) = DATE(:date) OR :date IS NULL) " +
                    "    AND (a.type <> :notType OR :notType IS NULL) " +
                    "ORDER BY a.time_stamp DESC",
            countQuery = "SELECT a.id AS logId, a.face_image AS faceImage, a.time_stamp AS timeStamp, " +
                    "    CASE " +
                    "        WHEN a.type = 0 THEN 'Unidentified' " +
                    "        WHEN a.type = 1 THEN 'Identified' " +
                    "        WHEN a.type = 2 THEN 'Unauthorized' " +
                    "        ELSE 'Unknown' " +
                    "    END AS type, " +
                    "    a.similarity AS similarity, a.user_id AS userId, " +
                    "    CONCAT(u.last_name, ' ', u.first_name) AS fullName, " +
                    "    r.role_name AS roleName, c.id AS cameraId, c.ip_tcip AS cameraIp, " +
                    "c.check_type AS checkType, ro.id AS roomId, ro.room_name AS roomName " +
                    "FROM access_log a " +
                    "LEFT JOIN users u ON a.user_id = u.id " +
                    "LEFT JOIN roles r ON u.role_id = r.id " +
                    "JOIN cameras c ON a.camera_id = c.id " +
                    "JOIN rooms ro ON c.room_id = ro.id " +
                    "WHERE " +
                    "    ( a.user_id is null or LOWER(CONCAT(u.last_name, ' ', u.first_name)) LIKE LOWER(CONCAT('%', :search, '%')) OR :search IS NULL) " +
                    "    AND (a.type = :type OR :type IS NULL) " +
                    "    AND (ro.id = :roomId OR :roomId IS NULL) " +
                    "    AND (DATE(a.time_stamp) = DATE(:date) OR :date IS NULL) " +
                    "    AND (a.type <> :notType OR :notType IS NULL)", nativeQuery = true)
    Page<IAccessLogDto> findAllAccessLog(
            @Param("search") String search,
            @Param("type") Integer type,
            @Param("roomId") Integer roomId,
            @Param("date") LocalDate date,
            @Param("notType") Integer notType,
            Pageable pageable);

    @Query(value =
            "SELECT a.id AS logId, a.face_image AS faceImage, a.time_stamp AS timeStamp, " +
                    "    CASE " +
                    "        WHEN a.type = 0 THEN 'Unidentified' " +
                    "        WHEN a.type = 1 THEN 'Identified' " +
                    "        WHEN a.type = 2 THEN 'Unauthorized' " +
                    "        ELSE 'Detecting' " +
                    "    END AS type, " +
                    "    a.similarity AS similarity, a.user_id AS userId, " +
                    "    CONCAT(u.last_name, ' ', u.first_name) AS fullName, " +
                    "    r.role_name AS roleName, c.id AS cameraId, c.ip_tcip AS cameraIp, " +
                    "    c.check_type AS checkType, ro.id AS roomId, ro.room_name AS roomName " +
                    "FROM access_log a " +
                    "LEFT JOIN users u ON a.user_id = u.id " +
                    "LEFT JOIN roles r ON u.role_id = r.id " +
                    "JOIN cameras c ON a.camera_id = c.id " +
                    "JOIN rooms ro ON c.room_id = ro.id " +
                    "WHERE " +
                    "    (ro.id = :roomId OR :roomId IS NULL) " +
                    "    AND (DATE(a.time_stamp) = DATE(:date)) " +
                    "    AND (TIME(a.time_stamp) BETWEEN TIME(:start) AND TIME(:end)) " +
                    "    AND (a.type <> 1) " +
                    "ORDER BY a.time_stamp DESC",
            countQuery = "SELECT COUNT(a.id) " +
                    "FROM access_log a " +
                    "LEFT JOIN users u ON a.user_id = u.id " +
                    "LEFT JOIN roles r ON u.role_id = r.id " +
                    "JOIN cameras c ON a.camera_id = c.id " +
                    "JOIN rooms ro ON c.room_id = ro.id " +
                    "WHERE " +
                    "    (ro.id = :roomId OR :roomId IS NULL) " +
                    "    AND (DATE(a.time_stamp) = DATE(:date)) " +
                    "    AND (TIME(a.time_stamp) BETWEEN TIME(:start) AND TIME(:end)) " +
                    "    AND (a.type <> 1)", nativeQuery = true)
    Page<IAccessLogDto> findStrangerBySchedule(@Param("date") LocalDate date,
                                               @Param("start") LocalTime start,
                                               @Param("end") LocalTime end,
                                               @Param("roomId") Integer roomId,
                                               Pageable pageable);

    @Query(value =
            "SELECT a.id AS id, a.face_image AS pic, a.time_stamp AS time " +
                    " FROM access_log a " +
                    " Join cameras c on c.id = a.camera_id " +
                    " WHERE a.time_stamp >= :time and c.camera_type = 'LCD' and c.check_type= 'ADD' ",
            nativeQuery = true)
    List<IImageDto> getImage(@Param("time") LocalDateTime time);
}