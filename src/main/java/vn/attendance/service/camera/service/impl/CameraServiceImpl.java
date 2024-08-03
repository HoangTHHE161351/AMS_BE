package vn.attendance.service.camera.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Camera;
import vn.attendance.model.Room;
import vn.attendance.model.Users;
import vn.attendance.repository.CameraRepository;
import vn.attendance.repository.RoomRepository;
import vn.attendance.service.camera.request.AddCameraRequest;
import vn.attendance.service.camera.request.EditCameraRequest;
import vn.attendance.service.camera.response.CameraRes;
import vn.attendance.service.camera.service.CameraService;
import vn.attendance.service.mqtt.entity.CameraDto;
import vn.attendance.service.semester.response.SemesterDto;
import vn.attendance.service.server.ServerInstance;
import vn.attendance.service.server.service.DeviceService;
import vn.attendance.service.user.service.EmailService;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@Service
public class CameraServiceImpl implements CameraService {

    @Autowired
    private CameraRepository cameraRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ServerInstance serverInstance;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    EmailService emailService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AddCameraRequest addCamera(AddCameraRequest request) throws AmsException {
        // Lấy thông tin người dùng
        Users users = BaseUserDetailsService.USER.get();
        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }
        if (!isValidIpFormat(request.getIpTcpip())) {
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            request.setErrorMess(MessageCode.FORMAT_FAIL.getCode());
            return request;
        }

