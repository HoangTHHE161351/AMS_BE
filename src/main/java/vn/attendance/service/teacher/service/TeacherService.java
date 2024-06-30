package vn.attendance.service.teacher.service;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import vn.attendance.model.Users;
import vn.attendance.service.teacher.service.response.TeacherDto;
import vn.attendance.service.user.service.response.UsersDto;

import java.util.List;

public interface TeacherService {

    Page<UsersDto> findTeacher(String search, String status, Integer gender, int page, int size);

    List<TeacherDto> teacherByClassRoom(Integer id);

    List<TeacherDto> teacherBySubject(Integer id);
}
