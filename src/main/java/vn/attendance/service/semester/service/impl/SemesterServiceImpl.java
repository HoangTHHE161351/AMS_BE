package vn.attendance.service.semester.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Semester;
import vn.attendance.model.Users;
import vn.attendance.repository.SemesterRepository;
import vn.attendance.service.semester.request.AddSemesterRequest;
import vn.attendance.service.semester.request.EditSemesterRequest;
import vn.attendance.service.semester.service.SemesterService;
import vn.attendance.service.semester.response.SemesterDto;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SemesterServiceImpl implements SemesterService {
    @Autowired
    SemesterRepository semesterRepository;

    @Override
    public Page<SemesterDto> searchSemester(String search, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return semesterRepository.searchSemester(search, status, pageable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AddSemesterRequest addSemester(AddSemesterRequest request, Integer option) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();
        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        List<Semester> semesterList = semesterRepository.findAll();


        Map<String, Integer> semesterMap = new HashMap<String, Integer>();
        for (Semester semester : semesterList) {
            semesterMap.put(semester.getSemesterName(), semester.getId());
        }

        if (semesterMap.containsKey(request.getSemesterName())) {
            if (option == 1 ) throw new AmsException(MessageCode.SEMESTER_ALREADY_EXISTS);

            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            request.setErrorMess(MessageCode.SEMESTER_ALREADY_EXISTS.getCode());
            return request;
        }
        if (!isValidTimeRange(request.getStartTime(), request.getEndTime())) {
            if (option == 1 ) throw new AmsException(MessageCode.INVALID_TIME_RANGE);

            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            request.setErrorMess(MessageCode.INVALID_TIME_RANGE.getCode());
            return request;
        }

        if (request.getStartTime().isBefore(LocalDate.now())) {
            if (option == 1 ) throw new AmsException("The start and end times must be from "+ LocalDate.now() +" or later.");

            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            request.setErrorMess("The start and end times must be from "+ LocalDate.now() +" or later.");
            return request;
        }

        try {
            Semester semester = new Semester();
            semester.setSemesterName(request.getSemesterName());
            semester.setStartTime(request.getStartTime().atStartOfDay());
            semester.setEndTime(request.getEndTime().atStartOfDay());
            semester.setDescription(request.getDescription());
            semester.setStatus(Constants.STATUS_TYPE.ACTIVE);
            semester.setCreatedAt(LocalDateTime.now());
            semester.setCreatedBy(users.getId());
            semesterList.add(semester);
            semesterRepository.save(semester);
        } catch (Exception e) {
            e.printStackTrace();
            if (option == 1 ) throw new AmsException(MessageCode.ADD_SEMESTER_FAIL);
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            request.setErrorMess(MessageCode.ADD_SEMESTER_FAIL.getCode());
            return request;
        }
        request.setStatus(Constants.REQUEST_STATUS.SUCCESS);
        return request;
    }

    @Override
    public List<Semester> getAllSemester() {
        return semesterRepository.getAllSemesters();
    }

    @Override
    public List<AddSemesterRequest> importSemester(List<AddSemesterRequest> requests) throws AmsException {
        for (AddSemesterRequest request : requests
        ){
            addSemester(request, 0);
        }
        return requests;
    }

    private boolean isValidTimeRange(LocalDate startTime, LocalDate endTime) {
        return startTime.isBefore(endTime);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EditSemesterRequest updateSemester(EditSemesterRequest request) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();
        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        Semester semester = semesterRepository.findById(request.getId()).orElse(null);

        if (semester == null || semester.getStatus().equals(Constants.STATUS_TYPE.DELETED)) {
            throw new AmsException(MessageCode.SEMESTER_NOT_FOUND);
        }
        Semester old = semesterRepository.findSemestersByName(request.getSemesterName());
        if(old!= null && old.getId().equals(request.getId())) throw new AmsException(MessageCode.SEMESTER_NAME_ALREADY_EXISTS);

        if (!isValidTimeRange(request.getStartTime(), request.getEndTime())) {
            throw new AmsException(MessageCode.INVALID_TIME_RANGE);
        }

        // Kiểm tra nếu thời gian kết thúc bị thay đổi
        if (!request.getEndTime().equals(semester.getEndTime().toLocalDate())) {
            //Nếu semester đã kết thúc -> báo lỗi
            if(semester.getEndTime().isBefore(LocalDateTime.now())) throw new AmsException(MessageCode.SEMESTER_END_TIME_HAS_ALREADY_PASSED);
            //Chưa kết thúc có thể thay đổi nhưng phải là 1 ngày của tương lai
            if(request.getEndTime().isBefore(LocalDate.now())) throw new AmsException("End time must be after the present");
        }

        // Kiểm tra nếu thời gian bắt đầu bị thay đổi và thời gian bắt đầu mới đã qua
        if (!request.getStartTime().equals(semester.getStartTime().toLocalDate())) {
            //Nếu đã bắt đầu thì không thể thay đổi
            if(semester.getStartTime().isBefore(LocalDateTime.now())) throw new AmsException(MessageCode.SEMESTER_START_TIME_HAS_ALREADY_PASSED);
            //Chưa bắt đầu có thể đổi lịch nhưng phải chọn ngày tương lai
            if(request.getStartTime().isBefore(LocalDate.now())) throw new AmsException("Start time must be after the present");
        }
        try {
            semester.setSemesterName(request.getSemesterName());
            semester.setStartTime(request.getStartTime().atStartOfDay());
            semester.setEndTime(request.getEndTime().atStartOfDay());

            semester.setDescription(request.getDescription());
            semester.setStatus(Constants.STATUS_TYPE.ACTIVE);
            semester.setModifiedAt(LocalDateTime.now());
            semester.setModifiedBy(users.getId());
            semesterRepository.save(semester);

            request.setStatus(Constants.REQUEST_STATUS.SUCCESS);
            return request;
        } catch (Exception e){
            throw new AmsException(MessageCode.UPDATE_SEMESTER_FAIL);
        }


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Semester deleteSemester(Integer id) throws AmsException {
        Users user = BaseUserDetailsService.USER.get();
        if (user == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        Semester semester = semesterRepository.findById(id).orElse(null);

        if (semester == null || semester.getStatus().equals(Constants.STATUS_TYPE.DELETED)) {
            throw new AmsException(MessageCode.SEMESTER_NOT_FOUND);
        }

        semester.setStatus(Constants.STATUS_TYPE.DELETED);
        semester.setModifiedAt(LocalDateTime.now());
        semester.setModifiedBy(user.getId());
        return semesterRepository.save(semester);
    }

}
