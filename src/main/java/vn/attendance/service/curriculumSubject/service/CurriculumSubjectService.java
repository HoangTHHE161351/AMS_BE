package vn.attendance.service.curriculumSubject.service;

import vn.attendance.exception.AmsException;
import vn.attendance.model.CurriculumSubject;
import vn.attendance.service.curriculumSubject.request.AddCurriculumSubjectRequest;
import vn.attendance.service.curriculumSubject.request.EditCurriculumSubjectRequest;

import java.util.List;

public interface CurriculumSubjectService {

    AddCurriculumSubjectRequest addCurriculumSubject(AddCurriculumSubjectRequest request)
            throws AmsException;

    EditCurriculumSubjectRequest editCurriculumSubject(Integer id, EditCurriculumSubjectRequest request)
            throws AmsException;

    List<CurriculumSubject> findAllByCurriculumId(Integer id);
    void deleteCurriculumSub(Integer id) throws AmsException;
}
