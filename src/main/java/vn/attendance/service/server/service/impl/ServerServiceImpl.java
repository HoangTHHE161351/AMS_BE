package vn.attendance.service.server.service.impl;

import com.sun.jna.Pointer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.attendance.callback.DisconnectCallback;
import vn.attendance.callback.HaveReconnectCallback;
import vn.attendance.controller.SocketController;
import vn.attendance.exception.AmsException;
import vn.attendance.lib.NetSDKLib;
import vn.attendance.lib.ToolKits;
import vn.attendance.lib.enumeration.ENUMERROR;
import vn.attendance.service.camera.response.CameraRes;
import vn.attendance.service.camera.response.ICameraRes;
import vn.attendance.service.notify.service.NotifyService;
import vn.attendance.service.server.ServerInstance;
import vn.attendance.service.server.request.ServerLoginRequest;
import vn.attendance.service.server.service.DeviceService;
import vn.attendance.service.server.service.FaceRecognitionService;
import vn.attendance.service.server.service.LogAccessService;
import vn.attendance.service.server.service.ServerService;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.CompletableFuture;

@Service
public class ServerServiceImpl implements ServerService {
    @Value(" ${device.ip}")
    private String deviceIp;

    @Value("${device.port}")
    private Integer devicePort;

    @Value("${device.username}")
    private String deviceUsername;

    @Value("${device.password}")
    private String devicePassword;

    @Autowired
    ServerInstance serverInstance;

    @Autowired
    public FaceRecognitionService faceRecognitionService;

    @Autowired
    public DeviceService deviceService;

    @Autowired
    public LogAccessService logAccessService;

    @Autowired
    private SocketController socketController;

    @Autowired
    private NotifyService notifyService;

    @Autowired
    DisconnectCallback disconnectCallback;

    @Autowired
    HaveReconnectCallback haveReconnectCallback;

    @PostConstruct
    private  void init() throws AmsException, InterruptedException {
        InitTest();

        loginServer(deviceIp, devicePort, deviceUsername, devicePassword);
        SynchronizeData();
        //Đồng bộ data
        faceRecognitionService.updateAllFace();
    }

    private void loginServer(String deviceIp, Integer devicePort, String deviceUsername, String devicePassword) throws AmsException {
        logout();

        NetSDKLib.NET_IN_LOGIN_WITH_HIGHLEVEL_SECURITY pstlnParam = new NetSDKLib.NET_IN_LOGIN_WITH_HIGHLEVEL_SECURITY() {
            {
                szIP = deviceIp.getBytes();
                nPort = devicePort;
                szUserName = deviceUsername.getBytes();
                szPassword = devicePassword.getBytes();
            }
        };

        NetSDKLib.NET_OUT_LOGIN_WITH_HIGHLEVEL_SECURITY pstOutParam = new NetSDKLib.NET_OUT_LOGIN_WITH_HIGHLEVEL_SECURITY();

        NetSDKLib.LLong loginHandle = serverInstance.getNetSdk().CLIENT_LoginWithHighLevelSecurity(pstlnParam, pstOutParam);
        deviceService.saveIvssServer(deviceIp, devicePort, deviceUsername, devicePassword);
        if (loginHandle.longValue() != 0) {
            System.out.println("Login Success");
            serverInstance.setLoginHandle(loginHandle);
            serverInstance.setBConnect(true);
            serverInstance.setDeviceInfo(pstOutParam.stuDeviceInfo);
            deviceService.changStatusIVSS(Constants.SERVER_CONNECT.CONNECTED);
            socketController.sendServerConnectMessage(Constants.SERVER_CONNECT.CONNECTED);
            notifyService.addNotifyForDevice(Constants.NOTIFY_TITLE.DEVICE_CONNECT, "Connect to IVSS Server");
        }else{
            socketController.sendServerConnectMessage("Login Fail");
            notifyService.addNotifyForDevice(Constants.NOTIFY_TITLE.DEVICE_CONNECT, "Unable to connect to IVSS Server");
            deviceService.changStatusIVSS(Constants.SERVER_CONNECT.DISCONNECT);
            System.out.printf("CLIENT_LoginWithHighLevelSecurity Failed!LastError = %s\n", ToolKits.getErrorCode());
            System.out.println(ENUMERROR.getErrorMessage());
        }
    }

    private void SynchronizeData(){
        CompletableFuture.supplyAsync(() -> {
            try {
                //Đồng bộ camera
                deviceService.SynchronizeCamera();
                //Bật cameraStateCallback
                deviceService.AttachCameraState();
                //Bật RealLoadPic
                logAccessService.AttachRealLoadPic();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });

    }

    public void InitTest() {
        serverInstance.getNetSdk().CLIENT_Init(disconnectCallback, null);
        serverInstance.getNetSdk().CLIENT_SetAutoReconnect(haveReconnectCallback, null);
    }

    @Override
    public void login(ServerLoginRequest request) throws AmsException{
        loginServer(request.getIp(), request.getPort(), request.getUser(), request.getPassword());
        SynchronizeData();
    }

    @Override
    public void logout() {
        if(serverInstance.isConnect()){
            //Tắt các callback
            deviceService.DetachCameraState();
            logAccessService.DetachRealLoadPic();

            serverInstance.getNetSdk().CLIENT_Logout(serverInstance.getLoginHandle());
            serverInstance.setBConnect(false);
            socketController.sendServerConnectMessage(Constants.SERVER_CONNECT.DISCONNECT);
        }
    }

    @PreDestroy
    public void cleanup() {
        logout();
        serverInstance.getNetSdk().CLIENT_Cleanup();
        System.out.println("See You...");
        socketController.sendServerConnectMessage("Server turn off");
    }
}
