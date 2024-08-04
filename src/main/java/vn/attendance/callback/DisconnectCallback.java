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
import vn.attendance.util.Constants;

@Component
public class DisconnectCallback implements NetSDKLib.fDisConnect {

    @Autowired
    private ServerInstance serverInstance;

    @Autowired
    private SocketController socketController;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private NotifyService notifyService;

    public void invoke(NetSDKLib.LLong lLoginID, String pchDVRIP, int nDVRPort, Pointer dwUser) {
        System.out.printf("Device[%s:%d] Disconnect!\n", pchDVRIP, nDVRPort);
        serverInstance.setBConnect(false);
        socketController.sendServerConnectMessage(Constants.SERVER_CONNECT.DISCONNECT);
        deviceService.disconectCameraCCTV();
        try {
            notifyService.addNotifyForDevice(Constants.NOTIFY_TITLE.DEVICE_CONNECT, "Unable to connect to IVSS Server");
            deviceService.changStatusIVSS(Constants.SERVER_CONNECT.DISCONNECT);
        }catch (AmsException e){
            System.err.println(e.getMessage());
        }
    }
}