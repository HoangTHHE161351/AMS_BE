package vn.attendance.service.user.facade;


import vn.attendance.exception.AmsException;
import vn.attendance.model.Users;
import vn.attendance.service.user.entity.req.LoginRequest;
import vn.attendance.service.user.entity.req.TokenRefreshReq;
import vn.attendance.service.user.entity.req.UserLoginReq;
import vn.attendance.service.user.entity.res.Res;
import vn.attendance.service.user.service.request.EditUserRequest;

import java.util.Map;

public interface LoginFacade {

    Res loginUser(LoginRequest req) throws Exception;

    Res getProfileUser(Map<String, String> headers);

    Users registerUser(UserLoginReq userLoginReq) throws Exception;

    void editProfileUser(EditUserRequest request) throws AmsException;
}
