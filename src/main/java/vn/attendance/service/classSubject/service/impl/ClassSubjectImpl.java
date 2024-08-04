package vn.attendance.service.classSubject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.ClassRoom;
import vn.attendance.model.ClassSubject;
import vn.attendance.model.Subject;
import vn.attendance.model.Users;
import vn.attendance.repository.ClassRoomRepository;
import vn.attendance.repository.ClassSubjectRepository;
import vn.attendance.repository.SubjectRepository;
import vn.attendance.service.classSubject.request.AddClassSubjectRequest;
import vn.attendance.service.classSubject.request.EditClassSubjectRequest;
import vn.attendance.service.classSubject.response.ClassSubjectDto;
import vn.attendance.service.classSubject.response.IClassDto;
import vn.attendance.service.classSubject.service.ClassSubjectService;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClassSubjectImpl  implements ClassSubjectService {

    @Autowired
    ClassSubjectRepository classSubjectRepository;

    @Autowired
    ClassRoomRepository classRoomRepository;

    @Autowired
    SubjectRepository subjectRepository;





    @Override
    public ClassSubject findById(Integer id) throws AmsException {
        ClassSubject classSubject = classSubjectRepository.findById(id).orElse(null);
        if (classSubject == null || classSubject.getStatus().equals(Constants.STATUS_TYPE.DELETED)) {
            throw new AmsException(MessageCode.NOT_FOUND);
        }
        return classSubject;
    }

    @Override
    public List<ClassSubject> findAllClassSubject() {
        return classSubjectRepository.findAll();
    }

    @Override
    public Page<ClassSubjectDto> findClassSubject(String search, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return classSubjectRepository.findClassSubject(search, pageable);
    }

    @Override
    public List<AddClassSubjectRequest> importClassSubject(List<AddClassSubjectRequest> requestList) throws AmsException {
        for (AddClassSubjectRequest request: requestList
             ) {
            addClassSubject(request, 0);
        }
        return requestList;
    }

    @Override
    public AddClassSubjectRequest addClassSubject(AddClassSubjectRequest addClassSubjectRequest, Integer option) throws AmsException {

        Users users = BaseUserDetailsService.USER.get();

        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        ClassRoom classRoom = classRoomRepository.findClassByName(addClassSubjectRequest.getClassName());

        if (classRoom == null || classRoom.getStatus().equals(Constants.STATUS_TYPE.DELETED)) {
            if(option == 1 ) throw new AmsException(MessageCode.CLASS_NOT_FOUND);
            addClassSubjectRequest.setStatus("FAILED");
            addClassSubjectRequest.setErrorMess(MessageCode.CLASS_NOT_FOUND.getCode());
            return addClassSubjectRequest;
        }

        Subject subject = subjectRepository.findSubjectByCode(addClassSubjectRequest.getSubjectCode());

        if (subject == null || subject.getStatus().equals(Constants.STATUS_TYPE.DELETED)) {
            if(option == 1 ) throw new AmsException(MessageCode.SUBJECT_NOT_FOUND);
            addClassSubjectRequest.setStatus("FAILED");
            addClassSubjectRequest.setErrorMess(MessageCode.SUBJECT_NOT_FOUND.getCode());
            return addClassSubjectRequest;
        }

        ClassSubject old = classSubjectRepository.findByClassAndSubject(classRoom.getId(), subject.getId());

        if (old!=null) {
            if(option == 1 ) throw new AmsException(MessageCode.CLASS_SUBJECT_EXISTED);
            addClassSubjectRequest.setStatus("FAILED");
            addClassSubjectRequest.setErrorMess(MessageCode.CLASS_SUBJECT_EXISTED.getCode());
            return addClassSubjectRequest;
        }

        try {
            ClassSubject classSubject = new ClassSubject();
            classSubject.setClassId(classRoom.getId());
            classSubject.setSubjectId(subject.getId());
            classSubject.setStatus(Constants.STATUS_TYPE.ACTIVE);
            classSubject.setCreatedAt(LocalDateTime.now());
            classSubject.setCreatedBy(users.getId());
            classSubject.setModifiedBy(users.getId());
            classSubject.setModifiedAt(LocalDateTime.now());
            classSubjectRepository.save(classSubject);
            addClassSubjectRequest.setStatus("SUCCESS");

        } catch (Exception e) {
            if (option == 1) throw new AmsException(MessageCode.ADD_CLASS_SUBJECT_FAIL);
            addClassSubjectRequest.setStatus("FAILED");
            addClassSubjectRequest.setErrorMess(MessageCode.ADD_CLASS_SUBJECT_FAIL.getCode());
        }

        return addClassSubjectRequest;
    }



    @Override
    public EditClassSubjectRequest editClassSubject(int id, EditClassSubjectRequest editClassSubjectRequest) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();

        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        ClassSubject classSubject = classSubjectRepository.findById(id).orElse(null);

        if (classSubject == null || classSubject.getStatus().equals(Constants.STATUS_TYPE.DELETED)) {
            editClassSubjectRequest.setStatus("FAILED");
            editClassSubjectRequest.setErrorMess(MessageCode.CLASS_SUBJECT_NOT_FOUND.getCode());
            return editClassSubjectRequest;
        }

        ClassRoom classRoom = classRoomRepository.findById(editClassSubjectRequest.getClassId()).orElse(null);
        if (classRoom == null || classRoom.getStatus().equals(Constants.STATUS_TYPE.DELETED)) {
            editClassSubjectRequest.setStatus("FAILED");
            editClassSubjectRequest.setErrorMess(MessageCode.CLASS_NOT_FOUND.getCode());
            return editClassSubjectRequest;
        }
        Subject subject = subjectRepository.findById(editClassSubjectRequest.getSubjectId()).orElse(null);
        if (subject == null || subject.getStatus().equals(Constants.STATUS_TYPE.DELETED)) {
            editClassSubjectRequest.setStatus("FAILED");
            editClassSubjectRequest.setErrorMess(MessageCode.SUBJECT_NOT_FOUND.getCode());
            return editClassSubjectRequest;
        }

        List<ClassSubject> classSubjectList = classSubjectRepository.findAll();

        for (ClassSubject classSubject1 : classSubjectList) {
            if (classSubject1.getSubjectId().equals(editClassSubjectRequest.getSubjectId()) && classSubject1.getClassId().equals(editClassSubjectRequest.getClassId()) && classSubject1.getId() != id) {
                editClassSubjectRequest.setStatus("FAILED");
                editClassSubjectRequest.setErrorMess(MessageCode.CLASS_SUBJECT_EXISTED.getCode());
                return editClassSubjectRequest;
            }
        }

        try {
            classSubject.setClassId(editClassSubjectRequest.getClassId());
            classSubject.setSubjectId(editClassSubjectRequest.getSubjectId());
            classSubject.setModifiedAt(LocalDateTime.now());
            classSubject.setModifiedBy(users.getId());
            classSubjectRepository.save(classSubject);
            editClassSubjectRequest.setStatus("SUCCESS");
        } catch (Exception e) {
            throw new AmsException(MessageCode.ADD_CLASS_SUBJECT_FAIL);
        }

        return editClassSubjectRequest;
    }

    @Override
    public void deleteClassSubject(Integer id) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();

        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        ClassSubject classSubject = classSubjectRepository.findById(id).orElse(null);
        if(classSubject == null || classSubject.getStatus().equals(Constants.STATUS_TYPE.DELETED)){
            throw  new AmsException(MessageCode.CLASS_SUBJECT_NOT_FOUND);
        }


        classSubject.setStatus(Constants.STATUS_TYPE.DELETED);
        classSubject.setModifiedAt(LocalDateTime.now());
        classSubject.setModifiedBy(users.getId());

        classSubjectRepository.save(classSubject);
    }

    @Override
    public List<IClassDto> getClassesSubject(Integer subjectId) {
        return classSubjectRepository.getClassesSubject(subjectId);
    }

}
