package vn.attendance.service.curriculum.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.*;
import vn.attendance.repository.*;
import vn.attendance.service.curriculum.request.AddCurriculumRequest;
import vn.attendance.service.curriculum.request.AddStudentCurriculumRequest;
import vn.attendance.service.curriculum.request.AddSubjectCurriculumRequest;
import vn.attendance.service.curriculum.request.EditCurriculumRequest;
import vn.attendance.service.curriculum.response.CurriculumDto;
import vn.attendance.service.curriculum.response.ICurriculumDto;
import vn.attendance.service.curriculum.response.ISubjectDto;
import vn.attendance.service.curriculum.service.CurriculumService;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CurriculumServiceImpl implements CurriculumService {
    @Autowired
    CurriculumRepository curriculumRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StudentCurriculumRepository studentCurriculumRepository;

    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    CurriculumSubjectRepository curriculumSubjectRepository;

    @Override
    public Curriculum findById(Integer id) throws AmsException {
        Curriculum curriculum = curriculumRepository.findById(id).orElse(null);
        if (curriculum == null || curriculum.getStatus().equals(Constants.STATUS_TYPE.DELETED)) {
            throw new AmsException(MessageCode.CURRICULUM_NOT_FOUND);
        }
        return curriculum;
    }

    @Override
    public Page<CurriculumDto> findCurriculum(String search, String status, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return curriculumRepository.findCurriculum(search, status, pageable);
    }

    @Override
    public List<Curriculum> findAllCurriculum() {
        return curriculumRepository.findAllCurriculum();
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void deleteCurriculum(Integer id) throws AmsException {
        Users user =BaseUserDetailsService.USER.get();
        if(user == null) throw new AmsException(MessageCode.USER_NOT_FOUND);

        Curriculum curriculum = curriculumRepository.findById(id).orElse(null);
        if (curriculum == null || curriculum.getStatus().equals(Constants.STATUS_TYPE.DELETED)) {
            throw new AmsException(MessageCode.CURRICULUM_NOT_FOUND);
        }

        if(curriculumRepository.getCountStudentCurriculum(id)>0){
            throw new AmsException(MessageCode.STUDENTS_ENROLLED_IN_CURRICULUM );
        }
        List<CurriculumSubject> curriculumSubjects = curriculumRepository.findAllByCurriculum(id);

        for (CurriculumSubject curriculumSubject: curriculumSubjects
             ) {
            curriculumSubject.setStatus(Constants.STATUS_TYPE.DELETED);
            curriculumSubject.setModifiedAt(LocalDateTime.now());
            curriculumSubject.setModifiedBy(user.getId());
            curriculumSubjectRepository.save(curriculumSubject);
        }
        curriculum.setStatus(Constants.STATUS_TYPE.DELETED);
        curriculum.setModifiedAt(LocalDateTime.now());
        curriculum.setModifiedBy(user.getId());
        curriculumRepository.save(curriculum);
    }


    @Override
    @Transactional(rollbackFor=Exception.class)
    public AddCurriculumRequest addCurriculum(AddCurriculumRequest request, Integer option) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();
        if (users==null) {
            throw  new AmsException(MessageCode.USER_NOT_FOUND);
        }
        Curriculum old = curriculumRepository.findCurriculumByName(request.getCurriculumName());
        if (old.getCurriculumName()!= null) throw new AmsException(MessageCode.CURRICULUM_NAME_EXIST);

        try {
            Curriculum curriculum = new Curriculum();
            curriculum.setCurriculumName(request.getCurriculumName());
            curriculum.setDescription(request.getDescription());
            curriculum.setStatus(Constants.STATUS_TYPE.ACTIVE);
            curriculum.setCreatedAt(LocalDateTime.now());
            curriculum.setCreatedBy(users.getId());
            curriculumRepository.save(curriculum);
        }catch (Exception e){
            if(option == 1) throw  new AmsException(MessageCode.ADD_CURRICULUM_FAIL);
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            request.setErrorMes(MessageCode.ADD_CURRICULUM_FAIL.getCode());
            return request;
        }
        request.setStatus(Constants.REQUEST_STATUS.SUCCESS);
        return request;
    }

    @Override
    public List<AddCurriculumRequest> importCurriculum(List<AddCurriculumRequest> requests) throws AmsException {
        for (AddCurriculumRequest request : requests
        ){
            addCurriculum(request, 0);
        }
        return requests;
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public EditCurriculumRequest editCurriculum(EditCurriculumRequest request) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();
        if (users==null) throw  new AmsException(MessageCode.USER_NOT_FOUND);

        Curriculum curriculum = curriculumRepository.findById(request.getId()).orElse(null);
        if (curriculum == null || curriculum.getStatus().equals(Constants.STATUS_TYPE.DELETED)) {
            throw new AmsException(MessageCode.CURRICULUM_NOT_FOUND);
        }

        Curriculum old = curriculumRepository.findCurriculumByName(request.getCurriculumName());
        if (old.getCurriculumName()!= null && old.getId() != request.getId()) throw new AmsException(MessageCode.CURRICULUM_NAME_EXIST);

        try {
            curriculum.setCurriculumName(request.getCurriculumName());
            curriculum.setDescription(request.getDescription());
            curriculum.setModifiedAt(LocalDateTime.now());
            curriculum.setModifiedBy(users.getId());
            curriculumRepository.save(curriculum);

            request.setStatus(Constants.REQUEST_STATUS.SUCCESS);
            return request;
        }catch (Exception e){
            throw new AmsException(MessageCode.UPDATE_CURRICULUM_FAIL);
        }
    }

    @Override
    public List<AddStudentCurriculumRequest> importStudentCurriculum(List<AddStudentCurriculumRequest> request) throws AmsException {
        for (AddStudentCurriculumRequest addStudentCurriculumRequest : request) {
            addStudentCurriculum(addStudentCurriculumRequest, 0);
        }
        return request;
    }

    @Override
    public List<AddSubjectCurriculumRequest> importSubjectCurriculum(List<AddSubjectCurriculumRequest> requests) throws AmsException {
        for (AddSubjectCurriculumRequest request: requests
             ) {
            addSubjectCurriculum(request, 0);
        }
        return requests;
    }

    @Override
    public AddSubjectCurriculumRequest addSubjectCurriculum(AddSubjectCurriculumRequest request, int option) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();
        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }
        Subject subject = subjectRepository.findSubjectByCode(request.getSubjectCode());
        if (subject == null) {
            if(option == 1){
                throw new AmsException(MessageCode.SUBJECT_NOT_FOUND);
            }
            request.setErrorMess(MessageCode.SUBJECT_NOT_FOUND.getCode());
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            return request;
        }
        Curriculum curriculum = curriculumRepository.findCurriculumByName(request.getCurriculumName());
        if (curriculum == null) {
            if(option == 1){
                throw new AmsException(MessageCode.CURRICULUM_NOT_FOUND);
            }
            request.setErrorMess(MessageCode.CURRICULUM_NOT_FOUND.getCode());
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            return request;
        }

        CurriculumSubject curriculumSubject1 = curriculumSubjectRepository.findByCurriculuAndSubject(curriculum.getId(),subject.getId());
        if(curriculumSubject1!=null){
            if(option == 1){
                throw new AmsException(MessageCode.SUBJECT_ALREADY_ADDED);
            }
            request.setErrorMess(MessageCode.SUBJECT_ALREADY_ADDED.getCode());
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            return request;
        }

        CurriculumSubject curriculumSubject = new CurriculumSubject();
        curriculumSubject.setCurriculumId(curriculum.getId());
        curriculumSubject.setSubjectId(subject.getId());
        curriculumSubject.setSemesterId(request.getSemesterNo());
        curriculumSubject.setStatus(Constants.STATUS_TYPE.ACTIVE);
        curriculumSubject.setCreatedAt(LocalDateTime.now());
        curriculumSubject.setCreatedBy(users.getId());

        curriculumSubjectRepository.save(curriculumSubject);
        request.setStatus(Constants.REQUEST_STATUS.SUCCESS);

        return null;
    }

    @Override
    public List<ISubjectDto> getAllSubjectsCurriculum(Integer curriculumId) {

        return curriculumRepository.getAllSubjectsCurriculum(curriculumId);
    }

    @Override
    public List<ICurriculumDto> getAllCurriculumsStudent(Integer studentId) {
        return curriculumRepository.getAllCurriculumsStudent(studentId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AddStudentCurriculumRequest addStudentCurriculum(AddStudentCurriculumRequest request, Integer option) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();
        if (users == null) {
                throw new AmsException(MessageCode.USER_NOT_FOUND);
        }
        Users student = studentRepository.findStudentByUsername(request.getUsername());
        if (student == null) {
            if (option == 1) {
                throw new AmsException(MessageCode.STUDENT_NOT_FOUND);
            }
            request.setErrorMess(MessageCode.STUDENT_NOT_FOUND.getCode());
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            return request;
        }
        Curriculum curriculum = curriculumRepository.findCurriculumByName(request.getCurriculumName());
        if (curriculum == null) {
            if (option == 1) {
                throw new AmsException(MessageCode.CURRICULUM_NOT_FOUND);
            }
            request.setErrorMess(MessageCode.CURRICULUM_NOT_FOUND.getCode());
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            return request;
        }

        StudentCurriculum current = studentCurriculumRepository.findByStudentId(student.getId());
        if(current!=null){
            if(current.getCurriculumId().equals(curriculum.getId())){
                if(option == 1){
                    throw new AmsException(MessageCode.CURRICULUM_ALREADY_ADDED );
                }
                request.setErrorMess(MessageCode.CURRICULUM_ALREADY_ADDED.getCode());
                request.setStatus(Constants.REQUEST_STATUS.FAILED);
                return request;
            }else{
                current.setStatus(Constants.STATUS_TYPE.INACTIVE);
                studentCurriculumRepository.save(current);
            }

        }

        StudentCurriculum studentCurriculum = new StudentCurriculum();
        studentCurriculum.setStudentId(student.getId());
        studentCurriculum.setCurriculumId(curriculum.getId());
        studentCurriculum.setCreatedAt(LocalDateTime.now());
        studentCurriculum.setCreatedBy(users.getId());
        studentCurriculum.setStatus(Constants.STATUS_TYPE.ACTIVE);
        studentCurriculumRepository.save(studentCurriculum);

        request.setStatus(Constants.REQUEST_STATUS.SUCCESS);
        return request;
    }
}
