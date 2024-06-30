package vn.attendance.service.classRoom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.ClassRoom;
import vn.attendance.model.Users;
import vn.attendance.repository.ClassRoomRepository;
import vn.attendance.service.classRoom.request.AddClassRoomRequest;
import vn.attendance.service.classRoom.request.EditClassRoomRequest;
import vn.attendance.service.classRoom.response.ClassRoomDto;
import vn.attendance.service.classRoom.service.ClassRoomService;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClassRoomServiceImpl implements ClassRoomService {

    @Autowired
    ClassRoomRepository classRoomRepository;


    @Override
    public Page<ClassRoomDto> findAllClassRoom(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1,size);
        return classRoomRepository.findAllClasses(search,pageable);
    }

    @Override
    public List<ClassRoom> findAll() {
        return classRoomRepository.findAllClass();
    }

    @Override
    public ClassRoom findById(Integer id)  throws AmsException {
        ClassRoom classRoom = classRoomRepository.findById(id).orElse(null);
        if (classRoom == null)
            throw new AmsException(MessageCode.NOT_FOUND);
        return classRoom;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AddClassRoomRequest addClassRoom(AddClassRoomRequest request) throws AmsException {
        // Get user info
        Users users = BaseUserDetailsService.USER.get();
        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        List<ClassRoom> classRooms = classRoomRepository.findAll();
        for (ClassRoom cr : classRooms) {
            if (cr.getClassName().equals(request.getClassName())) {
                request.setStatus("FAILED");
                throw new AmsException(MessageCode.CLASSNAME_ALREADY_EXISTED);
            }
        }

        try {
            ClassRoom classRoom = new ClassRoom();
            classRoom.setId(null);
            classRoom.setClassName(request.getClassName());
            classRoom.setDescription(request.getDescription());
            classRoom.setStatus(Constants.STATUS_TYPE.ACTIVE);
            classRoom.setCreatedBy(users.getId());
            classRoom.setCreatedAt(LocalDateTime.now());
            // Save camera
            classRoomRepository.save(classRoom);
            request.setStatus("SUCCESS");
        } catch (Exception e) {
            throw new AmsException(MessageCode.ADD_CLASSROOM_FAIL);
        }

        return request;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editClassRoom(Integer id, EditClassRoomRequest request) throws AmsException {
        Users user = BaseUserDetailsService.USER.get();
        if (user == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        ClassRoom classRoom = classRoomRepository.getById(id);
        if (classRoom == null) {
            request.setStatus("FAILED");
            throw  new AmsException(MessageCode.CLASSROOM_NOT_FOUND);
        }

        classRoom.setClassName(request.getClassName());
        classRoom.setDescription(request.getDescription());
        classRoom.setModifiedBy(user.getId());
        classRoom.setModifiedAt(LocalDateTime.now());
        request.setStatus("SUCCESS");
        classRoomRepository.save(classRoom);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteClassRoom(Integer id) throws AmsException {
        Users user = BaseUserDetailsService.USER.get();
        if (user == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        ClassRoom classRoom = classRoomRepository.getById(id);
        classRoom.setStatus(Constants.STATUS_TYPE.DELETED);
        classRoomRepository.save(classRoom);
    }


}
