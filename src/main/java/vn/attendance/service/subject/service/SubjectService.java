package vn.attendance.service.subject.service;

import org.springframework.data.domain.Page;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Semester;
import vn.attendance.model.Subject;
import vn.attendance.service.subject.request.AddSubjectRequest;
import vn.attendance.service.subject.request.EditSubjectRequest;
import vn.attendance.service.subject.response.SubjectDto;
import vn.attendance.service.teacher.service.response.TeacherDto;

import java.util.List;
import java.util.Optional;

public interface SubjectService {
    Page<Subject> findAllSubject(int page, int size);

    AddSubjectRequest addSubject(AddSubjectRequest request) throws AmsException;
    Optional<Subject> findById(Integer id);

    Subject save(Subject subject);

    Optional<Subject> updateSubject(Integer id, EditSubjectRequest editSubjectRequest);
   Page<SubjectDto> searchSubject(String search, int page, int size);

    List<Subject> findAllSubject();
    public Optional<Subject> deleteSubject(Integer id) throws AmsException;
}
