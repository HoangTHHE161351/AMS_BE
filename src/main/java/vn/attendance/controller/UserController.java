package vn.attendance.controller;

import io.micrometer.core.lang.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import vn.attendance.exception.AmsException;
import vn.attendance.helper.ApiResponse;
import vn.attendance.model.Users;
import vn.attendance.service.user.service.EmailService;
import vn.attendance.service.user.service.UserService;
import vn.attendance.service.user.service.request.*;
import vn.attendance.service.user.service.response.UsersDto;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("api/v1/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    EmailService emailService;

    @GetMapping("send-otp-change-password")
    public ApiResponse<String> sendOtp() throws MessagingException {
        userService.sendOtp();
        return ApiResponse.okStatus("Send Otp Success!");
    }

    @GetMapping("send-otp-reset-password")
    public ApiResponse<String> sendOtp(@RequestParam(required = true) String email) throws AmsException, MessagingException {
        userService.sendOtpToResetPassword(email);
        return ApiResponse.okStatus("Send Otp Success!");
    }

    @PutMapping("reset-password")
    public ApiResponse<?> resetPassword(@RequestBody @Valid ResetPasswordRequest request) throws AmsException {
        userService.resetPassword(request);
        return ApiResponse.okStatus("Reset Success!");
    }

    @PutMapping("change-password")
    public ApiResponse<?> changePassword(@RequestBody @Valid ChangePasswordRequest request) throws AmsException {
        return ApiResponse.okStatus(userService.changePassword(request));
    }

    @GetMapping("all-users")
    public ApiResponse<Page<UsersDto>> searchUser(@RequestParam(required = false) String search,
                                                  @RequestParam(required = false) String status,
                                                  @RequestParam(required = false) String roleName,
                                                  @RequestParam(required = false) Integer gender,
                                                  @RequestParam(defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "10") int size) {

        return ApiResponse.okStatus(userService.searchUser(search, status, roleName, gender, page, size));
    }

    @PutMapping("active-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<?> activeUser(@RequestBody @Valid ActiveUserRequest request) throws AmsException {
        userService.activeUser(request);
        return ApiResponse.okStatus("Active User Success!");
    }

    @PostMapping("add-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<?> addUser(@RequestBody @Valid AddUserRequest request) throws AmsException {
        userService.addUser(request);
        return ApiResponse.okStatus("Add User Success!");
    }

    @GetMapping("get-user-by-token")
    public ApiResponse<?> getUserByToken(@RequestParam @Valid String token) throws AmsException {
        return ApiResponse.okStatus(userService.getUserByToken(token));
    }

    @PutMapping("change-status")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<?> changeStatus(@RequestBody @Valid ChangeStatusRequest req) throws AmsException {
        userService.changeStatus(req);
        return ApiResponse.okStatus("Change User Status Success!");
    }

    @GetMapping("/export-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Resource> exportExcel(@RequestParam(required = false) String search,
                                                @RequestParam(required = false) String roleName,
                                                @RequestParam(required = false) String status,
                                                @RequestParam(required = false) Integer gender) throws AmsException
    {
        InputStreamResource file = new InputStreamResource(userService.exportUser(search, roleName, status, gender));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.xls")
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

    @GetMapping("user-blacklist")
    public ApiResponse<Page<UserDto>> searchUserBlackList(@RequestParam(required = false) String search,
                                                           @RequestParam(required = false) LocalDate date,
                                                           @RequestParam(required = false) String roleName,
                                                           @RequestParam(defaultValue = "1") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
//        LocalDate dateFormat = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return ApiResponse.okStatus(userService.searchUserBlackList(search, date, roleName, page, size));
    }

    @PutMapping("edit-user")
    public ApiResponse<?> editUser(@RequestBody @Valid EditUserRequest request) throws AmsException {
        return ApiResponse.okStatus(userService.editUser(request));
    }

    @GetMapping("users-details")
    public ApiResponse<?> userDetail(@RequestParam Integer id) throws AmsException {
        return ApiResponse.okStatus(userService.findUserById(id));
    }

    @PutMapping("set-newPass")
    public ApiResponse<?> setNewPass(@RequestParam String userName,
                                     @RequestParam String newPass) throws AmsException {
        userService.setNewPassword(userName, newPass);
        return ApiResponse.okStatus("Set new password for " + userName + " Success!");
    }

    @PostMapping("import-user")
    public ApiResponse<?> importUser(@RequestBody List<AddUserRequest> usersList) throws AmsException {
        return ApiResponse.okStatus(userService.importUsers(usersList));
    }

    @DeleteMapping("delete-users")
    public ApiResponse<?> deleteUser(@RequestParam Integer id) throws AmsException {
        return ApiResponse.okStatus(userService.deleteUser(id));
    }
}
