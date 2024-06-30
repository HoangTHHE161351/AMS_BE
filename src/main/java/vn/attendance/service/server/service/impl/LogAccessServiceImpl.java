package vn.attendance.service.server.service.impl;

import com.sun.jna.Pointer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.attendance.controller.SocketController;
import vn.attendance.lib.NetSDKLib;
import vn.attendance.lib.ToolKits;
import vn.attendance.lib.enumeration.EM_EVENT_IVS_TYPE;
import vn.attendance.model.AccessLog;
import vn.attendance.repository.LogRepository;
import vn.attendance.service.server.ServerInstance;
import vn.attendance.service.server.response.FaceDetectRes;
import vn.attendance.service.server.service.LogAccessService;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Objects;

import static vn.attendance.util.StructFieldChooser.GetSelectedSingleFieldValue;

@Service
public class LogAccessServiceImpl implements LogAccessService {

    @Autowired
    ServerInstance serverInstance;

    @Autowired
    public LogRepository logRepository;

    @Autowired
    private SocketController socketController;

    @Override
    public boolean AttachRealLoadPic() {
        NetSDKLib.LLong realLoadHandler =
                serverInstance.getNetSdk().CLIENT_RealLoadPictureEx(
                        serverInstance.getLoginHandle(),
                        -1,
                        EM_EVENT_IVS_TYPE.EVENT_IVS_ALL.getType(),
                        1,
                        AnalyzerDataCB.getInstance(socketController, logRepository),
                        null,
                        null);
        serverInstance.setRealLoadHandle(realLoadHandler);
        if(realLoadHandler.longValue() != 0){
            System.out.println("RealLoad Success");
        }else {
            System.out.println("RealLoad Fail");
        }
        return realLoadHandler.longValue() != 0;
    }

    @Override
    public boolean DetachRealLoadPic() {
        var bRet = serverInstance.getNetSdk().CLIENT_StopLoadPic(serverInstance.getRealLoadHandle());
        if(bRet){
            serverInstance.setRealLoadHandle(new NetSDKLib.LLong(0));
        }
        return bRet;
    }

    public static class AnalyzerDataCB implements NetSDKLib.fAnalyzerDataCallBack{

        private static File picturePath;
        private static AnalyzerDataCB instance;
        private LogRepository logRepository;
        private SocketController socketController;

        private AnalyzerDataCB(SocketController socketController, LogRepository logRepository) {
            picturePath = new File("./AnalyzerPicture/");
            if (!picturePath.exists()) {
                picturePath.mkdirs();
            }
            this.socketController = socketController;
            this.logRepository = logRepository;
        }

        public static AnalyzerDataCB getInstance(SocketController socketController, LogRepository logRepository) {
            if (instance == null) {
                synchronized (AnalyzerDataCB.class) {
                    if (instance == null) {
                        instance = new AnalyzerDataCB(socketController, logRepository);
                    }
                }
            }
            return instance;
        }

