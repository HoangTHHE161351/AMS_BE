package vn.attendance.service.semester.service;

import org.springframework.data.domain.Page;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Semester;
import vn.attendance.service.semester.request.AddSemesterRequest;
import vn.attendance.service.semester.request.EditSemesterRequest;
import vn.attendance.service.semester.response.SemesterDto;

import java.util.List;

public interface SemesterService {
    // Page<Semester> findSemester(int page, int size);
    //NOTE
    
    Page<SemesterDto> searchSemester(String search, String status, int page, int size);

    AddSemesterRequest addSemester(AddSemesterRequest request, Integer option) throws AmsException;

    EditSemesterRequest updateSemester(EditSemesterRequest request) throws AmsException;

    Semester deleteSemester(Integer id) throws AmsException;

    List<Semester> getAllSemester();

    List<AddSemesterRequest> importSemester(List<AddSemesterRequest> requests) throws AmsException;
}
