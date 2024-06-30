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
import vn.attendance.service.server.ServerInstance;
import vn.attendance.service.server.request.ServerLoginRequest;
import vn.attendance.service.server.service.DeviceService;
import vn.attendance.service.server.service.FaceRecognitionService;
import vn.attendance.service.server.service.LogAccessService;
import vn.attendance.service.server.service.ServerService;
import vn.attendance.util.MessageCode;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class ServerServiceImpl implements ServerService {

    @Value("${device.ip}")
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
        DisconnectCallback.initialize(socketController);
        HaveReconnectCallback.initialize(socketController, this);
        InitTest();

        loginServer(deviceIp, devicePort, deviceUsername, devicePassword);
    }

    private void loginServer(String deviceIp, Integer devicePort, String deviceUsername, String devicePassword) {
        if(serverInstance.getLoginHandle().longValue()!=0){
            logout();
        }

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
            serverInstance.setDeviceInfo(pstOutParam.stuDeviceInfo);
            System.out.println(loginHandle);
            socketController.sendServerConnectMessage("Login");
            //Đồng bộ data
            // Tạo và bắt đầu một thread mới để đồng bộ dữ liệu
            Thread syncThread = new Thread(() -> SynchronizeData());
            syncThread.start();
        }else{
            socketController.sendServerConnectMessage("Login Fail");
            System.out.printf("CLIENT_LoginWithHighLevelSecurity Failed!LastError = %s\n", ToolKits.getErrorCode());
            System.out.println(ENUMERROR.getErrorMessage());
        }
    }

    private void SynchronizeData() {
        //Đồng bộ user
        faceRecognitionService.SynchronizeFace();
        //Đồng bộ camera
        deviceService.SynchronizeCamera();
        //Bật cameraStateCallback
        deviceService.AttachCameraState();
        //Bật RealLoadPic
        logAccessService.AttachRealLoadPic();
    }

    public void InitTest() {
        serverInstance.getNetSdk().CLIENT_Init(DisconnectCallback.getInstance(), null);
        serverInstance.getNetSdk().CLIENT_SetAutoReconnect(HaveReconnectCallback.getInstance(), null);
    }

    @Override
    public void login(ServerLoginRequest request) throws AmsException{
        loginServer(request.getIp(), request.getPort(), request.getUser(), request.getPassword());
        if(serverInstance.getLoginHandle().longValue()==0){
            throw new AmsException(MessageCode.SERVER_CONNECT_FAIL);
        }
    }

    @Override
    public void logout() {
        if(serverInstance.getLoginHandle().longValue()!=0){

            //Tắt các callback
            deviceService.DetachCameraState();
            logAccessService.DetachRealLoadPic();

            serverInstance.getNetSdk().CLIENT_Logout(serverInstance.getLoginHandle());
            socketController.sendServerConnectMessage("Logout");
        }
    }

    private static class DisconnectCallback implements NetSDKLib.fDisConnect {

        private static DisconnectCallback instance;

        private SocketController socketController;

        private DisconnectCallback(SocketController socketController) {
            this.socketController = socketController;
        }

        public static DisconnectCallback getInstance() {
            if (instance == null) {
                throw new IllegalStateException("DisconnectCallback must be initialized before use.");
            }
            return instance;
        }

        public static synchronized void initialize(SocketController socketController) {
            if (instance == null) {
                instance = new DisconnectCallback(socketController);
            }
        }

        public void invoke(NetSDKLib.LLong lLoginID, String pchDVRIP, int nDVRPort, Pointer dwUser) {
            System.out.printf("Device[%s:%d] Disconnect!\n", pchDVRIP, nDVRPort);
            socketController.sendServerConnectMessage("Disconnect");
        }
    }

    private static class HaveReconnectCallback implements NetSDKLib.fHaveReConnect {

        private static HaveReconnectCallback instance;
        private SocketController socketController;
        private ServerServiceImpl serverService;

        private HaveReconnectCallback(SocketController socketController, ServerServiceImpl serverService) {
            this.socketController = socketController;
            this.serverService = serverService;
        }

        public static HaveReconnectCallback getInstance() {
            if (instance == null) {
                throw new IllegalStateException("HaveReconnectCallback must be initialized before use.");
            }
            return instance;
        }

        public static synchronized void initialize(SocketController socketController, ServerServiceImpl serverService) {
            if (instance == null) {
                instance = new HaveReconnectCallback(socketController, serverService);
            }
        }

        public void invoke(NetSDKLib.LLong lLoginID, String pchDVRIP, int nDVRPort, Pointer dwUser) {
            System.out.printf("Device[%s:%d] HaveReconnected!\n", pchDVRIP, nDVRPort);
            socketController.sendServerConnectMessage("Reconnected");
            serverService.SynchronizeData();
        }
    }


    @PreDestroy
    public void cleanup() {

        if (serverInstance.getLoginHandle().longValue() != 0) {
            logout();
        }
        serverInstance.getNetSdk().CLIENT_Cleanup();
        System.out.println("See You...");
        socketController.sendServerConnectMessage("Server turn off");
    }
}
