package vn.attendance.service.curriculumSubject.service;

import org.springframework.data.domain.Page;
import vn.attendance.exception.AmsException;
import vn.attendance.service.curriculumSubject.request.AddCurriculumSubjectRequest;
import vn.attendance.service.curriculumSubject.request.EditCurriculumSubjectRequest;
import vn.attendance.service.curriculumSubject.response.CurriculumSubjectDto;

import java.util.List;

public interface CurriculumSubjectService {

    AddCurriculumSubjectRequest addCurriculumSubject(AddCurriculumSubjectRequest request)
            throws AmsException;

    EditCurriculumSubjectRequest editCurriculumSubject(Integer id, EditCurriculumSubjectRequest request)
            throws AmsException;

    List<CurriculumSubjectDto> findAllByCurriculumId(Integer id) throws AmsException;

    void deleteCurriculumSub(Integer id) throws AmsException;

    Page<CurriculumSubjectDto> findCurriculumSubject(String search, Integer pageNo, Integer pageSize);
}
