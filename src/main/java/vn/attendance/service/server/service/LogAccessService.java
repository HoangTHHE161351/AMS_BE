package vn.attendance.service.server.service;

import org.springframework.data.domain.Page;
import vn.attendance.exception.AmsException;
import vn.attendance.helper.ApiResponse;
import vn.attendance.service.server.dto.AccessLogDTO;
import vn.attendance.service.server.service.dto.ImageDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface LogAccessService {
    boolean AttachRealLoadPic();

    boolean DetachRealLoadPic();

    Page<AccessLogDTO> findLog(String search,
                               Integer type,
                               Integer roomId,
                               LocalDate date,
                               Integer notType,
                               Integer page,
                               Integer size);

    Page<AccessLogDTO> findStrangerBySchedule(Integer scheduleId, Integer page, Integer size) throws AmsException;

    List<ImageDto> getImageLog(LocalDateTime time);
}
