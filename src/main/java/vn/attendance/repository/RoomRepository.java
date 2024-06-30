package vn.attendance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.attendance.model.Room;
import vn.attendance.service.room.response.RoomDto;

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
            "where (:search is null or r.roomName like %:search%) order by r.createdAt desc ")
    Page<RoomDto> findAllRoom(@Param("search") String search, Pageable pageable);


    @Query("select r from Room r where r.status = 'ACTIVE'")
    List<Room> findAll();

    @Query("select count(r.id) from Room r")
    int countRoom();

    @Query("select r from Room r where r.status = 'ACTIVE'")
    List<Room> findAllRooms();

    @Query("select r from Room r where r.roomName = :roomName and r.status = 'ACTIVE' ")
    Room findRoomByRoomName(String roomName);

    @Query("select new vn.attendance.service.room.response.RoomDto (r.id, r.roomName, r.description,r.status,r.createdAt," +
            " u.username, r.modifiedAt,um.username) " +
            "from  Room r left join Users u on r.createdBy = u.id " +
            "left join Users um on r.modifiedBy = um.id " +
            "where (:search is null or r.roomName like %:search%) order by r.createdAt desc ")
    List<RoomDto> findAll(@Param("search") String search);
}