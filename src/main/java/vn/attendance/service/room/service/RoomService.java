package vn.attendance.service.room.service;

import org.springframework.data.domain.Page;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Room;
import vn.attendance.service.room.request.AddRoomRequest;
import vn.attendance.service.room.request.EditRoomRequest;
import vn.attendance.service.room.response.RoomDto;

import java.util.List;

public interface RoomService {

    Room findById(Integer id) throws AmsException;

    Page<RoomDto> findAll(String search, String status, int page, int size);

    List<Room> finAllRoom();

    AddRoomRequest addRoom(AddRoomRequest request, Integer option) throws AmsException;

    EditRoomRequest updateRoom(EditRoomRequest request) throws AmsException;

    Room deleteRoom(Integer id) throws AmsException;


    List<AddRoomRequest> importRooms(List<AddRoomRequest> roomsList) throws AmsException;


    byte[] exportRoom(String search) throws AmsException;


}

