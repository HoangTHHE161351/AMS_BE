package vn.attendance.service.server.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ServerLoginRequest {
    @NotEmpty(message = "IP be not null")
    String ip;
    @NotNull(message = "Port be not null")
    Integer port;
    @NotEmpty(message = "Username be not null")
    String user;
    @NotEmpty(message = "Password be not null")
    String password;
}
