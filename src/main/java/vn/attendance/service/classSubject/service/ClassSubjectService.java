package vn.attendance.service.classSubject.service;

import org.springframework.data.domain.Page;
import vn.attendance.exception.AmsException;
import vn.attendance.model.ClassSubject;
import vn.attendance.service.classSubject.request.AddClassSubjectRequest;
import vn.attendance.service.classSubject.request.EditClassSubjectRequest;
import vn.attendance.service.classSubject.response.ClassSubjectDto;
import vn.attendance.service.classSubject.response.IClassDto;

import java.util.List;

public interface ClassSubjectService {
    ClassSubject findById(Integer id) throws AmsException;

    List<ClassSubject> findAllClassSubject();

    Page<ClassSubjectDto> findClassSubject(String search, Integer pageNo, Integer pageSize);
    EditClassSubjectRequest editClassSubject(int id, EditClassSubjectRequest editClassSubjectRequest) throws AmsException;
    AddClassSubjectRequest addClassSubject(AddClassSubjectRequest addClassSubjectRequest, Integer option) throws AmsException;
    void deleteClassSubject(Integer id) throws AmsException;


    List<IClassDto> getClassesSubject(Integer subjectId);

    List<AddClassSubjectRequest> importClassSubject(List<AddClassSubjectRequest> requestList) throws AmsException;
}