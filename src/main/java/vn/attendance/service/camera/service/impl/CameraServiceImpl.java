package vn.attendance.service.camera.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Camera;
import vn.attendance.model.Users;
import vn.attendance.repository.CameraRepository;
import vn.attendance.repository.UsersRepository;
import vn.attendance.service.camera.request.AddCameraRequest;
import vn.attendance.service.camera.service.CameraService;
import vn.attendance.service.server.ServerInstance;
import vn.attendance.service.server.service.DeviceService;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.time.LocalDateTime;


@Service
public class CameraServiceImpl implements CameraService {

    @Autowired
    private CameraRepository cameraRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ServerInstance serverInstance;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCamera(AddCameraRequest request) throws AmsException {
        // Lấy thông tin người dùng
        Users users = BaseUserDetailsService.USER.get();
        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }
        if (!isValidIpFormat(request.getIpTcpip())) {
            throw new AmsException(MessageCode.FORMAT_FAIL);
        }

        try {
            if(cameraRepository.getCameraByIpPort(request.getIpTcpip(), request.getPort())!=null){
                throw new AmsException(MessageCode.CAMERA_ALREADY_EXISTED);
            }
            // Tạo mới camera
            Camera camera = new Camera();
            camera.setIpTcip(request.getIpTcpip());
            camera.setPort(request.getPort());
            camera.setDescription(request.getDescription());
            camera.setStatus(Constants.STATUS_TYPE.ACTIVE);
            camera.setCreatedAt(LocalDateTime.now());
            camera.setCreatedBy(request.getId());
          if(serverInstance.getLoginHandle().longValue()!=0){
              camera = deviceService.checkCamera(camera);
              if(camera == null){
                  throw new AmsException(MessageCode.CAMERA_NOT_FOUND);
              }
          }
            // Lưu camera vào database
            cameraRepository.save(camera);
        } catch (Exception e) {

            throw new AmsException(MessageCode.ADD_CAMERA_FAIL);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editCamera(AddCameraRequest request) throws AmsException {
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
            camera.setModifiedAt(LocalDateTime.now());
            camera.setModifiedBy(request.getId());
            if(serverInstance.getLoginHandle().longValue()!=0){
                camera = deviceService.checkCamera(camera);
                if(camera == null){
                    throw new AmsException(MessageCode.CAMERA_NOT_FOUND);
                }
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
    public Page<Camera> findAllCamera(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return cameraRepository.findAllCamera(pageable);
    }

    private boolean isValidIpFormat(String ip) {

        String ipPattern = "\\d{3}[.]\\d{3}[.]\\d{1,3}[.]\\d{1,3}";
        return ip.matches(ipPattern);
    }


}