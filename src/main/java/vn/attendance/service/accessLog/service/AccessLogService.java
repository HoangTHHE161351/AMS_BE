package vn.attendance.service.accessLog.service;

import org.springframework.data.domain.Page;
import vn.attendance.exception.AmsException;
import vn.attendance.service.accessLog.response.AccessLogDTO;
import vn.attendance.service.accessLog.response.AccessLogInRoomDto;

import java.time.LocalDate;
import java.util.List;

public interface AccessLogService {

    Page<AccessLogInRoomDto> getAccessLogInRooms(String search,
                                                 Integer roomId,
                                                 LocalDate date,
                                                 int page, int size) throws AmsException;

    List<AccessLogDTO> findByRoomIdAndUserId(Integer roomId, String userName, LocalDate date) throws AmsException;

}
