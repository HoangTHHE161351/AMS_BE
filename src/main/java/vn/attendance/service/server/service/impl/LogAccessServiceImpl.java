package vn.attendance.service.server.service.impl;

import com.sun.jna.Pointer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.attendance.exception.AmsException;
import vn.attendance.lib.NetSDKLib;
import vn.attendance.lib.ToolKits;
import vn.attendance.lib.enumeration.EM_EVENT_IVS_TYPE;
import vn.attendance.model.Camera;
import vn.attendance.model.Room;
import vn.attendance.model.Schedule;
import vn.attendance.model.TimeSlot;
import vn.attendance.repository.*;
import vn.attendance.service.server.ServerInstance;
import vn.attendance.service.server.dto.AccessLogDTO;
import vn.attendance.service.server.dto.IAccessLogDto;
import vn.attendance.service.server.request.SendImageRequest;
import vn.attendance.service.server.service.FaceRecognitionService;
import vn.attendance.service.server.service.LogAccessService;
import vn.attendance.service.server.service.dto.IImageDto;
import vn.attendance.service.server.service.dto.ImageDto;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static vn.attendance.util.StructFieldChooser.GetSelectedSingleFieldValue;

@Service
public class LogAccessServiceImpl implements LogAccessService {

    @Autowired
    ServerInstance serverInstance;

    @Autowired
    public FaceRecognitionService faceRecognitionService;

    @Autowired
    private CameraRepository cameraRepository;

    @Autowired
    AccessLogRepository accessLogRepository;

    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    TimeSlotRepository timeSlotRepository;
    @Autowired
    RoomRepository roomRepository;

    @Override
    public boolean AttachRealLoadPic() {
        NetSDKLib.LLong realLoadHandler =
                serverInstance.getNetSdk().CLIENT_RealLoadPictureEx(
                        serverInstance.getLoginHandle(),
                        -1,
                        EM_EVENT_IVS_TYPE.EVENT_IVS_ALL.getType(),
                        1,
                        AnalyzerDataCB.getInstance(faceRecognitionService, cameraRepository),
                        null,
                        null);
        serverInstance.setRealLoadHandle(realLoadHandler);
        if (realLoadHandler.longValue() != 0) {
            System.out.println("RealLoad Success");
        } else {
            System.out.println("RealLoad Fail");
        }
        return realLoadHandler.longValue() != 0;
    }

    @Override
    public boolean DetachRealLoadPic() {
        var bRet = serverInstance.getNetSdk().CLIENT_StopLoadPic(serverInstance.getRealLoadHandle());
        if (bRet) {
            serverInstance.setRealLoadHandle(new NetSDKLib.LLong(0));
        }
        return bRet;
    }

    @Override
    public Page<AccessLogDTO> findLog(String search, Integer type, Integer roomId, LocalDate date, Integer notType, Integer page, Integer size) {

        Page<IAccessLogDto> accessLogDtos = accessLogRepository.findAllAccessLog(search, type, roomId, date, notType, PageRequest.of(page - 1, size));
        Page<AccessLogDTO> accessLogDTOPage = accessLogDtos.map(AccessLogDTO::new);
        return accessLogDTOPage;
    }

    @Override
    public Page<AccessLogDTO> findStrangerBySchedule(Integer scheduleId, Integer page, Integer size) throws AmsException {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElse(null);
        if(schedule == null || schedule.getStatus().equals(Constants.STATUS_TYPE.DELETED)){
            new AmsException(MessageCode.SCHEDULE_NOT_FOUND);
        }

        TimeSlot slot = timeSlotRepository.findById(schedule.getTimeSlotId())
                .orElse(null);
        if(slot == null || slot.getStatus().equals(Constants.STATUS_TYPE.DELETED)){
            new AmsException(MessageCode.TIMESLOT_NOT_FOUND);
        }
        Room room = roomRepository.findById(schedule.getRoomId())
                .orElse(null);
        if(room == null || room.getStatus().equals(Constants.STATUS_TYPE.DELETED)){
            new AmsException(MessageCode.ROOM_NOT_FOUND);
        }

        LocalDate learnDate = schedule.getLearnTimestamp().toLocalDate();

        return accessLogRepository.findStrangerBySchedule(learnDate,slot.getStartTime().minusMinutes(10),slot.getEndTime(), room.getId(), PageRequest.of(page - 1, size)).map(AccessLogDTO::new);
    }

    @Override
    public List<ImageDto> getImageLog(LocalDateTime time) {
        List<IImageDto> imageDtos = accessLogRepository.getImage(time);
        return imageDtos.stream().map(ImageDto::new).collect(Collectors.toList());
    }

