package vn.attendance.service.notify.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.controller.SocketController;
import vn.attendance.exception.AmsException;
import vn.attendance.model.*;
import vn.attendance.repository.*;
import vn.attendance.service.notify.service.NotifyService;
import vn.attendance.service.notify.entity.NotifyDto;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class NotifyServiceImpl implements NotifyService {

    @Autowired
    NotifyRepository notifyRepository;

    @Autowired
    NotifyUserRepository notifyUserRepository;

    @Autowired
    UsersRepository usersRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    AccessLogRepository accessLogRepository;
    @Autowired
    CameraRepository cameraRepository;
    @Autowired
    SocketController socketController;

    @Override
    public Page<NotifyDto> findAll(String search, Integer roomId, LocalDate date, Integer page, Integer size) throws AmsException {
        Users user = BaseUserDetailsService.USER.get();
        if(user == null) throw new AmsException(MessageCode.USER_NOT_FOUND);

        return notifyRepository.findNotify(user.getId(), PageRequest.of(page - 1, size));
    }

    @Override
    public Integer checkReadNotify() throws AmsException {
        Users user = BaseUserDetailsService.USER.get();
        if(user == null) throw new AmsException(MessageCode.USER_NOT_FOUND);
        return notifyRepository.checkReadNotify(user.getId());
    }

    @Override
    public void setReadNotify(Integer notifyId) throws AmsException {
        Users user = BaseUserDetailsService.USER.get();
        if(user == null) throw new AmsException(MessageCode.USER_NOT_FOUND);
        notifyRepository.setReadNotify(user.getId(), notifyId);
    }

    @Override
    public void AddNotify(Notify notify, List<Integer> userIds) throws AmsException {
        notifyRepository.save(notify);
        for (Integer user: userIds
             ) {
            NotifyUser notifyUser = new NotifyUser();
            notifyUser.setUserId(user);
            notifyUser.setNotifyId(notify.getId());
            notifyUser.setIsRead(0);
            notifyUserRepository.save(notifyUser);
        }
        socketController.notifyStatus("New notify");
    }


    @Override
    public void addNotifyForSynchronize(String title, String message) throws AmsException {
        Notify notify = new Notify();
        notify.setTitle(title);
        notify.setContent(message);
        notify.setTime(LocalDateTime.now());
        notify.setDestinationPage(Constants.SCREEN_NAME.SYNCHRONIZE);

        AddNotify(notify, usersRepository.findUserIdByRole(Constants.ROLE.ADMIN));
    }

    @Override
    public void addNotifyForDevice(String title, String message) throws AmsException {
        Notify notify = new Notify();
        notify.setTitle(title);
        notify.setContent(message);
        notify.setTime(LocalDateTime.now());
        notify.setDestinationPage(Constants.SCREEN_NAME.DEVICE);

        AddNotify(notify, usersRepository.findUserIdByRole(Constants.ROLE.ADMIN));
    }
}
