package vn.attendance.service.curriculum.service;

import org.springframework.data.domain.Page;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Curriculum;
import vn.attendance.service.curriculum.request.AddCurriculumRequest;
import vn.attendance.service.curriculum.request.EditCurriculumRequest;
import vn.attendance.service.curriculum.response.CurriculumDto;

import java.util.List;

public interface CurriculumService {

    Curriculum findById(Integer id) throws AmsException;

    Page<CurriculumDto> findCurriculum(String search, Integer pageNo, Integer pageSize);

    List<Curriculum> findAllCurriculum();

    void deleteCurriculum(Integer id) throws AmsException;

    void addCurriculum(AddCurriculumRequest addCurriculumRequest) throws AmsException;

    void editCurriculum(int id, EditCurriculumRequest editCurriculumRequest) throws AmsException;
}
