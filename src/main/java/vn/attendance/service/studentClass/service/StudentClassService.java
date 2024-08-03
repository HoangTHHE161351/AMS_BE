package vn.attendance.service.studentClass.service;

import org.springframework.data.domain.Page;
import vn.attendance.exception.AmsException;
import vn.attendance.model.StudentClass;
import vn.attendance.service.studentClass.request.AddStudentClassRequest;
import vn.attendance.service.studentClass.request.EditStudentClassRequest;
import vn.attendance.service.studentClass.response.StudentClassDto;
import vn.attendance.service.studentClass.response.StudentDto;

import java.util.List;

public interface StudentClassService {

    List<StudentClass> findAllStudentClass();

    Page<StudentClassDto> findStudentClass(String search, Integer pageNo, Integer pageSize);

    AddStudentClassRequest addStudentClass(AddStudentClassRequest request, Integer option) throws AmsException;

    EditStudentClassRequest editStudentClass(int id, EditStudentClassRequest editStudentClassRequest) throws AmsException;

    void deleteStudentClass(Integer id) throws AmsException;

    StudentClass findById(int id) throws AmsException;

    List<AddStudentClassRequest> importStudentClass(List<AddStudentClassRequest> requests) throws AmsException;

    List<StudentDto> getAllStudentsClass(Integer studentId);
}

