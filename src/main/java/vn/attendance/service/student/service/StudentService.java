package vn.attendance.service.student.service;

import org.springframework.data.domain.Page;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Users;
import vn.attendance.service.student.request.AddStudentCurriculum;
import vn.attendance.service.student.request.EditStudentCurriculum;
import vn.attendance.service.student.response.StudentCurriculumDto;
import vn.attendance.service.student.response.StudentDto;
import vn.attendance.service.user.service.response.UsersDto;

import java.time.LocalDate;
import java.util.List;

public interface StudentService {
    byte[] exportStudent(String search, String status, String curriculumName) throws AmsException;

    StudentDto getStudentById(Integer id);

    List<String> getStudentTimeline(Integer id, LocalDate date);

    Page<Users> findStudent(int page, int size);

    Page<UsersDto> searchStudent(String search, String curriculumName, String status,
                                  int page, int size) ;

    AddStudentCurriculum addStudentCurriculum(AddStudentCurriculum request) throws AmsException;

    EditStudentCurriculum updateStudentCurriculum(Integer id, EditStudentCurriculum request) throws AmsException;

    Users deleteStudentCurriculum(Integer id) throws AmsException;

    Page<StudentCurriculumDto> searchStudentCurriculum(String search,int page, int size) ;
}
