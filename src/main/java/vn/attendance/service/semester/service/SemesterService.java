package vn.attendance.service.semester.service;

import org.springframework.data.domain.Page;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Semester;
import vn.attendance.model.StudentCurriculum;
import vn.attendance.service.semester.request.AddSemesterRequest;
import vn.attendance.service.semester.request.EditSemesterRequest;
import vn.attendance.service.semester.service.response.SemesterDto;
import vn.attendance.service.student.request.EditStudentCurriculum;
import vn.attendance.service.user.service.response.UsersDto;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface SemesterService {
   // Page<Semester> findSemester(int page, int size);
    //NOTE
    Page<SemesterDto> searchSemester(String search, int page, int size);

    AddSemesterRequest addSemester(@Valid AddSemesterRequest request) throws AmsException;

    EditSemesterRequest updateSemester(Integer id, EditSemesterRequest request) throws AmsException;

    public Optional<Semester> deleteSemester(Integer id) throws AmsException;

    List<Semester> getAllSemester();
}
