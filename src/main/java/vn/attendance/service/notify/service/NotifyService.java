package vn.attendance.service.notify.service;

import vn.attendance.exception.AmsException;
import vn.attendance.model.Notify;
import vn.attendance.service.notify.entity.NotifyDto;

import java.time.LocalDate;
import java.util.List;

public interface NotifyService {

    List<NotifyDto> findAll(String search, Integer roomId, LocalDate date) throws AmsException;

    Integer checkReadNotify() throws AmsException;

    void setReadNotify(Integer notifyId) throws AmsException;

    void AddNotify(Notify notify, List<Integer> userIds) throws AmsException;

    void addNotifyForSynchronize(String title, String message) throws AmsException;
    void addNotifyForDevice(String title, String message) throws AmsException;
}
