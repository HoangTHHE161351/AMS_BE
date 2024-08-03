package vn.attendance.service.notify.service;

import org.springframework.data.domain.Page;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Notify;
import vn.attendance.service.notify.entity.NotifyDto;

import java.time.LocalDate;
import java.util.List;

public interface NotifyService {

    Page<NotifyDto> findAll(String search, Integer roomId, LocalDate date, Integer page, Integer size) throws AmsException;

    Integer checkReadNotify() throws AmsException;

    void setReadNotify(Integer notifyId) throws AmsException;

    void AddNotify(Notify notify, List<Integer> userIds) throws AmsException;
}
