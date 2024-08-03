package vn.attendance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.attendance.model.ClassRoom;
import vn.attendance.service.classRoom.response.ClassRoomDto;
import vn.attendance.service.classRoom.response.IClassDto;

import java.util.List;
import java.util.Optional;

public interface ClassRoomRepository extends JpaRepository<ClassRoom, Integer> {

    @Query("select c from ClassRoom c where c.id = :id")
    Optional<ClassRoom> findById (Integer id);

    @Query("select new vn.attendance.service.classRoom.response.ClassRoomDto (c.id, c.className, c.description," +
            "c.status, c.createdAt, u.username, c.modifiedAt, um.username) " +
            "from  ClassRoom c left join Users u on c.createdBy = u.id " +
            "left join Users um on c.modifiedBy = um.id " +
            "where (:search is null or c.className like %:search%) " +
            "and (:status is null or c.status = :status)" +
            "order by c.createdAt desc ")
    Page<ClassRoomDto> findAllClasses (@Param("search") String search,
                                       @Param("status") String status,
                                       Pageable pageable);

    @Query(value = "select * from class_room c where c.status = 'ACTIVE' ", nativeQuery = true)
    List<ClassRoom> findAllClassRoom();

    @Query(value = "select * from class_room c where c.status = 'ACTIVE'", nativeQuery = true)
    List<ClassRoom> findAllClass();

    @Query("select new vn.attendance.service.classRoom.response.ClassRoomDto (c.id, c.className, c.description," +
            "c.status, c.createdAt, u.username, c.modifiedAt, um.username) " +
            "from  ClassRoom c left join Users u on c.createdBy = u.id " +
            "left join Users um on c.modifiedBy = um.id " +
            "where (:search is null or c.className like %:search%) order by c.createdAt desc ")
    List<ClassRoomDto> findAllToExport(@Param("search") String search);

    @Query(value = "select c.id as id, c.class_name as className " +
            "from  class_room c " +
            "join class_subject cb on cb.class_id = c.id " +
            "where (:subjectId is null or cb.subject_id = :subjectId ) " +
            "and c.status = 'ACTIVE' " +
            "and cb.status = 'ACTIVE' ", nativeQuery = true)
    List<IClassDto> findAllClasses(Integer subjectId);

    @Query(value = "select * from class_room c where c.class_name = :className and c.status='ACTIVE' ", nativeQuery = true)
    ClassRoom findClassByName(String className);
}