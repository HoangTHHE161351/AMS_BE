package vn.attendance.service.room.service;

import org.springframework.data.domain.Page;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Room;
import vn.attendance.service.room.request.AddRoomRequest;
import vn.attendance.service.room.request.EditRoomRequest;
import vn.attendance.service.room.response.RoomDto;

import java.util.List;
import java.util.Optional;

public interface RoomService {

    Room findById (Integer id) throws AmsException;

    Page<RoomDto> findAll(String search, int page, int size);

    List<Room> finAllRoom();
    AddRoomRequest addRoom(AddRoomRequest request) throws AmsException;

    EditRoomRequest updateRoom(Integer id, EditRoomRequest request);

    Optional<Room> deleteRoom(Integer id);
    byte[] exportRoom( String search) throws AmsException;

}

