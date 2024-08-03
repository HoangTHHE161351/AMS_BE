package vn.attendance.service.curriculum.service;

import org.springframework.data.domain.Page;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Curriculum;
import vn.attendance.service.curriculum.request.AddCurriculumRequest;
import vn.attendance.service.curriculum.request.AddStudentCurriculumRequest;
import vn.attendance.service.curriculum.request.AddSubjectCurriculumRequest;
import vn.attendance.service.curriculum.request.EditCurriculumRequest;
import vn.attendance.service.curriculum.response.CurriculumDto;
import vn.attendance.service.curriculum.response.ICurriculumDto;
import vn.attendance.service.curriculum.response.ISubjectDto;

import java.util.List;

public interface CurriculumService {

    Curriculum findById(Integer id) throws AmsException;

    Page<CurriculumDto> findCurriculum(String search, String status, Integer pageNo, Integer pageSize);

    List<Curriculum> findAllCurriculum();

    void deleteCurriculum(Integer id) throws AmsException;

    AddCurriculumRequest addCurriculum(AddCurriculumRequest request, Integer option) throws AmsException;
    List<AddCurriculumRequest> importCurriculum(List<AddCurriculumRequest> requests) throws AmsException;


    EditCurriculumRequest editCurriculum(EditCurriculumRequest editCurriculumRequest) throws AmsException;

    AddStudentCurriculumRequest addStudentCurriculum(AddStudentCurriculumRequest request, Integer option) throws AmsException;

    List<AddStudentCurriculumRequest> importStudentCurriculum(List<AddStudentCurriculumRequest> request) throws AmsException;

    List<AddSubjectCurriculumRequest> importSubjectCurriculum(List<AddSubjectCurriculumRequest> request) throws AmsException;

    AddSubjectCurriculumRequest addSubjectCurriculum(AddSubjectCurriculumRequest request, int option) throws AmsException;

    List<ISubjectDto> getAllSubjectsCurriculum(Integer curriculumId);

    List<ICurriculumDto> getAllCurriculumsStudent(Integer studentId);
}
