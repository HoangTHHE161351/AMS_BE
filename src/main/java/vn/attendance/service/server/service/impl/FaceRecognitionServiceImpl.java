package vn.attendance.service.server.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jna.Memory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.attendance.config.mqtt.MqttConnectionStatusService;
import vn.attendance.controller.ModuleController;
import vn.attendance.controller.SocketController;
import vn.attendance.exception.AmsException;
import vn.attendance.job.entity.ChangeRequest;
import vn.attendance.lib.NetSDKLib.*;
import vn.attendance.lib.ToolKits;
import vn.attendance.model.*;
import vn.attendance.repository.*;
import vn.attendance.service.attendance.request.AutoAttendance;
import vn.attendance.service.attendance.service.AttendanceService;
import vn.attendance.service.mqtt.entity.CameraDto;
import vn.attendance.service.mqtt.entity.UserDto;
import vn.attendance.service.mqtt.service.MqttService;
import vn.attendance.service.notify.service.NotifyService;
import vn.attendance.service.server.ServerInstance;
import vn.attendance.service.server.dto.ILogDto;
import vn.attendance.service.server.dto.IScheduleCamDto;
import vn.attendance.service.server.request.SendImageRequest;
import vn.attendance.service.server.response.FaceDetectRes;
import vn.attendance.service.server.service.FaceRecognitionService;
import vn.attendance.service.user.service.request.IUserFace;
import vn.attendance.util.AvataUtils;
import vn.attendance.util.Constants;
import vn.attendance.util.DataUtils;
import vn.attendance.util.MessageCode;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class FaceRecognitionServiceImpl implements FaceRecognitionService {
    @Autowired
    ServerInstance serverInstance;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private AccessLogRepository accessLogRepository;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private CameraRepository cameraRepository;

    @Autowired
    private FacialRepository facialRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    NotifyService notifyService;

    @Autowired
    SocketController socketController;

    @Autowired
    MqttService mqttService;
    @Autowired
    TimeSlotRepository timeSlotRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    ModuleController moduleController;
    @Autowired
    MqttConnectionStatusService mqttConnectionStatusService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    /*    private static final String encode = "GBK";		// win*/

    private static final String encode = "UTF-8"; 	// linux

    @Override
    public void synchronizeFace() {
        System.out.println("Start SynchronizeFace");
        var roles = SynchronizeGroup();
        for (Role role: roles
        ) {
            SynchronizeUser(role.getRoleName(), role.getGroupId());
        }
        System.out.println("End SynchronizeFace");
    }

    @Override
    public Users createFace(Users user) throws AmsException {
        try {
            Role role = roleRepository.getById(user.getRoleId());
            Facial facial = facialRepository.findFacialByUserIdActive(user.getId());
            if(facial == null) return null;
            AvataUtils.AvataInfo avataInfo = AvataUtils.decodeBase64AndExtractInfo(facial.getImage());
            LocalDate dob = user.getDob();
            String[] dobArray = {String.valueOf(dob.getYear()), String.valueOf(dob.getMonthValue()), String.valueOf(dob.getDayOfMonth())};
            String id = addFaceRecognitionDB(role.getGroupId(),
                    avataInfo.getPictureLeng(),
                    avataInfo.getWidth(),
                    avataInfo.getHeight(),
                    avataInfo.getMemory(),
                    user.getLastName()+" "+user.getFirstName(),
                    user.getGender()==1?(byte) 1:(byte) 2,
                    dobArray,
                    user.getAddress(),
                    user.getAddress(),
                    user.getUsername(),
                    (byte) EM_CERTIFICATE_TYPE.CERTIFICATE_TYPE_UNKNOWN
            );
            if(id.isEmpty()){
                return null;
            }
            user.setFaceId(id);
            return user;
        }catch (Exception ex){
            return null;
        }

    }

    @Override
    public Users editFace(Users user) throws AmsException {
        try {
            if(user.getFaceId()==null){
                return createFace(user);
            }
            Role role = roleRepository.getById(user.getRoleId());
            Facial facial = facialRepository.findFacialByUserIdActive(user.getId());
            if(facial == null) return null;
            AvataUtils.AvataInfo avataInfo = AvataUtils.decodeBase64AndExtractInfo(facial.getImage());
            LocalDate dob = user.getDob();
            String[] dobArray = {String.valueOf(dob.getYear()), String.valueOf(dob.getMonthValue()), String.valueOf(dob.getDayOfMonth())};
            String id = modifyFaceRecognitionDB(role.getGroupId(),
                    user.getFaceId(),
                    avataInfo.getPictureLeng(),
                    avataInfo.getWidth(),
                    avataInfo.getHeight(),
                    avataInfo.getMemory(),
                    user.getLastName()+" "+user.getFirstName(),
                    user.getGender()==1?(byte) 1:(byte) 2,
                    dobArray,
                    user.getAddress(),
                    user.getAddress(),
                    user.getUsername(),
                    (byte) EM_CERTIFICATE_TYPE.CERTIFICATE_TYPE_UNKNOWN
            );
            if(id.isEmpty()){
                return null;
            }
            return user;
        }catch (Exception ex){
            return null;
        }
    }

    private List<Role> SynchronizeGroup() {
        //Đồng bộ nhóm người theo role
        List<Role> roles = roleRepository.findAll();
        for (Role role: roles
        ) {
            if(role.getGroupId() == null || !role.getRoleName().equals(findGroupInfo(role.getGroupId()))){
                var id = addFaceRecognitionGroup(role.getRoleName());
                role.setGroupId(id);
                roleRepository.save(role);
            }
        }
        return roles;
    }

    private void SynchronizeUser(String roleName, String groupId) {

        List<IUserFace> users = usersRepository.findUserByRole(roleName);
        HashMap<String,String> faces = findFaceRecognitionDB(groupId);

        for (var user: users
        ) {
            var username = user.getUsername();
            try {
                AvataUtils.AvataInfo avataInfo = AvataUtils.decodeBase64AndExtractInfo(user.getImage());
                LocalDate dob = user.getDob();
                String[] dobArray = {String.valueOf(dob.getYear()), String.valueOf(dob.getMonthValue()), String.valueOf(dob.getDayOfMonth())};

                if(faces.containsKey(username)){
                    if(user.getStatus().equals(Constants.STATUS_TYPE.DELETED)){
                        modifyFaceRecognitionDB(groupId,
                                faces.get(username),
                                avataInfo.getPictureLeng(),
                                avataInfo.getWidth(),
                                avataInfo.getHeight(),
                                avataInfo.getMemory(),
                                user.getFullname(),
                                user.getGender().byteValue(),
                                dobArray,
                                user.getAddress(),
                                user.getAddress(),
                                username,
                                (byte) EM_CERTIFICATE_TYPE.CERTIFICATE_TYPE_UNKNOWN
                        );
                    }
                }else{
                    String id = addFaceRecognitionDB(groupId,
                            avataInfo.getPictureLeng(),
                            avataInfo.getWidth(),
                            avataInfo.getHeight(),
                            avataInfo.getMemory(),
                            user.getFullname(),
                            user.getGender().byteValue(),
                            dobArray,
                            user.getAddress(),
                            user.getAddress(),
                            username,
                            (byte) EM_CERTIFICATE_TYPE.CERTIFICATE_TYPE_UNKNOWN
                    );
                    if(id.isEmpty()){
                        System.err.println("Add fail");
                        continue;
                    }else{
                        faces.put(username, id);
                    }
                }
                usersRepository.setFaceId(user.getId(), faces.get(username));
            }catch (Exception ex){
                System.err.println("Can't convert image");
            }
        }
    }

    //Tìm kiếm nhóm người dùng trong server, trống sẽ lấy toàn bộ
    public String findGroupInfo(String groupId) {
        // Tham số đầu vào
        NET_IN_FIND_GROUP_INFO stIn = new NET_IN_FIND_GROUP_INFO();
        System.arraycopy(groupId.getBytes(), 0, stIn.szGroupId, 0, groupId.getBytes().length);

        // Tham số đầu ra
        int max = 1;
        NET_FACERECONGNITION_GROUP_INFO[] groupInfo = new NET_FACERECONGNITION_GROUP_INFO[max];
        for(int i = 0; i < max; i++) {
            groupInfo[i] = new NET_FACERECONGNITION_GROUP_INFO();
        }

        NET_OUT_FIND_GROUP_INFO stOut = new NET_OUT_FIND_GROUP_INFO();
        stOut.pGroupInfos = new Memory(groupInfo[0].size() * max);     // Khởi tạo Pointer
        stOut.pGroupInfos.clear(groupInfo[0].size() * max);
        stOut.nMaxGroupNum = max;

        ToolKits.SetStructArrToPointerData(groupInfo, stOut.pGroupInfos);  // Sao chép dữ liệu mảng vào Pointer

        if(serverInstance.getNetSdk().CLIENT_FindGroupInfo(serverInstance.getLoginHandle(), stIn, stOut, 4000)) {
            ToolKits.GetPointerDataToStructArr(stOut.pGroupInfos, groupInfo);     // Lấy dữ liệu từ Pointer vào mảng NET_FACERECONGNITION_GROUP_INFO
            for (var group: groupInfo
            ) {
                String groupIdStr = new String(group.szGroupId).trim();
                String groupNameStr;
                try {
                    groupNameStr = new String(group.szGroupName, encode).trim();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    continue;
                }
                if(!groupNameStr.isEmpty()){
                    System.out.println(groupIdStr + " " + groupNameStr);
                    return groupNameStr;
                }
            }
        } else {
            System.err.println("Cannot query personal information" + ToolKits.getErrorCode());
        }
        return "";
    }


    public String addFaceRecognitionGroup(String groupName) {
        if(groupName.isEmpty()) {
            System.err.println("Please enter the name of the face database to add !");
            return "";
        }

        NET_ADD_FACERECONGNITION_GROUP_INFO addGroupInfo = new NET_ADD_FACERECONGNITION_GROUP_INFO();
        try {
            System.arraycopy(groupName.getBytes(encode),
                    0,
                    addGroupInfo.stuGroupInfo.szGroupName,
                    0,
                    groupName.getBytes(encode).length);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 入参
        NET_IN_OPERATE_FACERECONGNITION_GROUP stIn = new NET_IN_OPERATE_FACERECONGNITION_GROUP();
        stIn.emOperateType = EM_OPERATE_FACERECONGNITION_GROUP_TYPE.NET_FACERECONGNITION_GROUP_ADD; // 添加人员组信息
        stIn.pOPerateInfo = addGroupInfo.getPointer();

        //出参
        NET_OUT_OPERATE_FACERECONGNITION_GROUP stOut = new NET_OUT_OPERATE_FACERECONGNITION_GROUP();

        addGroupInfo.write();
        boolean bRet = serverInstance.getNetSdk().CLIENT_OperateFaceRecognitionGroup(serverInstance.getLoginHandle(), stIn, stOut, 4000);
        addGroupInfo.read();

        if(bRet) {
            System.out.println("Employee group id: " + new String(stOut.szGroupId).trim());  // 新增记录的人员组ID,唯一标识一组人员
            System.err.println("Successfully added face database");

        } else {
            System.err.println("Unable to add face database: ：" + ToolKits.getErrorCode());
            return "";
        }

        return new String(stOut.szGroupId).trim();
    }

    /**
     * Tìm kiếm thông tin nhận diện khuôn mặt
     * @param groupId   ID của thư viện khuôn mặt hiện tại đang tìm kiếm
     */
    public HashMap<String,String> findFaceRecognitionDB(String groupId) {
        HashMap<String,String> faces = new HashMap<>();

        // Thiết bị IVVS, chỉ các tham số trong stInStartFind.stPerson là có hiệu lực
        NET_IN_STARTFIND_FACERECONGNITION stInStartFind = new NET_IN_STARTFIND_FACERECONGNITION();
        // Kiểm tra tính hợp lệ của điều kiện tìm kiếm thông tin nhân viên, và sử dụng cấu trúc mở rộng
        stInStartFind.bPersonExEnable = 1;   // Điều kiện tìm kiếm thông tin nhân viên có hiệu lực và sử dụng cấu trúc mở rộng

        System.arraycopy(groupId.getBytes(), 0, stInStartFind.stPersonInfoEx.szGroupID, 0, groupId.getBytes().length);   // ID nhóm người

        // Thiết lập điều kiện lọc
        stInStartFind.stFilterInfo.nGroupIdNum = 1;   // Số lượng nhóm người
        System.arraycopy(groupId.getBytes(), 0, stInStartFind.stFilterInfo.szGroupIdArr[0].szGroupId, 0, groupId.getBytes().length);  // ID nhóm người
        stInStartFind.stFilterInfo.nRangeNum = 1;
        stInStartFind.stFilterInfo.szRange[0] = EM_FACE_DB_TYPE.NET_FACE_DB_TYPE_BLACKLIST;
        stInStartFind.stFilterInfo.emFaceType = EM_FACERECOGNITION_FACE_TYPE.EM_FACERECOGNITION_FACE_TYPE_ALL;

        stInStartFind.nChannelID = -1;

        // Đưa thiết bị vào chế độ sắp xếp kết quả dựa trên điều kiện tìm kiếm
        NET_OUT_STARTFIND_FACERECONGNITION stOutParam = new NET_OUT_STARTFIND_FACERECONGNITION();
        stInStartFind.write();
        stOutParam.write();
        if(serverInstance.getNetSdk().CLIENT_StartFindFaceRecognition(serverInstance.getLoginHandle(), stInStartFind,  stOutParam, 2000))
        {
            System.out.printf("Processing  token = %d\n" , stOutParam.nToken);

            int doNextCount = 0;    // Số lần tìm kiếm
            int count = 10;         // Số lượng bản ghi mỗi lần tìm kiếm
            // Tìm kiếm dữ liệu theo trang
            NET_IN_DOFIND_FACERECONGNITION  stFindIn = new NET_IN_DOFIND_FACERECONGNITION();
            stFindIn.lFindHandle = stOutParam.lFindHandle;
            stFindIn.nCount      = count;  // Số lượng bản ghi hiện tại muốn tìm kiếm
            stFindIn.nBeginNum   = 0;      // Bắt đầu từ số thứ tự

            NET_OUT_DOFIND_FACERECONGNITION stFindOut = new NET_OUT_DOFIND_FACERECONGNITION();
            stFindOut.bUseCandidatesEx = 1;   // Sử dụng cấu trúc mở rộng đối tượng ứng viên

            do
            {
                stFindIn.write();
                stFindOut.write();
                if(serverInstance.getNetSdk().CLIENT_DoFindFaceRecognition(stFindIn, stFindOut, 1000))
                {
                    System.out.printf("Number of records [%d]\n" , stFindOut.nCadidateExNum);

                    for(int i = 0; i < stFindOut.nCadidateExNum; i++)
                    {
                        // Id
                        String id = new String(stFindOut.stuCandidatesEx[i].stPersonInfo.szUID).trim();
                        // Mã giấy tờ
                        String username = new String(stFindOut.stuCandidatesEx[i].stPersonInfo.szID).trim();
                        System.out.println(id+":"+username);
                        faces.put(username,id);
                    }
                }
                else
                {
                    System.out.printf("CLIENT_DoFindFaceRecognition Failed! Final error = %x\n" , serverInstance.getNetSdk().CLIENT_GetLastError());
                    break;
                }

                if( stFindOut.nCadidateNum < stFindIn.nCount)
                {
                    System.out.println("No more records, search ended!\n");
                    break;
                } else {
                    stFindIn.nBeginNum += count;
                    doNextCount++;
                }
            } while (true);

            serverInstance.getNetSdk().CLIENT_StopFindFaceRecognition(stOutParam.lFindHandle);
        }
        else
        {
            System.out.println("CLIENT_StartFindFaceRecognition Failed, Error:" + ToolKits.getErrorCode());
        }
        return faces;
    }



    /**
     * Thêm thông tin người (đăng ký khuôn mặt)
     * @param groupId  ID nhóm (ID thư viện khuôn mặt)
     * @param nPicBufLen  Kích thước ảnh
     * @param width  Chiều rộng ảnh
     * @param height  Chiều cao ảnh
     * @param memory  Bộ nhớ lưu ảnh
     * @param personName  Tên người
     * @param bySex  Giới tính
     * @param birthday  Ngày sinh (mảng năm, tháng, ngày)
     * @param province  Tỉnh
     * @param city  Thành phố
     * @param id  Số chứng minh nhân dân
     * @param byIdType  Loại chứng minh nhân dân
     */
    public String addFaceRecognitionDB(String groupId, int nPicBufLen, int width, int height, Memory memory, String personName, byte bySex,
                                       String[] birthday, String province, String city, String id, byte byIdType) {
        // Tham số đầu vào
        NET_IN_OPERATE_FACERECONGNITIONDB stuIn = new NET_IN_OPERATE_FACERECONGNITIONDB();
        stuIn.emOperateType = EM_OPERATE_FACERECONGNITIONDB_TYPE.NET_FACERECONGNITIONDB_ADD;

        ///////// Sử dụng thông tin mở rộng của người /////////
        stuIn.bUsePersonInfoEx = 1;

        // Thiết lập ID nhóm
        System.arraycopy(groupId.getBytes(), 0, stuIn.stPersonInfoEx.szGroupID, 0, groupId.getBytes().length);

        // Thiết lập ngày sinh
        stuIn.stPersonInfoEx.wYear = (short)Integer.parseInt(birthday[0]);
        stuIn.stPersonInfoEx.byMonth = (byte)Integer.parseInt(birthday[1]);
        stuIn.stPersonInfoEx.byDay = (byte)Integer.parseInt(birthday[2]);

        // Giới tính, 1 - Nam, 2 - Nữ, khi làm điều kiện tìm kiếm, nếu tham số này là 0, thì tham số này không có hiệu lực
        stuIn.stPersonInfoEx.bySex = bySex;

        // Tên người
        try {
            System.arraycopy(personName.getBytes(encode), 0, stuIn.stPersonInfoEx.szPersonName, 0, personName.getBytes(encode).length);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // Loại chứng minh nhân dân
        stuIn.stPersonInfoEx.byIDType = byIdType;

        // Mã định danh duy nhất của người (số chứng minh nhân dân, mã số nhân viên, hoặc mã số khác)
        System.arraycopy(id.getBytes(), 0, stuIn.stPersonInfoEx.szID, 0, id.getBytes().length);

        // Quốc gia, tuân thủ chuẩn ISO3166
        System.arraycopy("VN".getBytes(), 0, stuIn.stPersonInfoEx.szCountry, 0, "VN".getBytes().length);

        // Tỉnh
        try {
            System.arraycopy(province.getBytes(encode), 0, stuIn.stPersonInfoEx.szProvince, 0, province.getBytes(encode).length);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // Thành phố
        try {
            System.arraycopy(city.getBytes(encode), 0, stuIn.stPersonInfoEx.szCity, 0, city.getBytes(encode).length);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // Thiết lập số lượng ảnh, kích thước, chiều rộng, chiều cao, bộ nhớ
        if(memory != null) {
            stuIn.stPersonInfoEx.wFacePicNum = 1; // Số lượng ảnh
            stuIn.stPersonInfoEx.szFacePicInfo[0].dwFileLenth = nPicBufLen;
            stuIn.stPersonInfoEx.szFacePicInfo[0].dwOffSet = 0;
            stuIn.stPersonInfoEx.szFacePicInfo[0].wWidth = (short)width;
            stuIn.stPersonInfoEx.szFacePicInfo[0].wHeight = (short)height;

            stuIn.nBufferLen = nPicBufLen;
            stuIn.pBuffer = memory;
        }

        // Tham số đầu ra
        NET_OUT_OPERATE_FACERECONGNITIONDB stuOut = new NET_OUT_OPERATE_FACERECONGNITIONDB();

        stuIn.write();
        if(serverInstance.getNetSdk().CLIENT_OperateFaceRecognitionDB(serverInstance.getLoginHandle(), stuIn, stuOut, 3000)) {
            System.out.println("Successfully added user information!");
        } else {
            System.out.println("Unable to add user information!");
            return "";
        }
        stuIn.read();

        return new String(stuOut.szUID).trim();
    }

    /**
     * Sửa đổi thông tin nhân sự (tức là đăng ký khuôn mặt)
     * @param groupId  ID nhóm (ID thư viện khuôn mặt)
     * @param szUID  Mã định danh duy nhất của nhân sự
     * @param nPicBufLen  Kích thước ảnh
     * @param width  Chiều rộng ảnh
     * @param height  Chiều cao ảnh
     * @param memory  Bộ nhớ lưu ảnh
     * @param personName  Tên nhân sự
     * @param bySex  Giới tính
     * @param birthday  Ngày sinh (mảng năm, tháng, ngày)
     * @param province  Tỉnh
     * @param city  Thành phố
     * @param id  Số chứng minh nhân dân
     * @param byIdType  Loại chứng minh nhân dân
     */
    public String modifyFaceRecognitionDB(String groupId, String szUID, int nPicBufLen, int width, int height, Memory memory, String personName, byte bySex,
                                          String[] birthday, String province, String city, String id, byte byIdType) {
        // Tham số đầu vào
        NET_IN_OPERATE_FACERECONGNITIONDB stuIn = new NET_IN_OPERATE_FACERECONGNITIONDB();
        stuIn.emOperateType = EM_OPERATE_FACERECONGNITIONDB_TYPE.NET_FACERECONGNITIONDB_MODIFY;

        ///////// Sử dụng thông tin mở rộng của nhân sự /////////
        stuIn.bUsePersonInfoEx = 1;

        // Thiết lập ID nhóm
        System.arraycopy(groupId.getBytes(), 0, stuIn.stPersonInfoEx.szGroupID, 0, groupId.getBytes().length);

        // Thiết lập UID
        System.arraycopy(szUID.getBytes(), 0, stuIn.stPersonInfoEx.szUID, 0, szUID.getBytes().length);

        // Thiết lập ngày sinh
        stuIn.stPersonInfoEx.wYear = (short)Integer.parseInt(birthday[0]);
        stuIn.stPersonInfoEx.byMonth = (byte)Integer.parseInt(birthday[1]);
        stuIn.stPersonInfoEx.byDay = (byte)Integer.parseInt(birthday[2]);

        // Giới tính, 1 - Nam, 2 - Nữ, khi làm điều kiện tìm kiếm, nếu tham số này là 0, thì tham số này không có hiệu lực
        stuIn.stPersonInfoEx.bySex = bySex;

        // Tên nhân sự
        try {
            System.arraycopy(personName.getBytes(encode), 0, stuIn.stPersonInfoEx.szPersonName, 0, personName.getBytes(encode).length);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // Loại chứng minh nhân dân
        stuIn.stPersonInfoEx.byIDType = byIdType;

        // Mã định danh duy nhất của nhân sự (số chứng minh nhân dân, mã số nhân viên, hoặc mã số khác)
        System.arraycopy(id.getBytes(), 0, stuIn.stPersonInfoEx.szID, 0, id.getBytes().length);

        // Quốc gia, tuân thủ chuẩn ISO3166
        System.arraycopy("CN".getBytes(), 0, stuIn.stPersonInfoEx.szCountry, 0, "CN".getBytes().length);

        // Tỉnh
        try {
            System.arraycopy(province.getBytes(encode), 0, stuIn.stPersonInfoEx.szProvince, 0, province.getBytes(encode).length);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // Thành phố
        try {
            System.arraycopy(city.getBytes(encode), 0, stuIn.stPersonInfoEx.szCity, 0, city.getBytes(encode).length);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // Thiết lập số lượng ảnh, kích thước, chiều rộng, chiều cao, bộ nhớ
        if(memory != null) {
            stuIn.stPersonInfoEx.wFacePicNum = 1; // Số lượng ảnh
            stuIn.stPersonInfoEx.szFacePicInfo[0].dwFileLenth = nPicBufLen;
            stuIn.stPersonInfoEx.szFacePicInfo[0].dwOffSet = 0;
            stuIn.stPersonInfoEx.szFacePicInfo[0].wWidth = (short)width;
            stuIn.stPersonInfoEx.szFacePicInfo[0].wHeight = (short)height;

            stuIn.nBufferLen = nPicBufLen;
            stuIn.pBuffer = memory;
        }

        // Tham số đầu ra
        NET_OUT_OPERATE_FACERECONGNITIONDB stuOut = new NET_OUT_OPERATE_FACERECONGNITIONDB();

        stuIn.write();
        if(serverInstance.getNetSdk().CLIENT_OperateFaceRecognitionDB(serverInstance.getLoginHandle(), stuIn, stuOut, 3000)) {
            System.out.println("Successfully updated employee information!");
        } else {
            System.out.println("Unable to update employee information!");
            return "";
        }
        stuIn.read();

        return new String(stuOut.szUID);
    }

    public boolean delFaceRecognitionDB(String groupId, String sUID) {
        // 入参
        NET_IN_OPERATE_FACERECONGNITIONDB stuIn  = new NET_IN_OPERATE_FACERECONGNITIONDB();
        stuIn.emOperateType = EM_OPERATE_FACERECONGNITIONDB_TYPE.NET_FACERECONGNITIONDB_DELETE;

        //////// 使用人员扩展信息  //////////
        stuIn.bUsePersonInfoEx = 1;

        // GroupID 赋值
        System.arraycopy(groupId.getBytes(), 0, stuIn.stPersonInfoEx.szGroupID, 0, groupId.getBytes().length);

        // UID赋值
        System.arraycopy(sUID.getBytes(), 0, stuIn.stPersonInfoEx.szUID, 0, sUID.getBytes().length);

        // 出参
        NET_OUT_OPERATE_FACERECONGNITIONDB stuOut = new NET_OUT_OPERATE_FACERECONGNITIONDB() ;

        if(serverInstance.getNetSdk().CLIENT_OperateFaceRecognitionDB(serverInstance.getLoginHandle(), stuIn, stuOut, 3000)) {
            System.out.println("Employee information successfully deleted!");
        } else {
            System.out.println("Unable to delete employee information!");
            System.err.println(serverInstance.getNetSdk().CLIENT_GetLastError());
            return false;
        }

        return true;
    }

    @Override
    public boolean deleteFace(Users user) throws AmsException{
        Role role = roleRepository.getById(user.getRoleId());
        if(!delFaceRecognitionDB(role.getGroupId(), user.getFaceId())){
            return false;
        }
        return true;
    }


    //Xử lý thông tin trả về từ Module AI
    @Override
    public void extractModuleLog(String message) throws AmsException{
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(message);

            // Trích xuất các giá trị trực tiếp từ JSON
            Integer logId = jsonNode.get("logId").asInt();
            String username = jsonNode.get("name").asText();
            Double score = jsonNode.get("score").asDouble();
            Integer similarity = (score.intValue()*100);

            ILogDto log = accessLogRepository.getLogById(logId);
            if(log == null) throw new AmsException(MessageCode.LOG_NOT_FOUND) ;
            if (username.equals("UNKNOWN")) {
                handleUnknownUser(log);
            } else {
                handleKnownUser(username, similarity, log);
            }
        }catch (IOException e) {
            throw new AmsException(MessageCode.LOG_FAIL);
        }

    }

    private void handleUnknownUser(ILogDto log) throws AmsException {
        accessLogRepository.updateLog(log.getId(), null, null, Constants.LOG_TYPE.UNKNOWN);
        System.out.println("======Log " + log);
        System.out.println("======LogId " + log.getId());
        addNotifyForLogs(log.getId());
        socketController.logStatus("New Log");
    }

    private void handleKnownUser(String username, Integer similarity, ILogDto log) throws AmsException {
        var userOptional = usersRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            handleUnknownUser(log);
            return;
        }

        var user = userOptional.get();
        if (log.getCheckType().equals("IN") || log.getCameraType().equals(Constants.DEVICE_TYPE.LCD)) {
            var auto = new AutoAttendance(username, log.getTime(), log.getCameraId());
            if (attendanceService.checkAttendance(auto) == null) {
                accessLogRepository.updateLog(log.getId(), user.getId(), similarity, Constants.LOG_TYPE.UNAUTHORIZED);
                addNotifyForLogs(log.getId());
                socketController.logStatus("New Log");
                return;
            }
        }
        accessLogRepository.updateLog(log.getId(), user.getId(), similarity, Constants.LOG_TYPE.IDENTIFIED);
        socketController.logStatus("New Log");
    }

    //Xử lý thông tin trả về từ MQTT
    public void extractJsonLcdLog(String message) {
        try {
            JsonNode rootNode = objectMapper.readTree(message);
            JsonNode infoNode = rootNode.path("info");

            String operator = rootNode.get("operator").asText();
            Integer deviceId = infoNode.path("facesluiceId").asInt();
            Camera camera = cameraRepository.getCameraByChannel(deviceId);
            if (camera == null) {
                return;
            }

            switch (operator) {
                case Constants.LCD_OPERATOR.CONTROL_LOG:
                    handleControlLog(infoNode, camera);
                    break;
                case Constants.LCD_OPERATOR.STRANGER_LOG:
                    handleStrangerLog(infoNode, camera);
                    break;
                case Constants.LCD_OPERATOR.ONLINE:
                    handleOnline(infoNode, deviceId);
                    break;
            }
        } catch (IOException | AmsException e) {
            // Log the error if necessary
        }
    }

    private void handleControlLog(JsonNode infoNode, Camera camera) throws AmsException {
        String username = infoNode.path("customId").asText();
        Optional<Users> user = usersRepository.findByUsername(username);
        if (!user.isPresent()) {
            return;
        }

        LocalDateTime time = LocalDateTime.parse(infoNode.path("time").asText(), formatter);
        AccessLog controlLog = new AccessLog();
        controlLog.setCameraId(camera.getId());
        controlLog.setUserId(user.get().getId());
        controlLog.setSimilarity((int) infoNode.path("similarity1").asDouble());
        controlLog.setTimeStamp(time);
        controlLog.setFaceImage(infoNode.path("pic").asText());

        Attendance attendance = attendanceService.checkAttendance(new AutoAttendance(username, time, camera.getId()));
        if (attendance == null) {
            controlLog.setType(Constants.LOG_TYPE.UNAUTHORIZED);
            accessLogRepository.save(controlLog);
            addNotifyForLogs(controlLog.getId());
        } else {
            controlLog.setType(Constants.LOG_TYPE.IDENTIFIED);
            accessLogRepository.save(controlLog);
            if ("IN".equals(camera.getCheckType())) {
                attendanceService.autoCheckAttendance(new AutoAttendance(username, controlLog.getTimeStamp(), camera.getId()));
            }
        }
        socketController.logStatus("New Log");
    }

    private void handleStrangerLog(JsonNode infoNode, Camera camera) throws AmsException {
        LocalDateTime time = LocalDateTime.parse(infoNode.path("time").asText(), formatter);
        AccessLog controlLog = new AccessLog();
        controlLog.setCameraId(camera.getId());
        controlLog.setTimeStamp(time);
        controlLog.setFaceImage(infoNode.path("pic").asText());
        controlLog.setType(Constants.LOG_TYPE.UNKNOWN);
        accessLogRepository.save(controlLog);
        addNotifyForLogs(controlLog.getId());
        socketController.logStatus("New Log");

/*        SendImageRequest sendImageRequest = new SendImageRequest();
        sendImageRequest.setCameraId(camera.getId());
        sendImageRequest.setTime(LocalDateTime.parse(infoNode.path("time").asText(), formatter));
        sendImageRequest.setPicture(infoNode.path("pic").asText());
        sendImageToModule(sendImageRequest);*/
    }

    private void handleOnline(JsonNode infoNode, Integer deviceId) {
        Camera cameraIp = cameraRepository.getCameraByChannel(deviceId);
        if (cameraIp != null) {
            cameraIp.setChannelId(deviceId);
            cameraIp.setStatus(Constants.CAMERA_STATE_TYPE.ACTIVE);
            cameraRepository.save(cameraIp);
        }
    }


    @Override
    public void sendImageToModule(SendImageRequest sendImageRequest) {
        AccessLog log = new AccessLog();
        log.setCameraId(sendImageRequest.getCameraId());
        log.setTimeStamp(sendImageRequest.getTime());
        log.setFaceImage(sendImageRequest.getPicture());
        log.setType(Constants.LOG_TYPE.DETECTING);
        accessLogRepository.save(log);

        FaceDetectRes faceDetectRes = new FaceDetectRes();
        faceDetectRes.setPicture(log.getFaceImage());
        faceDetectRes.setLogId(log.getId());

        socketController.getImageMessage(faceDetectRes);
    }

    @Override
    public void getResultCompare(FaceDetectRes res){
        CompletableFuture.supplyAsync(() -> {
            try {
                socketController.getImageMessage(res);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return null;
        });
    }

    @Override
    public void updateAllFace(){
        CompletableFuture.runAsync(() -> {
            try{
                if (serverInstance.isConnect()) {
                    synchronizeFace();
                    notifyService.addNotifyForSynchronize(Constants.NOTIFY_TITLE.SYNCHRONIZE_SUCCESS, "Data has been successfully synchronized to IVSS Server.");
                } else {
                    System.out.println(MessageCode.SERVER_CONNECT_FAIL);
                    notifyService.addNotifyForSynchronize(Constants.NOTIFY_TITLE.SYNCHRONIZE_FAIL, "Failed to synchronize data to IVSS Server due to connection issues.");
                }
                moduleController.callModelingApi();
                addAllUserTask();


            }catch (AmsException e){
                System.out.println(e.getMessage());
            }
            return;
        });
    }

    @Override
    public void addAllUserTask() throws AmsException {
        System.out.println("Add all user at " + LocalDateTime.now());
        if(mqttConnectionStatusService.isConnected()){
            try {
                System.out.println("Start Delete");
                mqttService.CreateDeleteFaceRequest();
                Thread.sleep(60 * 1000);
                System.out.println("Start Add");
                mqttService.CreateAddFaceRequest(Constants.PERSON_TYPE.BLACKLIST);
                System.out.println("End Add");
                notifyService.addNotifyForSynchronize(Constants.NOTIFY_TITLE.SYNCHRONIZE_SUCCESS, "Data has been successfully synchronized to LCD cameras.");
            } catch (InterruptedException | AmsException e) {
            }
        }else{
            notifyService.addNotifyForSynchronize(Constants.NOTIFY_TITLE.SYNCHRONIZE_FAIL, "Data synchronization to LCD cameras failed.");
        }

    }



    @Override
    public void processRoomEntryTask(Integer personType) throws AmsException {
        LocalDateTime time = LocalDateTime.now();
        TimeSlot timeSlot;

        if (Constants.PERSON_TYPE.WHITELIST.equals(personType)) {
            timeSlot = timeSlotRepository.findByStartTime(time);
        } else {
            timeSlot = timeSlotRepository.findByEndTime(time);
        }

        if (timeSlot == null) {
            throw new AmsException(MessageCode.TIMESLOT_NOT_FOUND);
        }

        List<Schedule> schedules = scheduleRepository.findScheduleByDateAndSlot(time.toLocalDate(), timeSlot.getId());

        for (Schedule schedule : schedules) {
            CompletableFuture.runAsync(() -> {
                try {
                    changeRoomEntry(schedule, personType);
                } catch (InterruptedException | AmsException e) {
                    System.out.println(e.getMessage());
                }
            });
        }

    }

    @Override
    public void sendChangeStateRequest(ChangeRequest request) throws AmsException {
        mqttService.CreateAddFaceRequest(request);
    }

    @Override
    public void hotAddUserRequest(Integer userId) throws AmsException {
        Users user = usersRepository.findById(userId).orElse(null);

        if(user == null || user.getStatus().equals(Constants.STATUS_TYPE.DELETED)){
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }
        if(serverInstance.isConnect()){
            editFace(user);
        }else{
            System.out.println(MessageCode.SERVER_CONNECT_FAIL);
        }

        List<IScheduleCamDto> deviceIds;
        Role role = roleRepository.findById(user.getRoleId()).orElse(null);
        if(role == null || role.getStatus().equals(Constants.STATUS_TYPE.DELETED)){
            throw new AmsException(MessageCode.ROLE_NOT_FOUND);
        }


        TimeSlot timeSlot = timeSlotRepository.findByStartTime(LocalDateTime.now());
        //if(timeSlot==null) throw new AmsException(MessageCode.TIMESLOT_NOT_FOUND);

        switch (role.getRoleName()){
            case Constants.ROLE.STUDENT:
                deviceIds = scheduleRepository.getDeviceForStudentSchedule(userId, LocalDate.now());
                break;
            case Constants.ROLE.TEACHER:
                deviceIds = scheduleRepository.getDeviceForTeacherSchedule(userId,LocalDate.now());
                break;
            default:{
                return;
            }
        }

        for (IScheduleCamDto sc: deviceIds
             ) {
            ChangeRequest changeRequest = new ChangeRequest();
            changeRequest.setUserId(userId);
            if(timeSlot != null && timeSlot.getId() == sc.getTimeSlot()){
                changeRequest.setPersonType(Constants.PERSON_TYPE.WHITELIST);
            }else{
                changeRequest.setPersonType(Constants.PERSON_TYPE.BLACKLIST);
            }
            changeRequest.setDevId(sc.getCameraId());
            sendChangeStateRequest(changeRequest);
        }
    }

    private void changeRoomEntry(Schedule schedule, Integer personType) throws AmsException, InterruptedException {
        if (schedule == null) throw new AmsException(MessageCode.SCHEDULE_NOT_FOUND);
        List<CameraDto> listCamera = cameraRepository.findLcdByRoom(schedule.getRoomId());
        if (listCamera.size() <= 0) throw new AmsException(MessageCode.CAMERA_NOT_FOUND);
        List<UserDto> facList = studentRepository.getStudentByClass(schedule.getClassId());
        UserDto teacher = teacherRepository.getTecherById(schedule.getUserId());
        if (teacher != null) {
            facList.add(teacher);
        } else {
            System.out.println("Teacher not found") ;
        }
        if (facList.size() <= 0) throw new AmsException(MessageCode.CLASS_NOT_FOUND);

        for (CameraDto camera : listCamera) {
            mqttService.CreateAddFaceRequest(camera.getChannelId(), facList, personType);
        }
    }


    //Tạo notify
    private void addNotifyForLogs(Integer logId) throws AmsException {
        // Lấy thông tin log, camera và room chỉ với một lần truy vấn
        AccessLog log = accessLogRepository.findById(logId).orElseThrow(() -> new AmsException("Log id " + logId + " not found"));

        Camera camera = cameraRepository.findById(log.getCameraId()).orElse(null);
        if(camera == null || camera.getStatus().equals(Constants.STATUS_TYPE.DELETED)){
            throw new AmsException("Camera id " + log.getCameraId() + " not found");
        }

        Room room = roomRepository.findById(camera.getRoomId()).orElse(null);
        if(camera == null || camera.getStatus().equals(Constants.STATUS_TYPE.DELETED)){
            throw new AmsException("Room id " + camera.getRoomId() + " not found");
        }
        String personName = determinePersonName(log);
        if (personName == null) {
            return;
        }

        String message = buildMessage(personName, camera.getCheckType(), room.getRoomName());
        if (message == null) {
            return;
        }

        Notify notify = createNotify(message, room, log.getTimeStamp());

        List<Integer> users = new ArrayList<>();
        users.addAll(usersRepository.findUserIdByRole(Constants.ROLE.ADMIN));
        users.addAll(usersRepository.findUserIdByRole(Constants.ROLE.STAFF));

        notifyService.AddNotify(notify, users);
    }

    private String determinePersonName(AccessLog log) throws AmsException {
        if (DataUtils.safeEqual(log.getType(), 0)) {
            return "Stranger";
        } else if (log.getType() == 2 || log.getUserId() != null) {
            Users user = usersRepository.findById(log.getUserId()).orElse(null);
            if(user == null || user.getStatus().equals(Constants.STATUS_TYPE.DELETED)){
                new AmsException("User id " + log.getUserId() + " not found");
            }
            return user.getLastName() + " " + user.getFirstName();
        }
        return null;
    }

    private String buildMessage(String personName, String checkType, String roomName) {
        if (checkType.equals("IN")) {
            return personName + " appeared inside room " + roomName;
        } else if (checkType.equals("OUT")) {
            return personName + " appeared outside room " + roomName;
        }
        return null;
    }

    private Notify createNotify(String message, Room room, LocalDateTime logTimeStamp) throws AmsException {
        Notify notify = new Notify();
        notify.setTitle(Constants.NOTIFY_TITLE.STRANGER + " " + room.getRoomName());
        notify.setContent(message);
        notify.setTime(LocalDateTime.now());
        notify.setDestinationPage(Constants.SCREEN_NAME.LOGS);

        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> pageParamsMap = new HashMap<>();
        pageParamsMap.put("roomId", room.getId().intValue()); // Thay đổi tùy thuộc vào cách bạn muốn lưu roomId
        pageParamsMap.put("notType", 1);
        pageParamsMap.put("date", dateTimeFormat.format(logTimeStamp.toLocalDate()));

        try {
            String pageParamsJson = objectMapper.writeValueAsString(pageParamsMap);
            notify.setPageParams(pageParamsJson);
        } catch (JsonProcessingException e) {
            throw new AmsException("Error converting page params to JSON: " + e.getMessage());
        }
        return notify;
    }
}
