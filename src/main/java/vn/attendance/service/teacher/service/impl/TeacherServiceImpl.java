package vn.attendance.service.teacher.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.attendance.repository.TeacherRepository;
import vn.attendance.repository.UsersRepository;
import vn.attendance.service.teacher.service.TeacherService;
import vn.attendance.service.teacher.service.response.TeacherDto;
import vn.attendance.service.user.service.response.UsersDto;

import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    TeacherRepository teacherRepository;

    @Override
    public Page<UsersDto> findTeacher(String search, String status, Integer gender, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return teacherRepository.findTeacher(search, status, gender, pageable);
    }

    @Override
    public List<TeacherDto> teacherByClassRoom(Integer id) {
        return teacherRepository.teacherByClassRoom(id);
    }

    @Override
    public List<TeacherDto> teacherBySubject(Integer id) {

        return teacherRepository.teacherBySubject(id);
    }

}
