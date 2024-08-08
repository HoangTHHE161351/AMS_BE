package vn.attendance.service.schedule.service;

import vn.attendance.exception.AmsException;
import vn.attendance.model.Schedule;
import vn.attendance.model.Users;
import vn.attendance.service.schedule.request.AddScheduleRequest;
import vn.attendance.service.schedule.request.EditScheduleRequest;
import vn.attendance.service.schedule.response.ExportScheduleDto;
import vn.attendance.service.schedule.response.IScheduleDto;
import vn.attendance.service.schedule.response.ScheduleDto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ScheduleService {

    IScheduleDto getScheduleById(Integer id) throws AmsException;

    String[][] findScheduleByUserId(LocalDate date) throws Exception;

    String[][] findScheduleByStudentId(LocalDate from, LocalDate to) throws Exception;

    List<AddScheduleRequest> importSchedule(List<AddScheduleRequest> stringList, String semesterName) throws AmsException;

    AddScheduleRequest addSchedule(int option, AddScheduleRequest request, String semesterName, LocalDate date) throws AmsException;

    EditScheduleRequest editSchedule(EditScheduleRequest request, LocalDate date) throws AmsException;

    void setRequestStatus(Object request, String status, String errorMess);

    Schedule deleteSchedule(Integer id) throws AmsException;

    List<ExportScheduleDto> exportSchedules() throws AmsException;
}
