package vn.attendance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.attendance.model.ClassRoom;
import vn.attendance.service.classRoom.response.ClassRoomDto;

import java.util.List;
import java.util.Optional;

public interface ClassRoomRepository extends JpaRepository<ClassRoom, Integer> {

    @Query("select c from ClassRoom c where c.id = :id")
    Optional<ClassRoom> findById (Integer id);

    @Query("select new vn.attendance.service.classRoom.response.ClassRoomDto (c.id, c.className, c.description," +
            "c.status, c.createdAt, u.username, c.modifiedAt, um.username) " +
            "from  ClassRoom c left join Users u on c.createdBy = u.id " +
            "left join Users um on c.modifiedBy = um.id " +
            "where (:search is null or c.className like %:search%) order by c.createdAt desc ")
    Page<ClassRoomDto> findAllClasses (@Param("search") String search, Pageable pageable);

    @Query(value = "select * from class_room c where c.status = 'ACTIVE' ", nativeQuery = true)
    List<ClassRoom> findAllClassRoom();

    @Query(value = "select * from class_room c ", nativeQuery = true)
    List<ClassRoom> findAllClass();
}