        @Override
        public int invoke(NetSDKLib.LLong lAnalyzerHandle, int dwAlarmType, Pointer pAlarmInfo, Pointer pBuffer, int dwBufSize, Pointer dwUser, int nSequence, Pointer reserved) throws UnsupportedEncodingException {
            if (lAnalyzerHandle == null || lAnalyzerHandle.longValue() == 0) {
                return -1;
            }

            NetSDKLib.NET_EVENT_FILE_INFO stuFileInfo = null;
            NetSDKLib.NET_PIC_INFO stPicInfo = null;
            NetSDKLib.NET_TIME_EX UTC = null;

            switch (Objects.requireNonNull(EM_EVENT_IVS_TYPE.getEventType(dwAlarmType))) {
                case EVENT_IVS_FACEDETECT:
                {
                    NetSDKLib.DEV_EVENT_FACEDETECT_INFO info = new NetSDKLib.DEV_EVENT_FACEDETECT_INFO();
                    ToolKits.GetPointerData(pAlarmInfo, info);

                    stPicInfo = info.stuObject.stPicInfo;
                    UTC = info.UTC;
                    byte[] picture = pBuffer.getByteArray(stPicInfo.dwOffSet, stPicInfo.dwFileLenth);
                    if(picture.length<=0){
                        break;
                    }
                    AccessLog log = new AccessLog();
                    LocalDateTime localDateTime = LocalDateTime.of(
                            UTC.dwYear,
                            UTC.dwMonth,
                            UTC.dwDay,
                            UTC.dwHour,
                            UTC.dwMinute,
                            UTC.dwSecond
                    );
                    log.setTimeStamp(localDateTime);
                    log.setFaceImage(Base64.getEncoder().encodeToString(picture));
                    logRepository.save(log);
                    /*log.setStatus("DETECTING");*/
                    FaceDetectRes faceDetectRes = new FaceDetectRes();
                    faceDetectRes.setPicture(log.getFaceImage());
                    faceDetectRes.setLogId(log.getId());
                    System.out.println("Face Detect");
/*                    if (stPicInfo != null && stPicInfo.dwFileLenth > 0) {
                        String smallPicture = picturePath + File.separator + "Small_FaceDetect_"+UTC.toStringTitle()+".jpg";
                        ToolKits.savePicture(pBuffer, stPicInfo.dwOffSet, stPicInfo.dwFileLenth, smallPicture);
                    }


                    System.out.println("人脸检测: dwOffSet:" + stPicInfo.dwOffSet + ",dwFileLength:" + stPicInfo.dwFileLenth+ " 通道号:" + info.nChannelID);*/
                    socketController.getImageMessage(faceDetectRes);
                }

                case EVENT_IVS_FACERECOGNITION:
                {
                    NetSDKLib.DEV_EVENT_FACERECOGNITION_INFO info = new NetSDKLib.DEV_EVENT_FACERECOGNITION_INFO();
                    ToolKits.GetPointerData(pAlarmInfo, info);

                    stPicInfo = (NetSDKLib.NET_PIC_INFO) GetSelectedSingleFieldValue("stuObject.stPicInfo", info, pAlarmInfo);
                    UTC = (NetSDKLib.NET_TIME_EX) GetSelectedSingleFieldValue("UTC", info, pAlarmInfo);

                    byte[] picture = pBuffer.getByteArray(stPicInfo.dwOffSet, stPicInfo.dwFileLenth);
                    AccessLog log = new AccessLog();
                    LocalDateTime localDateTime = LocalDateTime.of(
                            UTC.dwYear,
                            UTC.dwMonth,
                            UTC.dwDay,
                            UTC.dwHour,
                            UTC.dwMinute,
                            UTC.dwSecond
                    );
                    log.setTimeStamp(localDateTime);
                    log.setFaceImage(Base64.getEncoder().encodeToString(picture));
                    logRepository.save(log);
                    /*log.setStatus("DETECTING");*/
                    FaceDetectRes faceDetectRes = new FaceDetectRes();
                    faceDetectRes.setPicture(log.getFaceImage());
                    faceDetectRes.setLogId(log.getId());
                    System.out.println("FaceRecognize");
/*                    if (stPicInfo != null && stPicInfo.dwFileLenth > 0) {
                        String smallPicture = picturePath + File.separator + "Small_FaceRecognize_"+UTC.toStringTitle()+".jpg";
                        ToolKits.savePicture(pBuffer, stPicInfo.dwOffSet, stPicInfo.dwFileLenth, smallPicture);
                    }


                    System.out.println("人脸检测: dwOffSet:" + stPicInfo.dwOffSet + ",dwFileLength:" + stPicInfo.dwFileLenth+ " 通道号:" + info.nChannelID);*/
                    socketController.getImageMessage(faceDetectRes);
                    break;
                }
                default:
                    System.out.println("Other Event received: " + dwAlarmType);
                    break;
            }

            return 0;
        }
    }
}
