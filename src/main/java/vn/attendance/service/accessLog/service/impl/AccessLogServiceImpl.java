package vn.attendance.service.accessLog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Users;
import vn.attendance.repository.AccessLogRepository;
import vn.attendance.service.accessLog.response.AccessLogDTO;
import vn.attendance.service.accessLog.response.AccessLogInRoomDto;
import vn.attendance.service.accessLog.response.IAccessLogDTO;
import vn.attendance.service.accessLog.response.IAccessLogInRoomDto;
import vn.attendance.service.accessLog.service.AccessLogService;
import vn.attendance.util.MessageCode;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccessLogServiceImpl implements AccessLogService {

    @Autowired
    AccessLogRepository accessLogRepository;

    @Override
    public Page<AccessLogInRoomDto> getAccessLogInRooms(String search, Integer roomId, LocalDate date, int page, int size) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();
        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }
//        if (users.getRoleId() != 1 && users.getRoleId() != 2) {
//            throw new AmsException(MessageCode.USER_NOT_AUTHORITY);
//        }

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<IAccessLogInRoomDto> logInRoomDtos = accessLogRepository.historyLogOfRoom(search, roomId, date, pageable);

        return logInRoomDtos.map(AccessLogInRoomDto::new);
    }

    @Override
    public List<AccessLogDTO> findByRoomIdAndUserId(Integer roomId, String userName, LocalDate date) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();
        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }
//        if (users.getRoleId() != 1 && users.getRoleId() != 2) {
//            throw new AmsException(MessageCode.USER_NOT_AUTHORITY);
//        }

        List<IAccessLogDTO> accessLogs = accessLogRepository.findByRoomIdAndUserId(roomId, userName, date);
        List<AccessLogDTO> accessLogsDTO = accessLogs.stream().map(AccessLogDTO::new).collect(Collectors.toList());
        return accessLogsDTO;
    }
}
