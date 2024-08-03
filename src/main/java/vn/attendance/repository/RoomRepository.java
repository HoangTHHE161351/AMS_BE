package vn.attendance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.attendance.model.Room;
import vn.attendance.service.room.response.RoomDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Integer> {

    @Query("select r from Room r where r.id = :id and r.status = 'ACTIVE'")
    Optional<Room> findById(Integer id);

    @Query("select r from Room r where r.roomName = :name and r.status = 'ACTIVE'")
    Optional<Room> findByRoomName(String name);

    @Query("select new vn.attendance.service.room.response.RoomDto (r.id, r.roomName, r.description,r.status,r.createdAt," +
            " u.username, r.modifiedAt,um.username) " +
            "from  Room r left join Users u on r.createdBy = u.id " +
            "left join Users um on r.modifiedBy = um.id " +
            "where (coalesce(:search, '') = '' or r.roomName like %:search%) " +
            "and (coalesce(:status, '') = '' or r.status = :status)" +
            "order by r.createdAt desc ")
    Page<RoomDto> findAllRoom(@Param("search") String search,
                              @Param("status") String status,
                              Pageable pageable);


    @Query("select r from Room r where r.status = 'ACTIVE'")
    List<Room> findAll();

    @Query("select count(r.id) from Room r")
    int countRoom();

    @Query("select r from Room r where r.status = 'ACTIVE'")
    List<Room> findAllRooms();

    @Query("select r from Room r where r.roomName = :roomName and r.status = 'ACTIVE' ")
    Room findRoomByRoomName(String roomName);

    @Query("select r from Room r " +
            " join Camera c on c.roomId = r.id " +
            " where r.status <> 'DELETED' " +
            " and c.status <> 'DELETED' " +
            " and c.channelId = :channelId ")
    Room getRoomByCameraChannel(Integer channelId);

    @Query("select new vn.attendance.service.room.response.RoomDto (r.id, r.roomName, r.description,r.status,r.createdAt," +
            " u.username, r.modifiedAt,um.username) " +
            "from  Room r left join Users u on r.createdBy = u.id " +
            "left join Users um on r.modifiedBy = um.id " +
            "where (:search is null or r.roomName like %:search%) order by r.createdAt desc ")
    List<RoomDto> findAll(@Param("search") String search);

    @Query("select r from Room r join Camera c on c.roomId = r.id and r.status = 'ACTIVE' where c.id = :cameraId ")
    Room findRoomByCameraId(Integer cameraId);

    @Query(value = "select * from rooms r where r.status = 'ACTIVE' and r.room_name = :roomName", nativeQuery = true)
    Room findByName(String roomName);


    @Query(value = "select count(*) from schedules s where s.room_id = :roomId and Date(s.learn_timestamp) >= Date(:now) and s.status = 'ACTIVE' ",nativeQuery = true)
    Integer countScheduleByRoom(Integer roomId, LocalDate now);

    @Query(value = "select count(*) from cameras c where c.status <> 'DELETED' and c.room_id = :id", nativeQuery = true)
    Integer countCameraByRoom(Integer id);
}