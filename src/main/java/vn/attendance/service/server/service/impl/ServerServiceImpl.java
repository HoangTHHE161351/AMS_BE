package vn.attendance.service.server.service.impl;

import com.sun.jna.Pointer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.attendance.controller.SocketController;
import vn.attendance.exception.AmsException;
import vn.attendance.lib.NetSDKLib;
import vn.attendance.lib.ToolKits;
import vn.attendance.lib.enumeration.ENUMERROR;
import vn.attendance.service.camera.response.CameraRes;
import vn.attendance.service.camera.response.ICameraRes;
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

    @PostConstruct
    private  void init(){
        InitTest();

        loginServer(deviceIp, devicePort, deviceUsername, devicePassword);
        //Đồng bộ data
        // Tạo và bắt đầu một thread mới để đồng bộ dữ liệu
        CompletableFuture.supplyAsync(() -> {
            try {
                if(serverInstance.isConnect()) SynchronizeData();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    private void loginServer(String deviceIp, Integer devicePort, String deviceUsername, String devicePassword) {
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
        if (loginHandle.longValue() != 0) {
            System.out.println("Login Success");
            serverInstance.setLoginHandle(loginHandle);
            serverInstance.setBConnect(true);
            serverInstance.setDeviceInfo(pstOutParam.stuDeviceInfo);
            CameraRes device = new CameraRes();
            device.setCameraType(Constants.DEVICE_TYPE.IVSS);
            device.setIp(deviceIp);
            device.setPort(devicePort);

            serverInstance.setServerInfo(device);
            socketController.sendServerConnectMessage(Constants.SERVER_CONNECT.CONNECTED);
        }else{
            socketController.sendServerConnectMessage("Login Fail");
            System.out.printf("CLIENT_LoginWithHighLevelSecurity Failed!LastError = %s\n", ToolKits.getErrorCode());
            System.out.println(ENUMERROR.getErrorMessage());
        }
    }

    private void SynchronizeData() throws AmsException {
        //Đồng bộ camera
        deviceService.SynchronizeCamera();
        //Bật cameraStateCallback
        deviceService.AttachCameraState();
        //Bật RealLoadPic
        logAccessService.AttachRealLoadPic();
    }

    public void InitTest() {
        serverInstance.getNetSdk().CLIENT_Init(DisconnectCallback.getInstance(serverInstance, socketController, deviceService), null);
        serverInstance.getNetSdk().CLIENT_SetAutoReconnect(HaveReconnectCallback.getInstance(serverInstance, socketController, deviceService), null);
    }

    @Override
    public void login(ServerLoginRequest request) throws AmsException{
        loginServer(request.getIp(), request.getPort(), request.getUser(), request.getPassword());
        //Đồng bộ data
        // Tạo và bắt đầu một thread mới để đồng bộ dữ liệu
        CompletableFuture.supplyAsync(() -> {
            try {
                SynchronizeData();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
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

    private static class DisconnectCallback implements NetSDKLib.fDisConnect {

        private static DisconnectCallback instance;
        private ServerInstance serverInstance;

        private SocketController socketController;

        private DeviceService deviceService;


        private DisconnectCallback(ServerInstance serverInstance, SocketController socketController, DeviceService deviceService) {
            this.socketController = socketController;
            this.serverInstance = serverInstance;
            this.deviceService = deviceService;
        }

        public static DisconnectCallback getInstance(ServerInstance serverInstance, SocketController socketController, DeviceService deviceService) {
            if (instance == null) {
                instance = new DisconnectCallback(serverInstance, socketController, deviceService);            }
            return instance;
        }

        public void invoke(NetSDKLib.LLong lLoginID, String pchDVRIP, int nDVRPort, Pointer dwUser) {
            System.out.printf("Device[%s:%d] Disconnect!\n", pchDVRIP, nDVRPort);
            serverInstance.setBConnect(false);
            socketController.sendServerConnectMessage(Constants.SERVER_CONNECT.DISCONNECT);
            deviceService.disconectCameraCCTV();
        }
    }

    private static class HaveReconnectCallback implements NetSDKLib.fHaveReConnect {

        private static HaveReconnectCallback instance;
        private SocketController socketController;
        private ServerInstance serverInstance;
        private DeviceService deviceService;

        private HaveReconnectCallback(ServerInstance serverInstance, SocketController socketController, DeviceService deviceService) {
            this.socketController = socketController;
            this.serverInstance = serverInstance;
            this.deviceService = deviceService;
        }

        public static HaveReconnectCallback getInstance(ServerInstance serverInstance, SocketController socketController, DeviceService deviceService) {
            if (instance == null) {
                instance = new HaveReconnectCallback(serverInstance,socketController, deviceService);
            }
            return instance;
        }

        public void invoke(NetSDKLib.LLong lLoginID, String pchDVRIP, int nDVRPort, Pointer dwUser) {
            System.out.printf("Device[%s:%d] HaveReconnected!\n", pchDVRIP, nDVRPort);
            socketController.sendServerConnectMessage(Constants.SERVER_CONNECT.CONNECTED);
            serverInstance.setBConnect(true);
            deviceService.synchronizeCameraCCTV();
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
