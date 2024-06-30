package vn.attendance.service.cameraRoom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.*;
import vn.attendance.repository.CameraRepository;
import vn.attendance.repository.CameraRoomRepository;
import vn.attendance.repository.RoomRepository;
import vn.attendance.service.cameraRoom.request.AddCameraRoomRequest;
import vn.attendance.service.cameraRoom.service.CameraRoomService;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CameraRoomServiceImpl implements CameraRoomService {
    @Autowired
    private CameraRoomRepository cameraRoomRepository;

    @Autowired
    private CameraRepository cameraRepository;
    @Autowired
    private RoomRepository roomRepository;

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional(rollbackFor = Exception.class)
    public AddCameraRoomRequest addCameraRoom(AddCameraRoomRequest request) throws AmsException {
        Users user = BaseUserDetailsService.USER.get();
        if (user == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        List<CameraRoom> cameraRooms = cameraRoomRepository.findAll();
        // Check matching cameraId
        Camera matchingCamera = cameraRepository.findAll().stream().filter(c -> c.getId() == request.getCameraId())
                .findFirst().orElse(null);
        if (matchingCamera == null) {
            request.setStatus("FAILED");
            throw new AmsException(MessageCode.CAMERA_NOT_FOUND);
        }
        // Check matching roomId
        Room matchingRoom = roomRepository.findAll().stream().filter(c -> c.getId() == request.getRoomId())
                .findFirst().orElse(null);
        if (matchingRoom == null) {
            request.setStatus("FAILED");
            throw new AmsException(MessageCode.ROOM_NOT_FOUND);
        }
        // Check existed record
        for (CameraRoom cameraRoom : cameraRooms) {
            if (cameraRoom.getCameraId() == request.getCameraId()) {
                request.setStatus("FAILED");
                throw new AmsException(MessageCode.CAMERAROOM_ALREADY_EXISTED);
            }
        }
        try {
            CameraRoom newCameraRoom = new CameraRoom();
            newCameraRoom.setCameraId(request.getCameraId());
            newCameraRoom.setRoomId(request.getRoomId());
            newCameraRoom.setCreatedBy(user.getId());
            newCameraRoom.setCreatedAt(LocalDateTime.now());
            // Save cameraRoom
            cameraRoomRepository.save(newCameraRoom);
            request.setStatus("SUCCESS");
        } catch (Exception e) {
            throw new AmsException(MessageCode.ADD_CAMERAROOM_FAIL);
        }

        return request;
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional(rollbackFor = Exception.class)
    public AddCameraRoomRequest editCameraRoom(Integer id, AddCameraRoomRequest request) throws AmsException {
        Users user = BaseUserDetailsService.USER.get();
        if (user == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        List<CameraRoom> cameraRooms = cameraRoomRepository.findAll();
        // Check matching cameraId
        Camera matchingCamera = cameraRepository.findAll().stream().filter(c -> c.getId() == request.getCameraId())
                .findFirst().orElse(null);
        if (matchingCamera == null) {
            request.setStatus("FAILED");
            throw new AmsException(MessageCode.CAMERA_NOT_FOUND);
        }
        // Check matching roomId
        Room matchingRoom = roomRepository.findAll().stream().filter(c -> c.getId() == request.getRoomId())
                .findFirst().orElse(null);
        if (matchingRoom == null) {
            request.setStatus("FAILED");
            throw new AmsException(MessageCode.ROOM_NOT_FOUND);
        }

        // Check matching record
        for (CameraRoom cameraRoom : cameraRooms) {
            // One cameraRoom - 1 camera
            if (cameraRoom.getCameraId() == request.getCameraId()) {
                request.setStatus("FAILED");
                throw new AmsException(MessageCode.CAMERAROOM_ALREADY_EXISTED);
            }
        }

        CameraRoom cameraRoom = cameraRoomRepository.getById(id);
        if (cameraRoom == null) {
            request.setStatus("FAILED");
            throw new AmsException(MessageCode.CAMERAROOM_NOT_FOUND);
        }
        cameraRoom.setCameraId(request.getCameraId());
        cameraRoom.setRoomId(request.getRoomId());
        cameraRoom.setLocation(request.getLocation());
        cameraRoom.setModifiedAt(LocalDateTime.now());
        cameraRoom.setModifiedBy(user.getId());
        request.setStatus("SUCCESS");

        return request;
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional(rollbackFor = Exception.class)
    public void deleteCamera(Integer id) throws AmsException {
        Users user = BaseUserDetailsService.USER.get();
        if (user == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        CameraRoom cameraRoom = cameraRoomRepository.getById(id);
        if (cameraRoom == null) throw new AmsException(MessageCode.CAMERAROOM_NOT_FOUND);
        cameraRoom.setStatus(Constants.STATUS_TYPE.DELETED);
        cameraRoom.setModifiedAt(LocalDateTime.now());
        cameraRoom.setModifiedBy(user.getId());
    }

    @Override
    public Page<CameraRoom> findAllCameraRoom(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return cameraRoomRepository.findAllCameraRoom(pageable);
    }
}
