package vn.attendance.service.semester.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Semester;
import vn.attendance.model.StudentCurriculum;
import vn.attendance.model.Users;
import vn.attendance.repository.SemesterRepository;
import vn.attendance.service.semester.request.AddSemesterRequest;
import vn.attendance.service.semester.request.EditSemesterRequest;
import vn.attendance.service.semester.service.SemesterService;
import vn.attendance.service.semester.service.response.SemesterDto;
import vn.attendance.service.student.request.EditStudentCurriculum;
import vn.attendance.service.user.service.response.UsersDto;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SemesterServiceImpl implements SemesterService {
    @Autowired
    SemesterRepository semesterRepository;

    @Override
    public Page<SemesterDto> searchSemester(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return semesterRepository.searchSemester(search, pageable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AddSemesterRequest addSemester(AddSemesterRequest request) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();
        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }
        if (users.getRoleId() != 1 && users.getRoleId() != 2) {
            throw new AmsException(MessageCode.USER_NOT_AUTHORITY);
        }
        List<Semester> semesterList = semesterRepository.findAll();


        Map<String, Integer> semesterMap = new HashMap<String, Integer>();
        for (Semester semester : semesterList) {
            semesterMap.put(semester.getSemesterName(), semester.getId());
        }

        if (semesterMap.containsKey(request.getSemesterName())) {
            request.setStatus("FAILED");
            request.setErrorMess(MessageCode.SEMESTER_ALREADY_EXISTS.getCode());
            return request;
        }
        if (!isValidTimeRange(request.getStartTime(), request.getEndTime())) {
            request.setStatus("FAILED");
            request.setErrorMess(MessageCode.INVALID_TIME_RANGE.getCode());
            return request;
        }
        if (request.getStartTime().getYear() < 2024 || request.getEndTime().getYear() < 2024) {
            request.setStatus("FAILED");
            request.setErrorMess("The start and end times must be from the year 2024 or later.");
            return request;
        }

        try {
            Semester semester = new Semester();
            semester.setSemesterName(request.getSemesterName());
            semester.setStartTime(request.getStartTime().atStartOfDay());
            semester.setEndTime(request.getEndTime().atStartOfDay());
            semester.setDescription(request.getDescription());
            semester.setCreatedAt(LocalDateTime.now());
            semester.setCreatedBy(users.getId());
            semesterList.add(semester);
            semesterRepository.save(semester);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AmsException(MessageCode.ADD_SEMESTER_FAIL);
        }
        return request;
    }

    @Override
    public List<Semester> getAllSemester() {
        return semesterRepository.getAllSemesters();
    }

    private boolean isValidTimeRange(LocalDate startTime, LocalDate endTime) {
        return startTime.isBefore(endTime);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EditSemesterRequest updateSemester(Integer id, EditSemesterRequest request) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();
        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        Optional<Semester> oldSemester= semesterRepository.findById(id);

        if (!oldSemester.isPresent()) {
            throw new AmsException(MessageCode.SEMESTER_NOT_FOUND);
        }


        List<Semester> semesterList= semesterRepository.findAll();
        Map<String, Integer> semesterMap = new HashMap<String, Integer>();
        for (Semester semester : semesterList) {
            semesterMap.put(semester.getSemesterName(), semester.getId());
        }

        if (semesterMap.containsKey(request.getSemesterName())) {
            request.setStatus("FAILED");
            request.setErrorMess(MessageCode.SEMESTER_ALREADY_EXISTS.getCode());
            return request;
        }
        if (!isValidTimeRange(request.getStartTime(), request.getEndTime())) {
            request.setStatus("FAILED");
            request.setErrorMess(MessageCode.INVALID_TIME_RANGE.getCode());
            return request;
        }
        if (request.getStartTime().getYear() < 2024 || request.getEndTime().getYear() < 2024) {
            request.setStatus("FAILED");
            request.setErrorMess("The start and end times must be from the year 2024 or later.");
            return request;
        }
        else {
            oldSemester.get().setStatus(Constants.STATUS_TYPE.DELETED);
            Semester newSemester = new Semester();

            newSemester.setId(oldSemester.get().getId());
            newSemester.setSemesterName(request.getSemesterName());
            newSemester.setStartTime(request.getStartTime().atStartOfDay());
            newSemester.setEndTime(request.getEndTime().atStartOfDay());

            newSemester.setDescription(request.getDescription());
            newSemester.setStatus(Constants.STATUS_TYPE.ACTIVE);
            newSemester.setModifiedAt(LocalDateTime.now());
            newSemester.setModifiedBy(users.getId());
            request.setStatus("SUCCESS");

            semesterRepository.save(oldSemester.get());
            semesterRepository.save(newSemester);
        }
        return request;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Optional<Semester> deleteSemester(Integer id) throws AmsException{
        Users user = BaseUserDetailsService.USER.get();
        if (user == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }
        return semesterRepository.findById(id).map(sc -> {
            sc.setStatus(Constants.STATUS_TYPE.DELETED);
            sc.setModifiedAt(LocalDateTime.now());
            sc.setModifiedBy(user.getId());
            return semesterRepository.save(sc);
        });
    }

}
