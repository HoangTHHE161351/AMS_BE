package vn.attendance.service.server.service.impl;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.attendance.controller.SocketController;
import vn.attendance.exception.AmsException;
import vn.attendance.lib.NetSDKLib;
import vn.attendance.lib.NetSDKLib.*;
import vn.attendance.lib.ToolKits;
import vn.attendance.lib.structure.NET_CB_CAMERASTATE;
import vn.attendance.lib.structure.NET_IN_CAMERASTATE;
import vn.attendance.lib.structure.NET_OUT_CAMERASTATE;
import vn.attendance.model.Camera;
import vn.attendance.model.Role;
import vn.attendance.model.Users;
import vn.attendance.repository.CameraRepository;
import vn.attendance.repository.RoleRepository;
import vn.attendance.service.server.ServerInstance;
import vn.attendance.service.server.dto.CameraRule;
import vn.attendance.service.server.dto.LinkGroup;
import vn.attendance.service.server.response.CameraInfoDto;
import vn.attendance.service.server.response.CameraStateRes;
import vn.attendance.service.server.response.DiskInfoDto;
import vn.attendance.service.server.service.DeviceService;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService{

    @Autowired
    private ServerInstance serverInstance;

    @Autowired
    private CameraRepository cameraRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SocketController socketController;

    @Override
    public List<DiskInfoDto> GetHardDiskState() throws AmsException {
        IntByReference intRetLen = new IntByReference();
        NET_DEV_HARDDISK_STATE diskInfo = new NET_DEV_HARDDISK_STATE();
        List<DiskInfoDto> diskInfoList = new ArrayList();
        if (serverInstance.getNetSdk().CLIENT_QueryDevState(serverInstance.getLoginHandle(),
                NetSDKLib.NET_DEVSTATE_DISK,
                diskInfo.getPointer(),
                diskInfo.size(),
                intRetLen,
                5000)) {
            diskInfo.read();

            String[] diskType = {"Read-write drive", "Read-only drive", "Backup drive or media drive", "Redundant drive", "Snapshot drive"};
            String[] diskStatus = {"Sleep", "Active", "Fault"};
            String[] diskSignal = {"Local", "Remote"};
            for (int i = 0; i < diskInfo.dwDiskNum; ++i) {
                DiskInfoDto diskInfoDto = new DiskInfoDto();
                diskInfoDto.setNumber(String.valueOf(diskInfo.stDisks[i].bDiskNum));
                diskInfoDto.setPartition(String.valueOf(diskInfo.stDisks[i].bSubareaNum));
                diskInfoDto.setCapacity(diskInfo.stDisks[i].dwVolume);
                diskInfoDto.setFree(diskInfo.stDisks[i].dwFreeSpace);
                diskInfoDto.setType(diskType[(diskInfo.stDisks[i].dwStatus & 0xF0) >> 4]);
                diskInfoDto.setSignal(diskSignal[diskInfo.stDisks[i].bSignal]);
                diskInfoDto.setStatus(diskStatus[diskInfo.stDisks[i].dwStatus & 0x0F]);
                diskInfoList.add(diskInfoDto);
            }
        } else {
            throw new AmsException(MessageCode.DISK_STATE_FAIL);
        }
        return diskInfoList;
    }

    @Override
    public CameraRule GetCameraRule(Integer cameraId) throws AmsException {
        Camera camera = cameraRepository.getCameraById(cameraId);
        if (camera ==null || camera.getChannelId() == null) {
            throw new AmsException(MessageCode.CAMERA_NOT_FOUND);
        }
        int channel = camera.getChannelId(); // Số kênh
        String command = NetSDKLib.CFG_CMD_ANALYSERULE; // Lệnh truy vấn cấu hình quy tắc

        int ruleCount = 10; // Số lượng quy tắc sự kiện
        CFG_RULE_INFO[] ruleInfo = new CFG_RULE_INFO[ruleCount];
        for (int i = 0; i < ruleCount; i++) {
            ruleInfo[i] = new CFG_RULE_INFO();
        }

        CFG_ANALYSERULES_INFO analyse = new CFG_ANALYSERULES_INFO();
        analyse.nRuleLen = 1024 * 1024 * 40;
        analyse.pRuleBuf = new Memory(1024 * 1024 * 40); // Cấp phát bộ nhớ
        analyse.pRuleBuf.clear(1024 * 1024 * 40);

        // Truy vấn cấu hình từ thiết bị
        if (ToolKits.GetDevConfig(serverInstance.getLoginHandle(), channel, command, analyse)) {
            int offset = 0;
            int count = analyse.nRuleCount < ruleCount ? analyse.nRuleCount : ruleCount;

            for (int i = 0; i < count; i++) {
                ToolKits.GetPointerDataToStruct(analyse.pRuleBuf, offset, ruleInfo[i]);
                offset += ruleInfo[0].size(); // Bước đệm quy tắc thông minh
                switch (ruleInfo[i].dwRuleType) {
                    case NetSDKLib.EVENT_IVSS_FACECOMPARE: {
                        NetSDKLib.CFG_FACECOMPARE_INFO msg = new NetSDKLib.CFG_FACECOMPARE_INFO();
                        ToolKits.GetPointerDataToStruct(analyse.pRuleBuf, offset, msg);
                        var rule = new CameraRule();
                        rule.setCameraId(cameraId);
                        rule.setEnable(msg.bRuleEnable==1);
                        List<LinkGroup> groups = new ArrayList<>();

                        for (NetSDKLib.CFG_LINKGROUP_INFO group: msg.stuLinkGroupArr
                        ) {
                            if(group.bEnable!=1) continue;
                            Role role = roleRepository.getByGroupId(new String(group.szGroupID).trim());
                            if(role == null) continue;
                            LinkGroup linkGroup = new LinkGroup();
                            linkGroup.setGroupName(role.getRoleName());
                            linkGroup.setEnable(group.bEnable==1);
                            groups.add(linkGroup);
                        }
                        rule.setGroups(groups);
                        rule.setStrangerMode(msg.stuStrangerMode.bEnable==1);
                        return rule;
                    }
                    default:
                        break;
                }
                offset += ruleInfo[i].nRuleSize; // Bước đệm của sự kiện thông minh
            }
        }
        throw  new AmsException(MessageCode.CAMERA_NOT_EVENT);
    }



    @Override
    public HashMap<String, CameraInfoDto> GetListCameraCCTV() {
        HashMap<String, CameraInfoDto> cameraInfoDtoList = new HashMap<>();
        if(!serverInstance.isConnect()){
            return cameraInfoDtoList;
        }
        int cameraCount = serverInstance.getDeviceInfo().byChanNum;
        NetSDKLib.NET_MATRIX_CAMERA_INFO[]  cameraInfo = new NetSDKLib.NET_MATRIX_CAMERA_INFO[cameraCount];
        for(int i = 0; i < cameraCount; i++) {
            cameraInfo[i] = new NetSDKLib.NET_MATRIX_CAMERA_INFO();
        }

        NetSDKLib.NET_IN_MATRIX_GET_CAMERAS inMatrix = new NetSDKLib.NET_IN_MATRIX_GET_CAMERAS();

        NetSDKLib.NET_OUT_MATRIX_GET_CAMERAS outMatrix = new NetSDKLib.NET_OUT_MATRIX_GET_CAMERAS();
        outMatrix.nMaxCameraCount = cameraCount;
        outMatrix.pstuCameras = new Memory(cameraInfo[0].size() * cameraCount);
        outMatrix.pstuCameras.clear(cameraInfo[0].size() * cameraCount);

        ToolKits.SetStructArrToPointerData(cameraInfo, outMatrix.pstuCameras);

        if(serverInstance.getNetSdk().CLIENT_MatrixGetCameras(serverInstance.getLoginHandle(), inMatrix, outMatrix, 5000)) {
            ToolKits.GetPointerDataToStructArr(outMatrix.pstuCameras, cameraInfo);
            for(int j = 0; j < outMatrix.nRetCameraCount; j++) {
                if(new String(cameraInfo[j].stuRemoteDevice.szIp).trim().isEmpty() || cameraInfo[j].bRemoteDevice == 0 || cameraInfo[j].stuRemoteDevice.szIp.toString().isEmpty()) {   // 过滤远程设备
                    continue;
                }
                CameraInfoDto cameraInfoDto = new CameraInfoDto();
                cameraInfoDto.setChannelId(cameraInfo[j].nUniqueChannel);
                cameraInfoDto.setIp( new String(cameraInfo[j].stuRemoteDevice.szIp).trim());
                cameraInfoDto.setPort( cameraInfo[j].stuRemoteDevice.nPort);

                cameraInfoDtoList.put(cameraInfoDto.getIp(),cameraInfoDto);
            }
        }
        return cameraInfoDtoList;
    }

    @Override
    public void AttachCameraState() {
        if(!serverInstance.isConnect()){
            return;
        }

        this.DetachCameraState();

        NET_IN_CAMERASTATE stuIn = new NET_IN_CAMERASTATE();
        stuIn.nChannels = 1;
        stuIn.pChannels = new IntByReference(-1).getPointer();
        stuIn.cbCamera = CameraStateCallBack.getInstance(socketController, cameraRepository, this);  // 注册回调

        NET_OUT_CAMERASTATE stuOut = new NET_OUT_CAMERASTATE();

        stuIn.write();
        stuOut.write();
        LLong m_hCameraStateHandle =  serverInstance.getNetSdk().CLIENT_AttachCameraState(serverInstance.getLoginHandle(), stuIn.getPointer(), stuOut.getPointer(), 3000);
        System.out.println(m_hCameraStateHandle);
        if (m_hCameraStateHandle.longValue() != 0) {
            System.out.printf("Chn[%d] CLIENT_AttachCameraState Success\n", -1);
            serverInstance.setCameraHandle(m_hCameraStateHandle);
        } else {
            System.out.printf("Ch[%d] CLIENT_AttachCameraState Failed!LastError = %s\n", -1, ToolKits.getErrorCode());
        }
    }


    public static class CameraStateCallBack implements NetSDKLib.fCameraStateCallBack {

        private static CameraStateCallBack instance;
        private DeviceServiceImpl deviceService;

        private CameraRepository  cameraRepository;

        private SocketController socketController;

        private CameraStateCallBack(SocketController socketController, CameraRepository cameraRepository, DeviceServiceImpl deviceService) {
            this.socketController = socketController;
            this.cameraRepository = cameraRepository;
            this.deviceService = deviceService;
        }

        public static CameraStateCallBack getInstance(SocketController socketController, CameraRepository cameraRepository, DeviceServiceImpl deviceService) {
            if (instance == null) {
                instance = new CameraStateCallBack(socketController, cameraRepository, deviceService);
            }
            return instance;
        }

        @Override
        public void invoke(NetSDKLib.LLong lLoginID, NetSDKLib.LLong lAttachHandle, Pointer pBuf, int nBufLen, Pointer dwUser) {
            NET_CB_CAMERASTATE cameraState = new NET_CB_CAMERASTATE();
            ToolKits.GetPointerDataToStruct(pBuf, 0, cameraState);
            Camera camera = cameraRepository.getCameraByChannel(cameraState.nChannel);
            if(camera!=null){
                String status = ParseCameraStatus(cameraState.emConnectState);
                if(status.equals(Constants.CAMERA_STATE_TYPE.ERROR)){
                    status = deviceService.getStateCamera(cameraState.nChannel);
                }
                camera.setStatus(status);
                var cameraStatus = new CameraStateRes();
                cameraStatus.setChannel(cameraState.nChannel);
                cameraStatus.setStatus(status);
                socketController.sendCameraStateMessage(cameraStatus);
                System.out.println("Channel:" + cameraStatus.getChannel() + " StatusCode:" + cameraState.emConnectState + "Status: "+status);
            }
        }
    }

    public static String ParseCameraStatus(int status) {

        String statusStr;
        switch (status) {
            case 0:
                statusStr = Constants.CAMERA_STATE_TYPE.UNKNOWN;
                break;
            case 1:
                statusStr = Constants.CAMERA_STATE_TYPE.CONNECTING;
                break;
            case 2:
                statusStr = Constants.CAMERA_STATE_TYPE.ACTIVE;
                break;
            case 3:
                statusStr = Constants.CAMERA_STATE_TYPE.DISCONNECT;
                break;
            default:
                statusStr = Constants.CAMERA_STATE_TYPE.ERROR;
                break;
        }
        return statusStr;
    }

    @Override
    public boolean DetachCameraState() {
        if(serverInstance.getCameraHandle().longValue()!=0 && serverInstance.getNetSdk().CLIENT_DetachCameraState(serverInstance.getCameraHandle()))
        {
            serverInstance.setCameraHandle(new LLong(0));
            return true;
        }
        return false;
    }

    @Override
    public void SynchronizeCamera() {

        List<Camera> cameras = cameraRepository.findAllCamera();

        for (Camera camera: cameras
             ) {
            camera.setStatus(Constants.CAMERA_STATE_TYPE.UNKNOWN);
            cameraRepository.save(camera);
        }
        if (serverInstance.isConnect()) {
            synchronizeCameraCCTV();
        }
    }

    @Override
    public void synchronizeCameraCCTV() {
        List<Camera> cameras = cameraRepository.findAllCameraCCTV();
        var cameraDtos = GetListCameraCCTV();
        for (Camera camera: cameras
        ) {
            if(cameraDtos.containsKey(camera.getIpTcip())){
                if(camera.getCameraType().equals(Constants.DEVICE_TYPE.CCTV)){
                    camera.setChannelId( cameraDtos.get(camera.getIpTcip()).getChannelId());
                    if(camera.getChannelId()==-1){
                        camera.setStatus(Constants.CAMERA_STATE_TYPE.ERROR);
                    }else{
                        camera.setStatus(getStateCamera(camera.getChannelId())); //Get status camera
                    }
                }
            }else{
                camera.setStatus(Constants.CAMERA_STATE_TYPE.ERROR);
            }
            cameraRepository.save(camera);
        }
    }


    @Override
    public void disconectCameraCCTV(){
        List<Camera> cameras = cameraRepository.findAllCameraCCTV();
        for (Camera camera: cameras
        ) {
            camera.setStatus(Constants.CAMERA_STATE_TYPE.UNKNOWN);
            cameraRepository.save(camera);
        }
    }

    public Integer getChannelId(Camera camera) {
        var cameras = GetListCameraCCTV();
        if(cameras.containsKey(camera.getIpTcip())&&cameras.get(camera.getIpTcip()).getPort() == Integer.parseInt(camera.getPort())){
            return cameras.get(camera.getIpTcip()).getChannelId();
        }
        return -1;
    }

    @Override
    public Camera checkCamera(Camera camera) {
        int channelId = getChannelId(camera);
        if(channelId!=-1){
            camera.setChannelId(channelId);
            camera.setStatus(getStateCamera(channelId));
            return camera;
        }
        return null;
    }

    private String getStateCamera(Integer channelId) {
        NET_CAMERA_STATE_INFO[] arrCameraStatus = new NET_CAMERA_STATE_INFO[serverInstance.getDeviceInfo().byChanNum];
        for(int i = 0; i < arrCameraStatus.length; i++) {
            arrCameraStatus[i] = new NET_CAMERA_STATE_INFO();
        }
        NET_IN_GET_CAMERA_STATEINFO stIn = new NET_IN_GET_CAMERA_STATEINFO();
        stIn.bGetAllFlag = 1; // 全部

        NET_OUT_GET_CAMERA_STATEINFO stOut = new NET_OUT_GET_CAMERA_STATEINFO();
        stOut.nMaxNum = serverInstance.getDeviceInfo().byChanNum;
        stOut.pCameraStateInfo = new Memory(arrCameraStatus[0].size() * serverInstance.getDeviceInfo().byChanNum);
        stOut.pCameraStateInfo.clear(arrCameraStatus[0].size() * serverInstance.getDeviceInfo().byChanNum);
        ToolKits.SetStructArrToPointerData(arrCameraStatus, stOut.pCameraStateInfo);

        stIn.write();
        stOut.write();

        boolean bRet = serverInstance.getNetSdk().CLIENT_QueryDevInfo(serverInstance.getLoginHandle(), NetSDKLib.NET_QUERY_GET_CAMERA_STATE,
                stIn.getPointer(), stOut.getPointer(), null, 3000);
        if(bRet) {
            stOut.read();
            ToolKits.GetPointerDataToStructArr(stOut.pCameraStateInfo, arrCameraStatus);  // 将Pointer拷贝到数组内存
            for (int i = 0; i < stOut.nValidNum; ++i) {
                if(arrCameraStatus[i].nChannel == channelId){
                    return ParseCameraStatus(arrCameraStatus[i].emConnectionState);
                }
            }
        } else {
            System.err.println("Query Camera State Failed!" + ToolKits.getErrorCode());
        }
        return Constants.CAMERA_STATE_TYPE.ERROR ;
    }

    @Override
    public void saveIvssServer(String deviceIp, Integer devicePort, String deviceUsername, String devicePassword){
        Camera camera = cameraRepository.getIVSS();
        if(camera == null){
            camera = new Camera();
            camera.setCameraType(Constants.DEVICE_TYPE.IVSS);
            camera.setCheckType(Constants.CHECK_TYPE.MANAGEMENT);
        }
        camera.setIpTcip(deviceIp.trim());
        camera.setPort(devicePort.toString());
        camera.setUsername(deviceUsername);
        camera.setPassword(devicePassword);
        camera.setRoomId(0);
        cameraRepository.save(camera);
    }

    @Override
    public void changStatusIVSS(String status) throws AmsException {
        Camera camera = cameraRepository.getIVSS();
        if(camera == null) throw new AmsException(MessageCode.NOT_FOUND);
        camera.setStatus(status);
        cameraRepository.save(camera);
    }
}
