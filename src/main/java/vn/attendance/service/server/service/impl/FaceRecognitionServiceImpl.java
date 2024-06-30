package vn.attendance.service.server.service.impl;

import com.sun.jna.Memory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.attendance.exception.AmsException;
import vn.attendance.lib.NetSDKLib.*;
import vn.attendance.lib.ToolKits;
import vn.attendance.model.Role;
import vn.attendance.model.Users;
import vn.attendance.repository.RoleRepository;
import vn.attendance.repository.UsersRepository;
import vn.attendance.service.server.ServerInstance;
import vn.attendance.service.server.response.IFaceDto;
import vn.attendance.service.server.service.FaceRecognitionService;
import vn.attendance.util.AvataUtils;
import vn.attendance.util.Constants;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Service
public class FaceRecognitionServiceImpl implements FaceRecognitionService {
    @Autowired
    ServerInstance serverInstance;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UsersRepository usersRepository;

    /*    private static final String encode = "GBK";		// win*/

    private static final String encode = "UTF-8"; 	// linux

    @Override
    public void SynchronizeFace(){
        if(serverInstance.getLoginHandle().longValue()==0){
            return;
        }
        System.out.println("Start SynchronizeFace");

        var roles = SynchronizeGroup();

        for (Role role: roles
        ) {
            SynchronizeUser(role.getRoleName(), role.getGroupId());
        }

        System.out.println("End SynchronizeFace");
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

        List<Users> users = usersRepository.findUserByRole(roleName);
        HashMap<String,String> faces = findFaceRecognitionDB(groupId);

        for (var user: users
        ) {
            var username = user.getUsername();
            try {
                AvataUtils.AvataInfo avataInfo = AvataUtils.decodeBase64AndExtractInfo(user.getAvata());
                LocalDate dob = user.getDob();
                String[] dobArray = {String.valueOf(dob.getYear()), String.valueOf(dob.getMonthValue()), String.valueOf(dob.getDayOfMonth())};

                if(faces.containsKey(username)){
                    if(user.getStatus()!= Constants.STATUS_TYPE.DELETED){
                        modifyFaceRecognitionDB(groupId,
                                faces.get(username),
                                avataInfo.getPictureLeng(),
                                avataInfo.getWidth(),
                                avataInfo.getHeight(),
                                avataInfo.getMemory(),
                                user.getLastName()+" "+user.getFirstName(),
                                user.getGender().byteValue(),
                                dobArray,
                                user.getAddress(),
                                user.getAddress(),
                                username,
                                (byte) EM_CERTIFICATE_TYPE.CERTIFICATE_TYPE_UNKNOWN
                        );
                    }else{
                        delFaceRecognitionDB(groupId, faces.get(username));
                    }

                }else{
                    String id = addFaceRecognitionDB(groupId,
                            avataInfo.getPictureLeng(),
                            avataInfo.getWidth(),
                            avataInfo.getHeight(),
                            avataInfo.getMemory(),
                            user.getLastName()+" "+user.getFirstName(),
                            user.getGender()==1?(byte) 1:(byte) 2,
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
                user.setFaceId(faces.get(username));
                usersRepository.save(user);
            }catch (Exception ex){
                System.err.println("Can't convert image");;
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

    //Thêm group vào server
    public String addFaceRecognitionGroup(String groupName) {
        if(groupName.equals("") || groupName == null) {
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
            var decodeName = new String(addGroupInfo.stuGroupInfo.szGroupName,encode).trim();
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

            NET_OUT_DOFIND_FACERECONGNITION stFindOut = new NET_OUT_DOFIND_FACERECONGNITION();;
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
                    System.out.printf("No more records, search ended!\n");
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

    /**
     * 删除人员信息(即删除人脸)
     * @param groupId 人脸库ID
     * @param sUID  人员唯一标识符
     */
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
    public Page<IFaceDto> getListFace(String search, String roleName, String status, Integer gender, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        return usersRepository.GetListFace(search, roleName, status, gender, pageable);
    }

    @Override
    public Users CreateFace(Users user) throws AmsException {
        try {
            Role role = roleRepository.getById(user.getRoleId());
            AvataUtils.AvataInfo avataInfo = AvataUtils.decodeBase64AndExtractInfo(user.getAvata());
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
    public Users EditFace(Users user) throws AmsException{
        try {
            if(user.getFaceId()==null){
                return CreateFace(user);
            }
            Role role = roleRepository.getById(user.getRoleId());
            AvataUtils.AvataInfo avataInfo = AvataUtils.decodeBase64AndExtractInfo(user.getAvata());
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

    @Override
    public boolean DeleteFace(Users user) throws AmsException{
        Role role = roleRepository.getById(user.getRoleId());
        if(!delFaceRecognitionDB(role.getGroupId(), user.getFaceId())){
            return false;
        }
        return true;
    }
}
