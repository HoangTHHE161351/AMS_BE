package vn.attendance.service.user.facade.impl;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import vn.attendance.config.JwtConfig;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.config.authen.TokenProvider;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Role;
import vn.attendance.model.Users;
import vn.attendance.repository.RoleRepository;
import vn.attendance.repository.UsersRepository;
import vn.attendance.service.user.entity.req.LoginRequest;
import vn.attendance.service.user.entity.req.UserLoginReq;
import vn.attendance.service.user.entity.res.Res;
import vn.attendance.service.user.entity.res.UserLoginRes;
import vn.attendance.service.user.facade.LoginFacade;
import vn.attendance.service.user.service.UserService;
import vn.attendance.service.user.service.request.EditUserRequest;
import vn.attendance.util.Constants;
import vn.attendance.util.DataUtils;
import vn.attendance.util.MessageCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class LoginFacadeImpl implements LoginFacade {

    @Autowired
    UsersRepository usersRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    @Qualifier("jwtConfigAuth")
    private JwtConfig jwtConfig;
    @Autowired
    private UserService userService;
    @Autowired
    @Qualifier("jasyptStringEncryptor")
    private StringEncryptor encryptor;
    @Autowired
    RoleRepository rolesRepository;


    @Override
    public Res loginUser(LoginRequest req) throws Exception {
        Optional<Users> user = usersRepository.findByUsernameOrEmail(req.getUsername());
        if (!user.isPresent())
            throw new Exception("The login account is incorrect, please try again!");
        else if (!DataUtils.safeEqual(user.get().getStatus(), Constants.STATUS_TYPE.ACTIVE))
            throw new Exception("Account disabled !");

        String token = tokenProvider.createToken(user.get().getUsername(), req.getPassword());
        if (StringUtils.hasText(token)) {
            UserLoginRes res = new UserLoginRes();
            res.setToken(token);
            res.setUsername(tokenProvider.getUsername(token));
            res.setUser(user.get());
            user.get().setAccessToken(token);
            user.get().setTokenExpire(LocalDateTime.now());

            Authentication authen = tokenProvider.getAuthentication(token);
            Role role = rolesRepository.findById(user.get().getRoleId())
                    .orElse(null);
            if (role == null || role.getStatus().equals(Constants.STATUS_TYPE.DELETED)) {
                throw new AmsException(MessageCode.ROLE_NOT_FOUND);
            }
            res.setRoles(List.of(role.getRoleName()));

            // get user
            Users userDB = userService.findByUsername(res.getUsername());
            // todo: refresh token
            usersRepository.save(userDB);
            return new Res(Res.RESPONSE_SUCCESS, res);
        } else {
            throw new AmsException(MessageCode.LOGIN_FAIL);
        }
    }

    @Override
    public Res getProfileUser(Map<String, String> headers) {
        String token = headers.get(jwtConfig.getHeader().toLowerCase()).replace(jwtConfig.getPrefix(), "");
        String username = tokenProvider.getUsername(token);
        Users user = userService.findByUsername(username);
        return new Res(Res.RESPONSE_SUCCESS, user);
    }

    /**
     * function to register user
     *
     * @param userLoginReq
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Users registerUser(UserLoginReq userLoginReq) throws Exception {
        Optional<Users> user1 = usersRepository.findByUsername(userLoginReq.getUsername());
        if (user1.isPresent())
            throw new Exception("Username " + userLoginReq.getUsername() + " exists, cannot be created!");
        user1 = usersRepository.findByEmail(userLoginReq.getEmail());
        if (user1.isPresent())
            throw new Exception("Email " + userLoginReq.getEmail() + " exists, cannot be created!");

        Users user = new Users();
        user.setUsername(userLoginReq.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userLoginReq.getPassword()));
        user.setCreatedBy(null);
        user.setCreatedAt(DataUtils.getLocalDateTime());
        user.setStatus(Constants.STATUS_TYPE.ACTIVE);
        user.setFirstName(userLoginReq.getFirstName());
        user.setLastName(userLoginReq.getLastName());
        user.setDob(LocalDate.parse(userLoginReq.getDob()));
        user.setGender(userLoginReq.getSex().intValue());
        user.setEmail(userLoginReq.getEmail());
        user.setPhone(userLoginReq.getPhoneNumber());
        user.setAddress(userLoginReq.getAddress());

        Role role = rolesRepository.findByRoleName(userLoginReq.getRoleName().toUpperCase())
                .orElseThrow(() -> new AmsException(MessageCode.ROLE_NOT_FOUND));
        user.setRoleId(role.getId());
        return usersRepository.save(user);
    }

    @Override
    public void editProfileUser(EditUserRequest request) throws AmsException {
        //Lấy thông tin User
        Users user = BaseUserDetailsService.USER.get();
        if (user == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        List<Users> usersList = usersRepository.findAll();

        //Kiểm tra User
        for (Users u : usersList) {
            if (u.getUsername().equals(request.getUsername()) && !u.getUsername().equals(request.getUsername())) {
                throw new AmsException(MessageCode.USERNAME_ALREADY_EXISTS);
            }
            if (u.getPhone().equals(request.getPhoneNumber()) && !u.getPhone().equals(request.getPhoneNumber())) {
                throw new AmsException(MessageCode.PHONE_ALREADY_EXISTS);
            }
            if (u.getEmail().equals(request.getEmail()) && !u.getEmail().equals(request.getEmail())) {
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

        usersRepository.save(user);
    }

}
