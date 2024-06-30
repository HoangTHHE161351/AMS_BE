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
import vn.attendance.repository.ClassRoomRepository;
import vn.attendance.repository.RoleRepository;
import vn.attendance.repository.StudentClassRepository;
import vn.attendance.repository.StudentRepository;
import vn.attendance.service.studentClass.request.AddStudentClassRequest;
import vn.attendance.service.studentClass.request.EditStudentClassRequest;
import vn.attendance.service.studentClass.response.StudentClassDto;
import vn.attendance.service.studentClass.service.StudentClassService;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.time.LocalDateTime;
import java.util.List;

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


    @Override
    public List<StudentClass> findAllStudentClass() {
        return List.of();
    }

    @Override
    public StudentClass findById(int id) throws AmsException {
        StudentClass studentClass = studentClassRepository.findById(id).orElse(null);
        if (studentClass == null) {
            throw new AmsException(MessageCode.STUDENT_CLASS_NOT_FOUND);
        }
        return studentClass;
    }

    @Override
    public Page<StudentClassDto> findStudentClass(String search, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return studentClassRepository.findStudentClass(search, pageable);
    }

    @Override
    public AddStudentClassRequest addStudentClass(AddStudentClassRequest addStudentClassRequest) throws AmsException {

        Users users = BaseUserDetailsService.USER.get();

        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        Users student = studentRepository.findById(addStudentClassRequest.getStudentId()).orElse(null);

        if (student == null || student.getStatus().equals(Constants.STATUS_TYPE.DELETED)) {
            addStudentClassRequest.setStatus("FAILED");
            addStudentClassRequest.setErrorMess(MessageCode.STUDENT_NOT_FOUND.getCode());
            return addStudentClassRequest;
        }

        Role role = roleRepository.findById(student.getRoleId()).orElse(null);

        if (role != null && !role.getRoleName().equals(Constants.ROLE.STUDENT)) {
            addStudentClassRequest.setStatus("FAILED");
            addStudentClassRequest.setErrorMess(MessageCode.STUDENT_NOT_FOUND.getCode());
            return addStudentClassRequest;
        }

        ClassRoom classRoom = classRoomRepository.findById(addStudentClassRequest.getClassId()).orElse(null);

        if (classRoom == null || classRoom.getStatus().equals(Constants.STATUS_TYPE.DELETED)) {
            addStudentClassRequest.setStatus("FAILED");
            addStudentClassRequest.setErrorMess(MessageCode.CLASS_NOT_FOUND.getCode());
            return addStudentClassRequest;
        }

        List<StudentClass> studentClasses = studentClassRepository.findAll();

        for (StudentClass studentClass : studentClasses) {
            if (studentClass.getStudentId().equals(addStudentClassRequest.getStudentId()) && studentClass.getClassId().equals(addStudentClassRequest.getClassId())) {
                addStudentClassRequest.setStatus("FAILED");
                addStudentClassRequest.setErrorMess(MessageCode.STUDENT_CLASS_EXISTED.getCode());
                return addStudentClassRequest;
            }
        }

        try {
            StudentClass studentClass = new StudentClass();
            studentClass.setStudentId(addStudentClassRequest.getStudentId());
            studentClass.setClassId(addStudentClassRequest.getClassId());
            studentClass.setStatus(Constants.STATUS_TYPE.ACTIVE);
            studentClass.setCreatedBy(users.getId());
            studentClass.setCreatedAt(LocalDateTime.now());
            studentClassRepository.save(studentClass);
            addStudentClassRequest.setStatus("SUCCESS");
        } catch (Exception e) {
            throw new AmsException(MessageCode.ADD_STUDENT_CLASS_FAIL);
        }

        return addStudentClassRequest;
    }

    @Override
    public EditStudentClassRequest editStudentClass(int id, EditStudentClassRequest editStudentClassRequest) throws AmsException {

        Users users = BaseUserDetailsService.USER.get();

        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        Users student = studentRepository.findById(editStudentClassRequest.getStudentId()).orElse(null);

        if (student == null || student.getStatus().equals(Constants.STATUS_TYPE.DELETED)) {
            editStudentClassRequest.setStatus("FAILED");
            editStudentClassRequest.setErrorMess(MessageCode.STUDENT_NOT_FOUND.getCode());
            return editStudentClassRequest;
        }

        Role role = roleRepository.findById(student.getRoleId()).orElse(null);

        if (role != null && !role.getRoleName().equals(Constants.ROLE.STUDENT)) {
            editStudentClassRequest.setStatus("FAILED");
            editStudentClassRequest.setErrorMess(MessageCode.STUDENT_NOT_FOUND.getCode());
            return editStudentClassRequest;
        }

        ClassRoom classRoom = classRoomRepository.findById(editStudentClassRequest.getClassId()).orElse(null);

        if (classRoom == null || classRoom.getStatus().equals(Constants.STATUS_TYPE.DELETED)) {
            editStudentClassRequest.setStatus("FAILED");
            editStudentClassRequest.setErrorMess(MessageCode.CLASS_NOT_FOUND.getCode());
            return editStudentClassRequest;
        }

        List<StudentClass> studentClasses = studentClassRepository.findAll();

        for (StudentClass studentClass : studentClasses) {
            if (studentClass.getStudentId().equals(editStudentClassRequest.getStudentId()) && studentClass.getClassId().equals(editStudentClassRequest.getClassId()) && studentClass.getId() != id) {
                editStudentClassRequest.setStatus("FAILED");
                editStudentClassRequest.setErrorMess(MessageCode.STUDENT_CLASS_EXISTED.getCode());
                return editStudentClassRequest;
            }
        }

        try {
            StudentClass studentClass = studentClassRepository.findById(id).orElse(null);
            studentClass.setStudentId(editStudentClassRequest.getStudentId());
            studentClass.setClassId(editStudentClassRequest.getClassId());
            studentClass.setModifiedAt(LocalDateTime.now());
            studentClass.setModifiedBy(users.getId());
            studentClassRepository.save(studentClass);
            editStudentClassRequest.setStatus("SUCCESS");
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

        if (studentClass == null) {
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
