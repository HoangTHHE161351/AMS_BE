package vn.attendance.service.server.service;


import vn.attendance.exception.AmsException;
import vn.attendance.service.server.request.ServerLoginRequest;

public interface ServerService {

    void login(ServerLoginRequest request) throws AmsException;

    void logout();
}
