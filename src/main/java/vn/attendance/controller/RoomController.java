package vn.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.attendance.exception.AmsException;
import vn.attendance.helper.ApiResponse;
import vn.attendance.model.Room;
import vn.attendance.service.room.request.AddRoomRequest;
import vn.attendance.service.room.request.EditRoomRequest;
import vn.attendance.service.room.service.impl.RoomServiceImpl;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/room")
public class RoomController {

    @Autowired
    RoomServiceImpl roomService;

    @GetMapping("all-rooms")
    public ApiResponse<?> searchRoom(@RequestParam(required = false) String search,
                                     @RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size) {
        return ApiResponse.okStatus(roomService.findAll(search, page, size));
    }

    @GetMapping("room-detail")
    public ApiResponse<?> findRoomById(Integer id) throws AmsException {
        return ApiResponse.okStatus(roomService.findById(id));
    }

    @PostMapping("add-room")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ApiResponse<?> addRoom(@RequestBody @Valid AddRoomRequest request) throws AmsException {
        return ApiResponse.okStatus(roomService.addRoom(request));
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ApiResponse updateRoom(@PathVariable Integer id, @RequestBody EditRoomRequest request) {
        return ApiResponse.okStatus(roomService.updateRoom(id, request));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ApiResponse deleteRoom(@PathVariable Integer id) {
        Optional<Room> updatedRoom = roomService.deleteRoom(id);
        if (updatedRoom.isPresent()) {
            return ApiResponse.okStatus(updatedRoom.get());
        } else {
            return ApiResponse.errorStatus("Room not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("dropdown-rooms")
    public ApiResponse<?> dropdownRoom() {
        return ApiResponse.okStatus(roomService.finAllRoom());
    }

    @GetMapping("export-room")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<byte[]> exportRoom(@RequestParam String search) throws AmsException {
        byte[] bytes = roomService.exportRoom(search);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=rooms.xls");
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(bytes.length));

        return ResponseEntity.ok().headers(headers).body(bytes);
    }
}
