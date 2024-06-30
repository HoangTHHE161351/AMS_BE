package vn.attendance.service.classRoom.service;

import org.springframework.data.domain.Page;
import vn.attendance.exception.AmsException;
import vn.attendance.model.ClassRoom;
import vn.attendance.service.classRoom.request.AddClassRoomRequest;
import vn.attendance.service.classRoom.request.EditClassRoomRequest;
import vn.attendance.service.classRoom.response.ClassRoomDto;

import java.util.List;

public interface ClassRoomService {

    Page<ClassRoomDto> findAllClassRoom(String search, int page, int size);

    List<ClassRoom> findAll();

    ClassRoom findById(Integer id) throws AmsException;
    AddClassRoomRequest addClassRoom(AddClassRoomRequest request) throws AmsException;
    void editClassRoom(Integer id, EditClassRoomRequest request) throws AmsException;
    void deleteClassRoom(Integer id) throws AmsException;
}
