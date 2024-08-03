package vn.attendance.service.user.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import vn.attendance.model.StudentCurriculum;
import vn.attendance.model.TeacherSubject;
import vn.attendance.model.Users;
import vn.attendance.repository.*;
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
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    FacialRepository facialsRepository;

    @Value("${active.link}")
    String activationLink;

    @Autowired
    StudentCurriculumRepository studentCurriculumRepository;
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    TeacherSubjectRepository teacherSubjectRepository;

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
    public AddUserRequest addUser(AddUserRequest request, int option) throws AmsException {
        //Lấy thông tin User
        int count = 0;
        Users users = BaseUserDetailsService.USER.get();
        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        // Kiểm tra vai trò người dùng
        if (users.getRoleId() != 1 &&
                (request.getRoleName().equalsIgnoreCase(Constants.ROLE.ADMIN) ||
                        request.getRoleName().equalsIgnoreCase(Constants.ROLE.STAFF))) {
            if (option == 1) {
                throw new AmsException(MessageCode.USER_NOT_AUTHORITY);
            }
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            request.setErrorMess(MessageCode.USER_NOT_AUTHORITY.getCode());
            return request;
        }

        List<Users> usersList = userRepository.findAll();

        //Kiểm tra User
        for (Users user : usersList) {
            if (user.getUsername().equals(request.getUsername())) {
                if (option == 1) {
                    throw new AmsException(MessageCode.USERNAME_ALREADY_EXISTS);
                }
                request.setStatus(Constants.REQUEST_STATUS.FAILED);
                request.setErrorMess(MessageCode.USERNAME_ALREADY_EXISTS.getCode());
                return request;
            }
            if (user.getPhone().equals(request.getPhoneNumber())) {
                if (option == 1) {
                    throw new AmsException(MessageCode.PHONE_ALREADY_EXISTS);
                }
                request.setStatus(Constants.REQUEST_STATUS.FAILED);
                request.setErrorMess(MessageCode.PHONE_ALREADY_EXISTS.getCode());
                return request;
            }
            if (user.getEmail().equals(request.getEmail())) {
                if (option == 1) {
                    throw new AmsException(MessageCode.EMAIL_ALREADY_EXISTS);
                }
                request.setStatus(Constants.REQUEST_STATUS.FAILED);
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
            user.setStatus(Constants.STATUS_TYPE.PENDING);
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
                request.setStatus(Constants.REQUEST_STATUS.FAILED);
                request.setErrorMess(MessageCode.ROLE_NOT_FOUND.getCode());
                return request;
            }
            user.setRoleId(role.getId());

            userRepository.save(user);
            request.setStatus(Constants.REQUEST_STATUS.SUCCESS);
            Users finalUser = user;
            CompletableFuture.supplyAsync(() -> {
                try {
                    sendMail(finalUser, token);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            });
        }
        return request;
    }

    private void sendMail(Users users, String token) throws MessagingException {
        //code gửi mail
        emailService.sendSimpleMessage(users.getEmail(), "WELCOME", users.getUsername(), "Active account AMS!",
                "We're excited to have you get started. First, you need to" +
                        " active your account. Just press the button below." +
                        " Please click the button to activate your account:",
                activationLink + token, 1);
//        if (users != null) {
//            Facial facial = new Facial();
//            facial.setUserId(users.getId());
//            facial.setCreatedAt(LocalDateTime.now());
//            facial.setCreatedBy(users.getId());
//            facial.setStatus(Constants.STATUS_TYPE.ACTIVE);
//            facialsRepository.save(facial);
//        }
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
        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }
        Users user = userRepository.findById(req.getId()).orElse(null);
        if (user == null || user.getStatus().equals(Constants.STATUS_TYPE.DELETED)) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }
        user.setStatus(req.getStatus());

        user.setModifiedAt(LocalDateTime.now());
        user.setModifiedBy(users.getId());
        userRepository.save(user);
    }

    @Override
    public String exportUser(String search, String roleName, String status,
                                           Integer gender) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();
        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        List<UsersDto> list = userRepository.findUsersByConditions(search, roleName, status, gender);

        try {
            // Chuyển đổi danh sách thành chuỗi JSON
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            String jsonString = objectMapper.writeValueAsString(list);

            // Mã hóa chuỗi JSON thành bytes UTF-8
            byte[] utf8Bytes = jsonString.getBytes(StandardCharsets.UTF_8);

            // Mã hóa bytes UTF-8 thành base64
            String base64Encoded = Base64.getEncoder().encodeToString(utf8Bytes);
            return base64Encoded;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editUser(EditUserRequest request) throws AmsException {
        //Lấy thông tin User
        Users users = BaseUserDetailsService.USER.get();
        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        Users user = userRepository.findById(request.getId()).orElse(null);
        if (user == null || user.getStatus().equals(Constants.STATUS_TYPE.DELETED)) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        // Kiểm tra quyền sửa
        if (users.getRoleId() != 1) {
            // Người dùng hiện tại không phải là Admin
            if (user.getRoleId() == 1 || user.getRoleId() == 2) {
                // Người dùng hiện tại không có quyền sửa Admin hoặc Staff
                throw new AmsException(MessageCode.USER_NOT_AUTHORITY);
            }

            if (request.getRoleName().equalsIgnoreCase(Constants.ROLE.ADMIN) ||
                    request.getRoleName().equalsIgnoreCase(Constants.ROLE.STAFF)) {
                // Người dùng hiện tại không có quyền nâng cấp vai trò lên Admin hoặc Staff
                throw new AmsException(MessageCode.USER_NOT_AUTHORITY);
            }
        } else {
            // Người dùng hiện tại là Admin
            if (user.getRoleId() == 1) {
                // Admin không được sửa các Admin khác
                throw new AmsException(MessageCode.USER_NOT_AUTHORITY);
            }

            if (request.getRoleName().equalsIgnoreCase(Constants.ROLE.ADMIN)) {
                // Admin không được nâng cấp vai trò từ Staff lên Admin
                throw new AmsException(MessageCode.USER_NOT_AUTHORITY);
            }
        }

        List<Users> usersList = userRepository.findAll();

        //Kiểm tra User
        for (Users u : usersList) {
            if (u.getUsername().equals(request.getUsername()) && !user.getUsername().equals(request.getUsername())) {
                throw new AmsException(MessageCode.USERNAME_ALREADY_EXISTS);
            }
            if (u.getPhone().equals(request.getPhoneNumber()) && !user.getPhone().equals(request.getPhoneNumber())) {
                throw new AmsException(MessageCode.PHONE_ALREADY_EXISTS);
            }
            if (u.getEmail().equals(request.getEmail()) && !user.getEmail().equals(request.getEmail())) {
                throw new AmsException(MessageCode.EMAIL_ALREADY_EXISTS);
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
        userRepository.save(user);
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

//    private void addImageToSheet(Sheet sheet, byte[] imageBytes, int rowNumber, int columnNumber, Workbook workbook) throws IOException {
//        int pictureIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);
//        CreationHelper helper = workbook.getCreationHelper();
//        Drawing<?> drawing = sheet.createDrawingPatriarch();
//
//        ClientAnchor anchor = helper.createClientAnchor();
//        anchor.setCol1(columnNumber);
//        anchor.setRow1(rowNumber);
//        anchor.setCol2(columnNumber + 1);
//        anchor.setRow2(rowNumber + 1);
//
//        HSSFPicture picture = (HSSFPicture) drawing.createPicture(anchor, pictureIdx);
//        picture.resize(1.0); // Resize image to fit the cell
//    }

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
            if (role == null || role.getStatus().equals(Constants.STATUS_TYPE.DELETED))
                throw new AmsException(MessageCode.ROLE_NOT_FOUND);

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
    public Page<UserDto> searchUserBlackList(String search, LocalDate date, String roleName, int page, int size) throws AmsException {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<IUserDto> usersDtoPage = userRepository.findUsersBlackList(search, date, roleName, pageable);

        return usersDtoPage.map(UserDto::new);
    }

    @Override
    public List<AddUserRequest> importUsers(List<AddUserRequest> usersList) throws AmsException {
        List<AddUserRequest> addUserRequests = new ArrayList<>();
        for (AddUserRequest user : usersList) {
            addUserRequests.add(addUser(user,2));
        }
        return addUserRequests;
    }

    @Override
    public Users deleteUser(Integer id) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();
        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }
        Users user = userRepository.findById(id).orElseThrow(null);
        if (user == null || user.getStatus().equals(Constants.STATUS_TYPE.DELETED))
            throw new AmsException(MessageCode.USER_NOT_FOUND);


        switch (user.getRoleId()){
            case 1:{
                throw new AmsException(MessageCode.USER_NOT_AUTHORITY);
            }
            case 2:{
                if(users.getRoleId().equals(user.getRoleId())) throw new AmsException(MessageCode.USER_NOT_AUTHORITY);
                break;
            }
            case 3:{
                if(scheduleRepository.countScheduleByTeacher(id, LocalDate.now())>0) throw new AmsException(MessageCode.SCHEDULES_EXIST_FOR_TEACHER);
                List<TeacherSubject> teacherSubjects = teacherSubjectRepository.findByTeacher(id);
                for (TeacherSubject teacherSubject: teacherSubjects
                     ) {
                    teacherSubject.setStatus(Constants.STATUS_TYPE.DELETED);
                    teacherSubject.setModifiedAt(LocalDateTime.now());
                    teacherSubject.setModifiedBy(user.getId());
                    teacherSubjectRepository.save(teacherSubject);
                }
                break;
            }
            case 4:{
                StudentCurriculum studentCurriculum = studentCurriculumRepository.findByStudentId(id);
                if (studentCurriculum!=null){
                    studentCurriculum.setStatus(Constants.STATUS_TYPE.DELETED);
                    studentCurriculum.setModifiedAt(LocalDateTime.now());
                    studentCurriculum.setModifiedBy(user.getId());
                    studentCurriculumRepository.save(studentCurriculum);
                }
                break;
            }
            default:{

            }
        }

        user.setStatus(Constants.STATUS_TYPE.DELETED);
        user.setModifiedAt(LocalDateTime.now());
        user.setModifiedBy(users.getId());
        userRepository.save(user);
        return user;
    }

    @Override
    public void sendActiveMail(String email) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();
        if (users == null) throw  new AmsException(MessageCode.USER_NOT_FOUND);
        Users user = userRepository.findByEmail(email).orElseThrow(()->new AmsException(MessageCode.USER_NOT_FOUND));
        String token = tokenProvider.createTokenForNewUser(user.getUsername(), DataUtils.randomPassword());
        if(StringUtils.hasText(token))    {
            try {
                sendMail(users,token);
            }catch (MessagingException e) {
                throw new AmsException(MessageCode.SEND_EMAIL_FAIL);
            }
        }else{
            throw new AmsException(MessageCode.SEND_EMAIL_FAIL);
        }
    }

}
