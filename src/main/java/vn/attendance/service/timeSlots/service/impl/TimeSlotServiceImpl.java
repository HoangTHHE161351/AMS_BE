package vn.attendance.service.timeSlots.service.impl;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.TimeSlot;
import vn.attendance.model.Users;
import vn.attendance.repository.TimeSlotRepository;
import vn.attendance.service.timeSlots.request.AddTimeSlotRequest;
import vn.attendance.service.timeSlots.request.EditTimeSlotRequest;
import vn.attendance.service.timeSlots.response.TimeSlotDto;
import vn.attendance.service.timeSlots.service.TimeSlotService;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TimeSlotServiceImpl implements TimeSlotService {
    @Autowired
    TimeSlotRepository timeSlotRepository;

    @Override
    public Page<TimeSlotDto> findAllTimeSlots(int page, int size) {
        Pageable pageable = PageRequest.of(page-1,size);
        Page<TimeSlotDto> timeSlotDtos = timeSlotRepository.findAllDto(pageable);
        return timeSlotDtos.map(t -> new TimeSlotDto(t.getId(),t.getStartTime(),t.getEndTime(),t.getDescription(),t.getStatus()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AddTimeSlotRequest addTimeSLot(AddTimeSlotRequest request) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();
        if (users == null)
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        try {
            if (timeSlotRepository.findByTime(request.getStartTime(), request.getEndTime()).isPresent()) {
                System.out.println("abc");
                throw new AmsException(MessageCode.TIMESLOT_ALREADY_EXISTS);
            }

            TimeSlot timeSlotNew = new TimeSlot();
            timeSlotNew.setStartTime(request.getStartTime());
            timeSlotNew.setEndTime(request.getEndTime());
            timeSlotNew.setDescription(request.getDescription());
            timeSlotNew.setStatus(Constants.STATUS_TYPE.ACTIVE);
            timeSlotNew.setCreatedAt(LocalDateTime.now());
            timeSlotNew.setCreatedBy(users.getId());
            request.setStatusAdd("SUCCESS");
            timeSlotRepository.save(timeSlotNew);
        }
        catch (AmsException e) {
            request.setStatusAdd("FAILED");
            request.setErrorMess(e.getMessage());
        }
        return request;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Optional<TimeSlot> editTimeSlot(Integer id, EditTimeSlotRequest request) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();
        if (users == null)
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        if (timeSlotRepository.findByTime(request.getStartTime(), request.getEndTime()).isPresent()) {
            throw new AmsException(MessageCode.TIMESLOT_ALREADY_EXISTS);
        }

        return timeSlotRepository.findById(id).map( ts -> {
            ts.setModifiedBy(users.getId());
            ts.setModifiedAt(LocalDateTime.now());
            ts.setStartTime(request.getStartTime());
            ts.setEndTime(request.getEndTime());
            ts.setDescription(request.getDescription());
            return timeSlotRepository.save(ts);
        });

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Optional<TimeSlot> deleteSlot(Integer id) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();
        if (users == null)
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        return timeSlotRepository.findById(id).map(ts -> {
            ts.setStatus(Constants.STATUS_TYPE.DELETED);
            ts.setModifiedAt(LocalDateTime.now());
            ts.setModifiedBy(users.getId());
            return timeSlotRepository.save(ts);
        });
    }

    @Override
    public List<TimeSlot> findAllTimeSlots() throws AmsException {
        return timeSlotRepository.findAllTime().stream().toList();
    }


}