    public static class AnalyzerDataCB implements NetSDKLib.fAnalyzerDataCallBack {

        private static AnalyzerDataCB instance;
        private FaceRecognitionService faceRecognitionService;
        private CameraRepository cameraRepository;

        private AnalyzerDataCB(FaceRecognitionService faceRecognitionService, CameraRepository cameraRepository) {
            this.faceRecognitionService = faceRecognitionService;
            this.cameraRepository = cameraRepository;
        }

        public static AnalyzerDataCB getInstance(FaceRecognitionService faceRecognitionService, CameraRepository cameraRepository) {
            if (instance == null) {
                synchronized (AnalyzerDataCB.class) {
                    if (instance == null) {
                        instance = new AnalyzerDataCB(faceRecognitionService, cameraRepository);
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
            Integer channelId = -1;
            NetSDKLib.NET_PIC_INFO stPicInfo = null;
            NetSDKLib.NET_TIME_EX UTC = null;

            switch (Objects.requireNonNull(EM_EVENT_IVS_TYPE.getEventType(dwAlarmType))) {
                case EVENT_IVS_FACEDETECT -> {
                    NetSDKLib.DEV_EVENT_FACEDETECT_INFO info = new NetSDKLib.DEV_EVENT_FACEDETECT_INFO();
                    ToolKits.GetPointerData(pAlarmInfo, info);

                    channelId = info.nChannelID;
                    Camera camera = cameraRepository.getCameraByChannel(channelId);
                    if (camera == null || !camera.getStatus().equals(Constants.CAMERA_STATE_TYPE.ACTIVE)) {
                        break;
                    }
                    stPicInfo = info.stuObject.stPicInfo;
                    UTC = info.UTC;
                    byte[] picture = pBuffer.getByteArray(stPicInfo.dwOffSet, stPicInfo.dwFileLenth);
                    if (picture.length <= 0) {
                        break;
                    }
                    String base64 = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(picture);
                    LocalDateTime localDateTime = LocalDateTime.of(
                            UTC.dwYear,
                            UTC.dwMonth,
                            UTC.dwDay,
                            UTC.dwHour,
                            UTC.dwMinute,
                            UTC.dwSecond
                    );
                    System.out.println("Face Detect");
                    SendImageRequest sendImageRequest = new SendImageRequest();
                    sendImageRequest.setPicture(base64);
                    sendImageRequest.setTime(localDateTime);
                    sendImageRequest.setCameraId(camera.getId());

                    CompletableFuture.supplyAsync(() -> {
                        try {
                            faceRecognitionService.sendImageToModule(sendImageRequest);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    });
                    break;
                }
                case EVENT_IVS_FACERECOGNITION -> {
                    NetSDKLib.DEV_EVENT_FACERECOGNITION_INFO info = new NetSDKLib.DEV_EVENT_FACERECOGNITION_INFO();
                    ToolKits.GetPointerData(pAlarmInfo, info);

                    channelId = info.nChannelID;
                    Camera camera = cameraRepository.getCameraByChannel(channelId);
                    if (camera == null || !camera.getStatus().equals(Constants.CAMERA_STATE_TYPE.ACTIVE)) {
                        break;
                    }
                    stPicInfo = (NetSDKLib.NET_PIC_INFO) GetSelectedSingleFieldValue("stuObject.stPicInfo", info, pAlarmInfo);
                    UTC = (NetSDKLib.NET_TIME_EX) GetSelectedSingleFieldValue("UTC", info, pAlarmInfo);

                    byte[] picture = pBuffer.getByteArray(stPicInfo.dwOffSet, stPicInfo.dwFileLenth);
                    String base64 = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(picture);
                    LocalDateTime localDateTime = LocalDateTime.of(
                            UTC.dwYear,
                            UTC.dwMonth,
                            UTC.dwDay,
                            UTC.dwHour,
                            UTC.dwMinute,
                            UTC.dwSecond
                    );
                    System.out.println("FaceRecognize");
                    SendImageRequest sendImageRequest = new SendImageRequest();
                    sendImageRequest.setPicture(base64);
                    sendImageRequest.setTime(localDateTime);
                    sendImageRequest.setCameraId(camera.getId());

                    CompletableFuture.supplyAsync(() -> {
                        try {
                            faceRecognitionService.sendImageToModule(sendImageRequest);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    });
                    break;
                }
                default -> System.out.println("Other Event received: " + dwAlarmType);
            }

            return 0;
        }
    }
}