        try {
            if(cameraRepository.getCameraByIpPort(request.getIpTcpip(), request.getPort())!=null){
                request.setStatus(Constants.REQUEST_STATUS.FAILED);
                request.setErrorMess(MessageCode.CAMERA_ALREADY_EXISTED.getCode());
                return request;
            }
            if(roomRepository.getById(request.getRoomId())==null){
                request.setStatus(Constants.REQUEST_STATUS.FAILED);
                request.setErrorMess(MessageCode.ROOM_NOT_FOUND.getCode());
                return request;
            }
            // Tạo mới camera
            Camera camera = new Camera();
            camera.setIpTcip(request.getIpTcpip());
            camera.setPort(request.getPort());
            camera.setDescription(request.getDescription());
            camera.setStatus(Constants.STATUS_TYPE.ACTIVE);
            camera.setUsername(request.getUsername());
            camera.setPassword(request.getPassword());
            camera.setCameraType(request.getCameraType());
            camera.setCheckType(request.getCheckType());
            camera.setCreatedAt(LocalDateTime.now());
            camera.setCreatedBy(users.getId());
            camera.setRoomId(request.getRoomId());
            camera.setChannelId(request.getDeviceName());
            camera.setStatus(Constants.CAMERA_STATE_TYPE.PENDING);
          if(serverInstance.isConnect() && camera.getCameraType().equals(Constants.DEVICE_TYPE.CCTV)){
              camera = deviceService.checkCamera(camera);
              if(camera == null){
                  request.setStatus(Constants.REQUEST_STATUS.FAILED);
                  request.setErrorMess(MessageCode.CAMERA_NOT_FOUND.getCode());
                  return request;
              }
          }

        if(camera.getCameraType().equals(Constants.DEVICE_TYPE.LCD)){
            Camera finalCamera = camera;
            Executors.newScheduledThreadPool(1).schedule(() -> checkCameraLCD(finalCamera.getIpTcip()),  10, TimeUnit.SECONDS);
        }
            // Lưu camera vào database
            cameraRepository.save(camera);

            request.setStatus(Constants.REQUEST_STATUS.SUCCESS);
            return request;
        } catch (Exception e) {
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            request.setErrorMess(MessageCode.ADD_CAMERA_FAIL.getCode());
            return request;
        }
    }

    private void checkCameraLCD(String ip) {
        var camera = cameraRepository.findCameraLCDByIp(ip);
        if(camera == null) return;
        if(camera.getStatus().equals(Constants.CAMERA_STATE_TYPE.PENDING)){
            camera.setStatus(Constants.CAMERA_STATE_TYPE.ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editCamera(EditCameraRequest request) throws AmsException {
        // Lấy thông tin người dùng
        Users users = BaseUserDetailsService.USER.get();
        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }
        if (!isValidIpFormat(request.getIpTcpip())) {
            throw new AmsException(MessageCode.FORMAT_FAIL);
        }
        try {
            var camera = cameraRepository.getCameraByIpPort(request.getIpTcpip(), request.getPort());
            if(camera!=null&& camera.getId()!=request.getId()){
                throw new AmsException(MessageCode.CAMERA_ALREADY_EXISTED);
            }
            // Tạo mới camera
            camera.setIpTcip(request.getIpTcpip());
            camera.setPort(request.getPort());
            camera.setDescription(request.getDescription());
            camera.setUsername(request.getUsername());
            camera.setPassword(request.getPassword());
            camera.setCameraType(request.getCameraType());
            camera.setCheckType(request.getCheckType());
            camera.setModifiedAt(LocalDateTime.now());
            camera.setModifiedBy(request.getId());
            if(serverInstance.isConnect() && camera.getCameraType().equals(Constants.DEVICE_TYPE.CCTV)){
                camera = deviceService.checkCamera(camera);
                if(camera == null){
                    throw new AmsException(MessageCode.CAMERA_NOT_FOUND);
                }
            }
            if(camera.getCameraType().equals(Constants.DEVICE_TYPE.LCD)){
                camera.setStatus(Constants.CAMERA_STATE_TYPE.PENDING);
                Camera finalCamera = camera;
                Executors.newScheduledThreadPool(1).schedule(() -> checkCameraLCD(finalCamera.getIpTcip()),  10, TimeUnit.SECONDS);
            }
            // Lưu camera vào database
            cameraRepository.save(camera);
        } catch (Exception e) {

            throw new AmsException(MessageCode.UPDATE_CAMERA_FAIL);
        }
    }

    @Override
    public void deleteCamera(Integer cameraId) throws AmsException {
        try {
            var camera = cameraRepository.getById(cameraId);
            camera.setStatus(Constants.STATUS_TYPE.DELETED);
            cameraRepository.save(camera);
        } catch (Exception e) {

            throw new AmsException(MessageCode.DELETE_USER_FACE_FAIL);
        }
    }

    @Override
    public List<AddCameraRequest> importCamera(List<AddCameraRequest> cameraList) throws AmsException {
        for (var camera: cameraList
             ) {
            addCamera(camera);
        }
        return cameraList;
    }

    @Override
    public void getCameraAccess(Integer cameraId) throws AmsException {
        Camera camera = cameraRepository.findById(cameraId).orElse(null);
        if(camera == null || camera.getStatus().equals(Constants.STATUS_TYPE.DELETED)){
            throw new AmsException(MessageCode.CAMERA_NOT_FOUND);
        }
        List<Camera> cameras = new ArrayList<>();
        cameras.add(camera);
        sendCameraAccessByEmail(cameras);
    }

    @Override
    public void getCameraAccessByRoom(Integer roomId) throws AmsException {
        Room room = roomRepository.findById(roomId).orElse(null);
        if(room == null || room.getStatus().equals(Constants.STATUS_TYPE.DELETED)){
            throw new AmsException(MessageCode.ROOM_NOT_FOUND);
        }
        List<Camera> cameras = cameraRepository.findAllCameraCCTV(roomId);
        sendCameraAccessByEmail(cameras);
    }

    private void sendCameraAccessByEmail(List<Camera> cameras) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();
        if(users == null) throw new AmsException(MessageCode.USER_NOT_FOUND);
        StringBuilder content = new StringBuilder();

        // Build the content of the email with camera details
        content.append("Here are the camera details: <br><br>");

        for (Camera camera : cameras) {
            content.append("IP Address: ").append(camera.getIpTcip()).append("<br>")
                    .append("Username: ").append(camera.getUsername()).append("<br>")
                    .append("Password: ").append(camera.getPassword()).append("<br><br>");
        }

        content.append("Please make sure to keep this information secure.");

        try {
            emailService.sendSimpleMessage(
                    users.getEmail(),
                    "Camera Access",
                    users.getUsername(),
                    "Camera Access",
                    content.toString(),
                    null,  // Assuming no OTP needed in this context
                    3
            );
        } catch (MessagingException e) {
            throw new AmsException(MessageCode.SEND_EMAIL_FAIL);
        }
    }

    @Override
    public Page<CameraRes> findAllCamera(String search, String status, Integer roomId, int page, int size) {
        return cameraRepository.findAllCameraCCTV(search, status, roomId, PageRequest.of(page - 1, size)).map(CameraRes::new);
    }

    private boolean isValidIpFormat(String ip) {

        String ipPattern = "\\d{3}[.]\\d{3}[.]\\d{1,3}[.]\\d{1,3}";
        return ip.matches(ipPattern);
    }

    @Override
    public Page<CameraDto>findCamera (String search, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return cameraRepository.findCamera(search, pageable);
    }



}