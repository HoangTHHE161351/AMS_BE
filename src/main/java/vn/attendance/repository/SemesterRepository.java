package vn.attendance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.attendance.model.Semester;
import vn.attendance.service.semester.response.SemesterDto;

import java.time.LocalDate;
import java.util.List;


@Repository

public interface SemesterRepository extends JpaRepository<Semester, Integer> {

    @Query("SELECT new vn.attendance.service.semester.response.SemesterDto(s.id, " +
            "s.semesterName, s.startTime, s.endTime, s.description, " +
            "s.status, s.createdAt, uc.username, s.modifiedAt, um.username) " +
            "FROM Semester s " +
            "LEFT JOIN Users uc ON s.createdBy = uc.id " +
            " LEFT JOIN Users um ON s.modifiedBy = um.id " +
            "WHERE (coalesce(:search, '') = '' OR s.semesterName LIKE CONCAT('%', :search, '%')) " +
            "and (coalesce(:status, '') = '' or s.status = :status)" +
            "ORDER BY s.createdAt DESC")
    Page<SemesterDto> searchSemester(@Param("search") String search,
                                     @Param("status") String status,
                                     Pageable pageable);

    @Query("select s from Semester s where s.status = 'ACTIVE'")
    List<Semester> findAllSemesters();

    @Query(value = "select * from semester s where " +
            "date(s.start_time) <= date(:date) and date(s.end_time) >= date(:date) and s.status = 'ACTIVE'", nativeQuery = true)
    Semester findSemesters(LocalDate date);

    @Query(value = "select * from semester s where " +
            "s.semester_name = :semesterName and s.status = 'ACTIVE'", nativeQuery = true)
    Semester findSemestersByName(String semesterName);

    @Query("select s from Semester s ")
    Page<Semester> findAllSemester(Pageable pageable);

    @Query("select s from Semester s")
    List<Semester> getAllSemesters();

}