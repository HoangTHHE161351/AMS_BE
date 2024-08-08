package vn.attendance.service.classRoom.service;

import org.springframework.data.domain.Page;
import vn.attendance.exception.AmsException;
import vn.attendance.model.ClassRoom;
import vn.attendance.service.classRoom.request.AddClassRoomRequest;
import vn.attendance.service.classRoom.request.EditClassRoomRequest;
import vn.attendance.service.classRoom.response.ClassRoomDto;
import vn.attendance.service.classRoom.response.IClassDto;

import java.time.LocalDate;
import java.util.List;

public interface ClassRoomService {

    Page<ClassRoomDto> findAllClassRoom(String search, String status, int page, int size);

    List<IClassDto> findAllClassRoom(Integer subjectId);

    List<ClassRoom> findAll();

    ClassRoom findById(Integer id) throws AmsException;

    AddClassRoomRequest addClassRoom(AddClassRoomRequest request, int option) throws AmsException;

    void editClassRoom(Integer id, EditClassRoomRequest request) throws AmsException;

    void deleteClassRoom(Integer id) throws AmsException;

    byte[] exportClassRoom(String search) throws AmsException;

    List<AddClassRoomRequest> importClassRoom(List<AddClassRoomRequest> classRoomRequests) throws AmsException;

    List<IClassDto> searchClassRoomForSchedule(Integer subjectId, LocalDate date, Integer slotId);
}
