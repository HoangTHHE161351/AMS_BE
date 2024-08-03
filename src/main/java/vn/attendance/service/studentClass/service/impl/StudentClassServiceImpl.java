package vn.attendance.service.studentClass.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.ClassRoom;
import vn.attendance.model.Role;
import vn.attendance.model.StudentClass;
import vn.attendance.model.Users;
import vn.attendance.repository.*;
import vn.attendance.service.accessLog.response.AccessLogDTO;
import vn.attendance.service.studentClass.request.AddStudentClassRequest;
import vn.attendance.service.studentClass.request.EditStudentClassRequest;
import vn.attendance.service.studentClass.response.StudentClassDto;
import vn.attendance.service.studentClass.response.StudentDto;
import vn.attendance.service.studentClass.service.StudentClassService;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentClassServiceImpl implements StudentClassService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    ClassRoomRepository classRoomRepository;

    @Autowired
    StudentClassRepository studentClassRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ClassSubjectRepository classSubjectRepository;
    @Autowired
    CurriculumSubjectRepository curriculumSubRepository;

    @Override
    public List<StudentClass> findAllStudentClass() {
        return List.of();
    }

    @Override
    public StudentClass findById(int id) throws AmsException {
        StudentClass studentClass = studentClassRepository.findById(id).orElse(null);
        if (studentClass == null || studentClass.getStatus().equals(Constants.STATUS_TYPE.DELETED)) {
            throw new AmsException(MessageCode.STUDENT_CLASS_NOT_FOUND);
        }
        return studentClass;
    }

    @Override
    public List<AddStudentClassRequest> importStudentClass(List<AddStudentClassRequest> requests) throws AmsException {
        for (AddStudentClassRequest request : requests
        ){
            addStudentClass(request, 0);
        }
        return requests;
    }

    @Override
    public List<StudentDto> getAllStudentsClass(Integer studentId) {
        return studentClassRepository.findStugetAllStudentsClassdentS(studentId).stream().map(StudentDto::new).collect(Collectors.toList());
    }

    @Override
    public Page<StudentClassDto> findStudentClass(String search, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return studentClassRepository.findStudentClass(search, pageable);
    }

    @Override
    public AddStudentClassRequest addStudentClass(AddStudentClassRequest request, Integer option) throws AmsException {

        Users users = BaseUserDetailsService.USER.get();

        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        Users student = studentRepository.findStudentByUsername(request.getUsername());

        if (student == null) {
            if(option == 1){
                throw new AmsException(MessageCode.STUDENT_NOT_FOUND);
            }
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            request.setErrorMess(MessageCode.STUDENT_NOT_FOUND.getCode());
            return request;
        }

        ClassRoom classRoom = classRoomRepository.findClassByName(request.getClassName());

        if (classRoom == null ) {
            if(option == 1){
                throw new AmsException(MessageCode.CLASS_NOT_FOUND);
            }
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            request.setErrorMess(MessageCode.CLASS_NOT_FOUND.getCode());
            return request;
        }

        List<StudentClass> studentClasses = studentClassRepository.findByStudentAndClass(student.getId(), classRoom.getId());
        if(studentClasses.size() > 0){
            if (option  == 1){
                throw new AmsException(MessageCode.STUDENT_ALREADY_IN_CLASS);
            }
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            request.setErrorMess(MessageCode.STUDENT_ALREADY_IN_CLASS.getCode());
            return request;
        }
        // Lấy các môn học của lớp học
        List<Integer> classSubjects = classSubjectRepository.findSubjectIdsByClassId(classRoom.getId());

        // Lấy các môn học của học sinh
        List<Integer> studentSubjects = curriculumSubRepository.findSubjectIdsByStudentId(student.getId());

        // Kiểm tra xem lớp học có môn học khớp với môn học của học sinh không
        boolean isMatching = classSubjects.stream().anyMatch(studentSubjects::contains);

        if (!isMatching) {
            if(option == 1){
                throw new AmsException(MessageCode.SUBJECT_MISMATCH);
            }
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            request.setErrorMess(MessageCode.SUBJECT_MISMATCH.getCode()); // Định nghĩa mã lỗi phù hợp
            return request;
        }

        StudentClass studentClass = new StudentClass();
        studentClass.setStudentId(student.getId());
        studentClass.setClassId(classRoom. getId());
        studentClass.setStatus(Constants.STATUS_TYPE.ACTIVE);
        studentClass.setCreatedBy(users.getId());
        studentClass.setCreatedAt(LocalDateTime.now());
        studentClassRepository.save(studentClass);
        request.setStatus(Constants.REQUEST_STATUS.SUCCESS);

        return request;
    }

    @Override
    public EditStudentClassRequest editStudentClass(int id, EditStudentClassRequest editStudentClassRequest) throws AmsException {

        Users users = BaseUserDetailsService.USER.get();

        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        Users student = studentRepository.findById(editStudentClassRequest.getStudentId()).orElse(null);

        if (student == null || student.getStatus().equals(Constants.STATUS_TYPE.DELETED)) {
            editStudentClassRequest.setStatus(Constants.REQUEST_STATUS.FAILED);
            editStudentClassRequest.setErrorMess(MessageCode.STUDENT_NOT_FOUND.getCode());
            return editStudentClassRequest;
        }

        Role role = roleRepository.findById(student.getRoleId()).orElse(null);

        if (role != null || role.getStatus().equals(Constants.STATUS_TYPE.DELETED) || !role.getRoleName().equals(Constants.ROLE.STUDENT)) {
            editStudentClassRequest.setStatus(Constants.REQUEST_STATUS.FAILED);
            editStudentClassRequest.setErrorMess(MessageCode.ROLE_NOT_FOUND.getCode());
            return editStudentClassRequest;
        }

        ClassRoom classRoom = classRoomRepository.findById(editStudentClassRequest.getClassId()).orElse(null);

        if (classRoom == null || classRoom.getStatus().equals(Constants.STATUS_TYPE.DELETED)) {
            editStudentClassRequest.setStatus(Constants.REQUEST_STATUS.FAILED);
            editStudentClassRequest.setErrorMess(MessageCode.CLASS_NOT_FOUND.getCode());
            return editStudentClassRequest;
        }

        List<StudentClass> studentClasses = studentClassRepository.findAll();

        for (StudentClass studentClass : studentClasses) {
            if (studentClass.getStudentId().equals(editStudentClassRequest.getStudentId()) && studentClass.getClassId().equals(editStudentClassRequest.getClassId()) && studentClass.getId() != id) {
                editStudentClassRequest.setStatus(Constants.REQUEST_STATUS.FAILED);
                editStudentClassRequest.setErrorMess(MessageCode.STUDENT_CLASS_EXISTED.getCode());
                return editStudentClassRequest;
            }
        }

        try {
            StudentClass studentClass = studentClassRepository.findById(id).orElse(null);

            if (studentClass == null || studentClass.getStatus().equals(Constants.STATUS_TYPE.DELETED)) {
                editStudentClassRequest.setStatus(Constants.REQUEST_STATUS.FAILED);
                editStudentClassRequest.setErrorMess(MessageCode.STUDENT_CLASS_NOT_FOUND.getCode());
                return editStudentClassRequest;
            }

            studentClass.setStudentId(editStudentClassRequest.getStudentId());
            studentClass.setClassId(editStudentClassRequest.getClassId());
            studentClass.setModifiedAt(LocalDateTime.now());
            studentClass.setModifiedBy(users.getId());
            studentClassRepository.save(studentClass);
            editStudentClassRequest.setStatus(Constants.REQUEST_STATUS.SUCCESS);
        } catch (Exception e) {
            throw new AmsException(MessageCode.ADD_STUDENT_CLASS_FAIL);
        }

        return editStudentClassRequest;
    }

    @Override
    public void deleteStudentClass(Integer id) throws AmsException {

        Users users = BaseUserDetailsService.USER.get();

        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        StudentClass studentClass = studentClassRepository.findById(id).orElse(null);
        if(studentClass == null || studentClass.getStatus().equals(Constants.STATUS_TYPE.DELETED)){
            throw new AmsException(MessageCode.STUDENT_CLASS_NOT_FOUND);
        }


        try {
            studentClass.setStatus(Constants.STATUS_TYPE.DELETED);
            studentClass.setModifiedBy(users.getId());
            studentClass.setModifiedAt(LocalDateTime.now());
            studentClassRepository.save(studentClass);
        } catch (Exception e) {
            throw new AmsException(MessageCode.DELETE_STUDENT_CLASS_FAIL);
        }

    }

}
