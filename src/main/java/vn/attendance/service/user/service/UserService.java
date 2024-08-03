package vn.attendance.service.user.service;

import org.springframework.data.domain.Page;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Users;
import vn.attendance.service.user.entity.res.Res;
import vn.attendance.service.user.service.request.*;
import vn.attendance.service.user.service.response.GetUserByTokenRes;
import vn.attendance.service.user.service.response.UsersDto;

import javax.mail.MessagingException;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;


public interface UserService {

    Users findByUsername(String username);

    void sendOtp() throws MessagingException;

    void resetPassword(ResetPasswordRequest request) throws AmsException;

    void sendOtpToResetPassword(String email) throws AmsException, MessagingException;

    Users changePassword(ChangePasswordRequest request) throws AmsException;

    Res activeUser(ActiveUserRequest request) throws AmsException;

    Page<UsersDto> searchUser(String search, String roleName, String status, Integer gender, int page, int size);

    Page<UserDto> searchUserBlackList(String search, LocalDate date, String roleName,
                                       int page, int size) throws AmsException;

    AddUserRequest addUser(AddUserRequest request, int option) throws AmsException;

    GetUserByTokenRes getUserByToken(String token) throws AmsException ;

    void changeStatus(ChangeStatusRequest req) throws AmsException;

    String exportUser(String search, String roleName, String status,
                                    Integer gender) throws AmsException;

    void editUser(EditUserRequest request) throws AmsException;

    void setNewPassword(String userName, String newPass) throws AmsException;

    Users findUserById(Integer id);

    List<AddUserRequest> importUsers(List<AddUserRequest> usersList) throws AmsException;

    Users deleteUser(Integer id) throws AmsException;

    void sendActiveMail(String email) throws AmsException;
}
