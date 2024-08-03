package vn.attendance.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.attendance.exception.AmsException;
import vn.attendance.helper.ApiResponse;
import vn.attendance.service.user.entity.req.LoginRequest;
import vn.attendance.service.user.entity.req.UserLoginReq;
import vn.attendance.service.user.facade.LoginFacade;
import vn.attendance.service.user.service.request.EditUserRequest;

import javax.validation.Valid;
import java.util.Map;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/v1/login")
public class LoginController extends BaseController {

    @Autowired
    private LoginFacade loginFacade;

    //    @CrossOrigin(origins = "http://localhost:8016")
    @PostMapping()
    public ApiResponse<?> loginUser(@RequestBody @Valid LoginRequest userLoginReq) throws Exception {
        return ApiResponse.okStatus(loginFacade.loginUser(userLoginReq));

    }

    @ApiOperation(value = "This test swagger", hidden = false, nickname = "LongZen1", consumes = "LongZen", tags = "LongZenT")
    @GetMapping("get-profile")
    public ApiResponse<?> getProfileUser(@RequestHeader Map<String, String> headers) {
        return ApiResponse.okStatus(loginFacade.getProfileUser(headers));

    }

    @PostMapping("edit-profile")
    public ApiResponse<?> editProfile(@RequestBody EditUserRequest request) throws AmsException {
        loginFacade.editProfileUser(request);
        return ApiResponse.okStatus("Edit profile successfully");
    }

    // todo: register
    @ApiOperation(value = "This test swagger", hidden = true)
    @PostMapping("register")
    public ApiResponse<?> registerUser(@Valid @RequestBody UserLoginReq userLoginReq) throws Exception {
        return ApiResponse.okStatus(loginFacade.registerUser(userLoginReq));

    }

}
