package vn.attendance.service.subject.service;

import org.springframework.data.domain.Page;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Subject;
import vn.attendance.service.subject.request.AddSubjectRequest;
import vn.attendance.service.subject.request.EditSubjectRequest;
import vn.attendance.service.subject.response.ISubjectDto;
import vn.attendance.service.subject.response.SubjectDto;

import java.util.List;
import java.util.Optional;

public interface SubjectService {

    Optional<Subject> findById(Integer id);

    Subject save(Subject subject);

    Subject updateSubject(EditSubjectRequest editSubjectRequest) throws AmsException;

    Page<SubjectDto> searchSubject(String search, String status, int page, int size);

    List<ISubjectDto> getDropdownSubject();

    Subject deleteSubject(Integer id) throws AmsException;

    Page<SubjectDto> findSubject(String search, String status, int page, int size);

    AddSubjectRequest addSubject(AddSubjectRequest request, Integer options) throws AmsException;

    List<AddSubjectRequest> importSubject(List<AddSubjectRequest> requests) throws AmsException;
}
