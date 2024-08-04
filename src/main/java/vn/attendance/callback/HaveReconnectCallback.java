package vn.attendance.callback;

import com.sun.jna.Pointer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.attendance.controller.SocketController;
import vn.attendance.exception.AmsException;
import vn.attendance.lib.NetSDKLib;
import vn.attendance.service.notify.service.NotifyService;
import vn.attendance.service.server.ServerInstance;
import vn.attendance.service.server.service.DeviceService;
import vn.attendance.service.server.service.impl.ServerServiceImpl;
import vn.attendance.util.Constants;

@Component
public class HaveReconnectCallback implements NetSDKLib.fHaveReConnect {

    @Autowired
    private SocketController socketController;
    @Autowired
    private ServerInstance serverInstance;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private NotifyService notifyService;

    public void invoke(NetSDKLib.LLong lLoginID, String pchDVRIP, int nDVRPort, Pointer dwUser) {
        System.out.printf("Device[%s:%d] HaveReconnected!\n", pchDVRIP, nDVRPort);
        socketController.sendServerConnectMessage(Constants.SERVER_CONNECT.CONNECTED);
        serverInstance.setBConnect(true);
        deviceService.synchronizeCameraCCTV();
        try {
            notifyService.addNotifyForDevice(Constants.NOTIFY_TITLE.DEVICE_CONNECT, "Connect to IVSS Server");
            deviceService.changStatusIVSS(Constants.SERVER_CONNECT.CONNECTED);
        } catch (AmsException e) {
            System.err.println(e.getMessage());
        }
    }
}
