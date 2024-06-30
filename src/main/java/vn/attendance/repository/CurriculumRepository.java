package vn.attendance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import vn.attendance.model.Curriculum;
import vn.attendance.service.curriculum.response.CurriculumDto;

import java.util.List;
import java.util.Optional;

public interface CurriculumRepository extends JpaRepository<Curriculum, Integer> {

    @Query("select c from Curriculum c where c.id = :id")
    Optional<Curriculum> findById(Integer id);

    @Query("select c from Curriculum c ")
    List<Curriculum> findAllCurriculum();

    @Query("select new vn.attendance.service.curriculum.response.CurriculumDto(c.id, c.curriculumName, " +
            "c.description, c.status, c.createdAt, uc.username, c.modifiedAt, um.username) " +
            "from Curriculum c " +
            " LEFT JOIN Users uc ON c.createdBy = uc.id " +
            " LEFT JOIN Users um ON c.modifiedBy = um.id " +
            "where (:search is null or c.curriculumName like %:search% ) order by c.createdAt desc")
    Page<CurriculumDto> findCurriculum(String search, Pageable pageable);

    //    delete set status = DELETE where id = :id
    @Query("update Curriculum c set c.status = 'DELETED' where c.id = :id")
    @Modifying
    @Transactional
    void deleteCurriculum(Integer id);

    @Query("select c from Curriculum c where c.curriculumName = :curriculumName")
    Curriculum findCurriculumByName(String curriculumName);
}