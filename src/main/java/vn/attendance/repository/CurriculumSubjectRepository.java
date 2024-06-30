package vn.attendance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.attendance.model.CurriculumSubject;

import java.util.List;

public interface CurriculumSubjectRepository  extends JpaRepository<CurriculumSubject, Integer> {
    @Query("SELECT cs FROM CurriculumSubject cs " +
            "order by cs.createdAt desc")
    Page<CurriculumSubject> findAllCurriculumSub(Pageable pageable);

    @Query("SELECT cs FROM CurriculumSubject cs WHERE (:id is null or cs.curriculumId = :id) order by cs.createdAt desc")
    List<CurriculumSubject> findAllByCurriculumId(Integer id);
    CurriculumSubject findByCurriculumIdAndSubjectIdAndSemesterId(Integer curriculumId, Integer subjectId, Integer semesterId);
//    @Transactional
//    @Query(value = "UPDATE curriculum_sub SET status='DELETED' WHERE id = :id", nativeQuery = true)
//    void changeStatusCurriculumSub(Integer id);

}
