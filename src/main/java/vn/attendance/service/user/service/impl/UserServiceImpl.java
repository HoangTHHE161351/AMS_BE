package vn.attendance.service.user.service.impl;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.config.authen.TokenProvider;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Role;
import vn.attendance.model.Users;
import vn.attendance.repository.RoleRepository;
import vn.attendance.repository.UsersRepository;
import vn.attendance.service.server.ServerInstance;
import vn.attendance.service.server.service.FaceRecognitionService;
import vn.attendance.service.user.entity.res.Res;
import vn.attendance.service.user.entity.res.UserLoginRes;
import vn.attendance.service.user.service.EmailService;
import vn.attendance.service.user.service.UserService;
import vn.attendance.service.user.service.request.*;
import vn.attendance.service.user.service.response.GetUserByTokenRes;
import vn.attendance.service.user.service.response.UsersDto;
import vn.attendance.util.Constants;
import vn.attendance.util.DataUtils;
import vn.attendance.util.MessageCode;

import javax.mail.MessagingException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UsersRepository userRepository;
    @Value("${security.otp.time-expire}")
    private Integer otpExpire;
    @Autowired
    EmailService emailService;
    @Autowired
    BCryptPasswordEncoder encoder;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    RoleRepository rolesRepository;

    String activationLink = "http://localhost:3000/active-account?token=";
    @Autowired
    ServerInstance serverInstance;
    @Autowired
    FaceRecognitionService faceRecognitionService;

    @Override
    public Users findByUsername(String username) {
        return getData(username);
    }

    private Users getData(String username) {
        Optional<Users> user = userRepository.findByUsername(username);
        return user.isPresent() ? user.get() : null;
    }

    @Override
    public void sendOtp() throws MessagingException {
        Users users = BaseUserDetailsService.USER.get();
        users.setOtp(DataUtils.randomOtp());
        users.setOtpExpire(LocalDateTime.now().plusMinutes(otpExpire));
        emailService.sendSimpleMessage(users.getEmail(), "OTP Change Password!", users.getUsername(),
                "OTP Change Password!", "Your Otp is : ", users.getOtp(), 2);
        userRepository.save(users);
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) throws AmsException {
        Users users = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new AmsException(MessageCode.USER_NOT_FOUND));
        if (DataUtils.safeEqual(users.getOtp(), request.getOtp())
                && users.getOtpExpire() != null && users.getOtpExpire().isAfter(LocalDateTime.now())) {
            users.setPassword(encoder.encode(request.getPassword()));
            users.setAccessToken(null);
            users.setTokenExpire(null);
            userRepository.save(users);
        } else throw new AmsException(MessageCode.OTP_EXPIRED);
    }

    @Override
    public void sendOtpToResetPassword(String email) throws AmsException, MessagingException {
        Users users = userRepository.findByEmail(email).orElseThrow(() -> new AmsException(MessageCode.USER_NOT_FOUND));
        users.setOtp(DataUtils.randomOtp());
        users.setOtpExpire(LocalDateTime.now().plusMinutes(otpExpire));
        emailService.sendSimpleMessage(users.getEmail(), "OTP Reset Password!", users.getUsername(),
                "OTP Reset Password!", "Your Otp is : ", users.getOtp(), 2);
        userRepository.save(users);
    }

    @Override
    public Users changePassword(ChangePasswordRequest request) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();
        if (!encoder.matches(request.getOldPassword(), users.getPassword()))
            throw new AmsException(MessageCode.OLD_PASSWORD_NOT_MATCH);
        if (DataUtils.safeEqual(request.getOtp(), users.getOtp()) && users.getOtpExpire().isBefore(LocalDateTime.now()))
            throw new AmsException(MessageCode.OTP_EXPIRED);
        users.setPassword(encoder.encode(request.getNewPassword()));
        return userRepository.save(users);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AddUserRequest addUser(AddUserRequest request) throws AmsException {
        //Lấy thông tin User
        int count = 0;
        Users users = BaseUserDetailsService.USER.get();
        if (users == null) {
            request.setStatus("FAILED");
            request.setErrorMess(MessageCode.USER_NOT_FOUND.getCode());
            return request;
        }
        List<Users> usersList = userRepository.findAll();

        //Kiểm tra User
        for (Users user : usersList) {
            if (user.getUsername().equals(request.getUsername())) {
                request.setStatus("FAILED");
                request.setErrorMess(MessageCode.USERNAME_ALREADY_EXISTS.getCode());
                return request;
            }
            if (user.getPhone().equals(request.getPhoneNumber())) {
                request.setStatus("FAILED");
                request.setErrorMess(MessageCode.PHONE_ALREADY_EXISTS.getCode());
                return request;
            }
            if (user.getEmail().equals(request.getEmail())) {
                request.setStatus("FAILED");
                request.setErrorMess(MessageCode.EMAIL_ALREADY_EXISTS.getCode());
                return request;
            }
        }

        //Tạo User mới

        String password = DataUtils.randomPassword();
        String token = tokenProvider.createTokenForNewUser(request.getUsername(), password);
        if (StringUtils.hasText(token) && count == 0) {
            Users user = new Users();
            user.setUsername(request.getUsername());
            user.setPassword(encoder.encode(password));
            user.setCreatedBy(users.getId());
            user.setCreatedAt(DataUtils.getLocalDateTime());
            user.setStatus(Constants.STATUS_TYPE.PENDING_ACTIVATION);
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setDob(LocalDate.parse(request.getDob()));
            user.setGender(request.getSex().intValue());
            user.setEmail(request.getEmail());
            user.setPhone(request.getPhoneNumber());
            user.setAddress(request.getAddress());
            user.setAvata(request.getAvatar());
            user.setAccessToken(token);
            Role role;
            try {
                role = rolesRepository.findByRoleName(request.getRoleName().toUpperCase())
                        .orElseThrow(() -> new AmsException(MessageCode.ROLE_NOT_FOUND));
            } catch (AmsException e) {
                request.setStatus("FAILED");
                request.setErrorMess(MessageCode.ROLE_NOT_FOUND.getCode());
                return request;
            }
            user.setRoleId(role.getId());

            if(serverInstance.getLoginHandle().longValue()!=0){
                user = faceRecognitionService.CreateFace(user);
                if(user == null){
                    request.setStatus("FAILED");
                    request.setErrorMess(MessageCode.ADD_USER_FAIL.getCode());
                    return request;
                }
            }

            userRepository.save(user);
            request.setStatus("SUCCESS");
            Users finalUser = user;
            CompletableFuture.supplyAsync(() -> {
                try {
                    //code gửi mail
                    emailService.sendSimpleMessage(finalUser.getEmail(), "WELCOME", finalUser.getUsername(), "Active account AMS!",
                            "We're excited to have you get started. First, you need to" +
                                    " active your account. Just press the button below." +
                                    " Please click the button to activate your account:",
                            activationLink + token, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            });
        }
        return request;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GetUserByTokenRes getUserByToken(String token) throws AmsException {
        Users users = userRepository.findByToken(token).orElseThrow(() -> new AmsException(MessageCode.USER_NOT_FOUND));
        GetUserByTokenRes res = new GetUserByTokenRes();
        res.setFullName(users.getLastName() + " " + users.getFirstName());
        res.setEmail(users.getEmail());
        return res;
    }

    @Override
    public void changeStatus(ChangeStatusRequest req) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();
        if(users == null){
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }
        Users user = userRepository.findById(req.getId()).orElseThrow(() -> new AmsException(MessageCode.USER_NOT_FOUND));
        String oldStatus = user.getStatus();
        user.setStatus(req.getStatus());

        if(serverInstance.getLoginHandle().longValue()!=0 && oldStatus.equals(Constants.STATUS_TYPE.DELETED) && !req.getStatus().equals(Constants.STATUS_TYPE.DELETED)){
            user = faceRecognitionService.CreateFace(user);
            if(user == null){
                throw new AmsException(MessageCode.DELETE_USER_FACE_FAIL);
            }
        }

/*        if(serverInstance.getLoginHandle().longValue()!=0 &&  req.getStatus().equals(Constants.STATUS_TYPE.DELETED) && user.getFaceId() != null){
            if(faceRecognitionService.DeleteFace(user)){
                user.setFaceId(null);
            }else throw new AmsException(MessageCode.DELETE_USER_FACE_FAIL);
        }*/

        user.setModifiedAt(LocalDateTime.now());
        user.setModifiedBy(users.getId());
        userRepository.save(user);
    }

    @Override
    public ByteArrayInputStream exportUser(String search, String roleName, String status,
                                           Integer gender) throws AmsException {
        HSSFWorkbook workbook = new HSSFWorkbook();

        try {
            List<UsersDto> users = userRepository.findUsersByConditions(search, roleName, status, gender);
            if (users.size() <= 0) {
                throw new AmsException(MessageCode.CONTENT_EMPTY);
            }
            Sheet sheet = workbook.createSheet("User List");
            // Tạo tiêu đề cho các cột
            Row headerRow = sheet.createRow(0);

            // Create headers
            String[] headers = {"No", "User Name", "Full Name", "Email", "Avatar", "Gender", "Address", "Phone", "Date of Birth", "Role", "Status", "Created By", "Created At", "Modified By", "Modified At"};
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            int rowNum = 1;
            for (UsersDto user : users) {
                Row dataRow = sheet.createRow(rowNum++);

                dataRow.createCell(0).setCellValue(rowNum - 1);
                dataRow.createCell(1).setCellValue(user.getUsername());
                dataRow.createCell(2).setCellValue(user.getLastName() + " " + user.getFirstName());
                dataRow.createCell(3).setCellValue(user.getEmail());

                // Handle avatar image
                if (user.getAvata() != null) {
                    try {
                        byte[] imageBytes = Base64.getDecoder().decode(user.getAvata());
                        addImageToSheet(sheet, imageBytes, rowNum - 1, 4, workbook);
                    } catch (IllegalArgumentException | IOException e) {
                        dataRow.createCell(4).setCellValue("N/A");
                    }
                } else {
                    dataRow.createCell(4).setCellValue("N/A");
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                // Check and handle null values
//                dataRow.createCell(5).setCellValue(user.getGender() == 1 ? "MALE" : "FEMALE");
                dataRow.createCell(5).setCellValue(user.getGender());
                dataRow.createCell(6).setCellValue(Objects.requireNonNullElse(user.getAddress(), "N/A"));
                dataRow.createCell(7).setCellValue(Objects.requireNonNullElse(user.getPhone(), "N/A"));
                dataRow.createCell(8).setCellValue(user.getDob() != null ? user.getDob().format(formatter) : "N/A");
                dataRow.createCell(9).setCellValue(user.getRoleName());
                dataRow.createCell(10).setCellValue(Objects.requireNonNullElse(user.getStatus(), "N/A"));
                dataRow.createCell(11).setCellValue(user.getCreatedBy() != null ? user.getCreatedBy() : "N/A");
                dataRow.createCell(12).setCellValue(user.getCreatedAt() != null ? user.getCreatedAt().format(formatter) : "N/A");
                dataRow.createCell(13).setCellValue(user.getModifiedBy() != null ? user.getModifiedBy() : "N/A");
                dataRow.createCell(14).setCellValue(user.getModifiedAt() != null ? user.getModifiedAt().format(formatter) : "N/A");
            }


            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            workbook.close();

//            byte[] fileContent = out.toByteArray();

            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new AmsException(MessageCode.EXPORT_FAIL);
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                throw new AmsException(MessageCode.EXPORT_FAIL);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EditUserRequest editUser(EditUserRequest request) throws AmsException {
        //Lấy thông tin User
        Users users = BaseUserDetailsService.USER.get();
        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        Users user = findUserById(request.getId());
        if (user == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        List<Users> usersList = userRepository.findAll();

        //Kiểm tra User
        for (Users u : usersList) {
            if (u.getUsername().equals(request.getUsername()) && !users.getUsername().equals(request.getUsername())) {
                request.setStatus("FAILED");
                request.setErrorMess(MessageCode.USERNAME_ALREADY_EXISTS.getCode());
                return request;
            }
            if (u.getPhone().equals(request.getPhoneNumber()) && !users.getPhone().equals(request.getPhoneNumber())) {
                request.setStatus("FAILED");
                request.setErrorMess(MessageCode.PHONE_ALREADY_EXISTS.getCode());
                return request;
            }
            if (u.getEmail().equals(request.getEmail()) && !users.getEmail().equals(request.getEmail())) {
                request.setStatus("FAILED");
                request.setErrorMess(MessageCode.EMAIL_ALREADY_EXISTS.getCode());
                return request;
            }
        }

        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDob(LocalDate.parse(request.getDob()));
        user.setGender(request.getSex().intValue());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        user.setAvata(request.getAvatar());
        user.setModifiedBy(user.getId());
        user.setModifiedAt(LocalDateTime.now());
        Role role = rolesRepository.findByRoleName(request.getRoleName().toUpperCase())
                .orElseThrow(() -> new AmsException(MessageCode.ROLE_NOT_FOUND));
        user.setRoleId(role.getId());
        if(serverInstance.getLoginHandle().longValue()!=0){
            user = faceRecognitionService.EditFace(user);
            if(user == null){
                throw new AmsException(MessageCode.UPDATE_USER_FACE_FAIL);
            }
        }
        userRepository.save(user);
        request.setStatus("SUCCESS");
        return request;
    }

    @Override
    public Users findUserById(Integer id) {
        Optional<Users> user = userRepository.findUsersById(id);
        return user.isPresent() ? user.get() : null;
    }

    @Override
    public void setNewPassword(String userName, String newPass) throws AmsException {
        Users user = findByUsername(userName);
        if (user == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }
        user.setPassword(encoder.encode(newPass));
        userRepository.save(user);
    }

    private void addImageToSheet(Sheet sheet, byte[] imageBytes, int rowNumber, int columnNumber, Workbook workbook) throws IOException {
        int pictureIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);
        CreationHelper helper = workbook.getCreationHelper();
        Drawing<?> drawing = sheet.createDrawingPatriarch();

        ClientAnchor anchor = helper.createClientAnchor();
        anchor.setCol1(columnNumber);
        anchor.setRow1(rowNumber);
        anchor.setCol2(columnNumber + 1);
        anchor.setRow2(rowNumber + 1);

        HSSFPicture picture = (HSSFPicture) drawing.createPicture(anchor, pictureIdx);
        picture.resize(1.0); // Resize image to fit the cell
    }

    @Override
    public Res activeUser(ActiveUserRequest request) throws AmsException {
        Users user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new AmsException(MessageCode.USER_NOT_FOUND));
        user.setPassword(encoder.encode(request.getPassword()));
        user.setStatus(Constants.STATUS_TYPE.ACTIVE);
        userRepository.save(user);

        String token = tokenProvider.createToken(user.getUsername(), request.getPassword());
        if (StringUtils.hasText(token)) {
            UserLoginRes res = new UserLoginRes();
            res.setToken(token);
            res.setUsername(tokenProvider.getUsername(token));
            res.setUser(user);
            user.setAccessToken(token);
            user.setTokenExpire(LocalDateTime.now());

            Authentication authen = tokenProvider.getAuthentication(token);
            Role role = rolesRepository.findById(user.getRoleId())
                    .orElseThrow(() -> new AmsException(MessageCode.ROLE_NOT_FOUND));
            res.setRoles(List.of(role.getRoleName()));

            userRepository.save(user);
            return new Res(Res.RESPONSE_SUCCESS, res);
        } else {
            throw new AmsException(MessageCode.LOGIN_FAIL);
        }
    }

    @Override
    public Page<UsersDto> searchUser(String search, String roleName, String status, Integer gender, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return userRepository.searchUser(search, roleName, status, gender, pageable);
    }

    @Override
    public Page<UserDto> searchUserBlackList(String search, LocalDate date, String roleName, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<IUserDto> usersDtoPage = userRepository.findUsersBlackList(search, date, roleName, pageable);

        return usersDtoPage.map(UserDto::new);
    }

    @Override
    public List<AddUserRequest> importUsers(List<AddUserRequest> usersList) throws AmsException {
        List<AddUserRequest> addUserRequests = new ArrayList<>();
        for (AddUserRequest user : usersList) {
            addUserRequests.add(addUser(user));
        }
        return addUserRequests;
    }

    @Override
    public Users deleteUser(Integer id) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();
        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }
        if (users.getRoleId() != 1 && users.getRoleId() != 2) {
            throw new AmsException(MessageCode.USER_NOT_AUTHORITY);
        }
        Users user = userRepository.getById(id);

        if (user == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        } else {
            user.setStatus(Constants.STATUS_TYPE.DELETED);
            user.setModifiedAt(LocalDateTime.now());
            user.setModifiedBy(users.getId());
            userRepository.save(user);
        }

        return user;
    }

}
