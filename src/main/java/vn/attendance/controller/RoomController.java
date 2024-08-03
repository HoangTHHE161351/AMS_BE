package vn.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.attendance.exception.AmsException;
import vn.attendance.helper.ApiResponse;
import vn.attendance.service.accessLog.service.AccessLogService;
import vn.attendance.service.room.request.AddRoomRequest;
import vn.attendance.service.room.request.EditRoomRequest;
import vn.attendance.service.room.service.RoomService;
import vn.attendance.service.room.service.impl.RoomServiceImpl;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/room")
public class RoomController {

    @Autowired
    RoomService roomService;

    @Autowired
    AccessLogService accessLogService;

    @GetMapping("all-rooms")
    public ApiResponse<?> searchRoom(@RequestParam(required = false) String search,
                                     @RequestParam(required = false) String status,
                                     @RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size) {
        return ApiResponse.okStatus(roomService.findAll(search, status, page, size));
    }

    @GetMapping("room-detail")
    public ApiResponse<?> findRoomById(Integer id) throws AmsException {
        return ApiResponse.okStatus(roomService.findById(id));
    }

    @PostMapping("add-room")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ApiResponse<?> addRoom(@RequestBody @Valid AddRoomRequest request) throws AmsException {
        roomService.addRoom(request, 1);
        return ApiResponse.okStatus("Add room successfully");
    }

    @PutMapping("edit-room")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ApiResponse<?> updateRoom(@RequestBody EditRoomRequest request) throws AmsException {
        roomService.updateRoom(request);
        return ApiResponse.okStatus("Update Room Successfully");
    }

    @DeleteMapping("delete-room")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ApiResponse<?> deleteRoom(@RequestParam Integer id) throws AmsException {
        roomService.deleteRoom(id);
        return ApiResponse.okStatus("Delete Room Success");
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

    @PostMapping("import-room")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<?> importRoom(@RequestBody List<AddRoomRequest> roomLists) throws AmsException {
        return ApiResponse.okStatus(roomService.importRooms(roomLists));
    }

    @GetMapping("history-room-log")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ApiResponse<?> getHistoryLogInRoom(@RequestParam(required = false) String search,
                                              @RequestParam(required = false) Integer roomId,
                                              @RequestParam(required = false) LocalDate date,
                                              @RequestParam(defaultValue = "1") int page,
                                              @RequestParam(defaultValue = "10") int size) throws AmsException {
        return ApiResponse.okStatus(accessLogService.getAccessLogInRooms(search, roomId, date, page, size));
    }

    @GetMapping("history-log")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ApiResponse<?> getHistoryLog(@RequestParam(required = false) Integer roomId,
                                                         @RequestParam(required = false) String userName,
                                                         @RequestParam(required = false) LocalDate date) throws AmsException {
        return ApiResponse.okStatus(accessLogService.findByRoomIdAndUserId(roomId, userName, date));
    }
}
