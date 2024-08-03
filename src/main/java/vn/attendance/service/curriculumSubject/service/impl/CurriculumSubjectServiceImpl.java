package vn.attendance.service.curriculumSubject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.CurriculumSubject;
import vn.attendance.model.Users;
import vn.attendance.repository.CurriculumSubjectRepository;
import vn.attendance.service.curriculumSubject.request.AddCurriculumSubjectRequest;
import vn.attendance.service.curriculumSubject.request.EditCurriculumSubjectRequest;
import vn.attendance.service.curriculumSubject.response.CurriculumSubjectDto;
import vn.attendance.service.curriculumSubject.service.CurriculumSubjectService;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class CurriculumSubjectServiceImpl implements CurriculumSubjectService {

    @Autowired
    CurriculumSubjectRepository curriculumSubjectRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AddCurriculumSubjectRequest addCurriculumSubject(AddCurriculumSubjectRequest request) throws AmsException {
        Users user = BaseUserDetailsService.USER.get();
        if (user == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        CurriculumSubject cs = curriculumSubjectRepository.findByCurriculumIdAndSubjectIdAndSemesterId(request.getCurriculum_id(),
                request.getSubject_id(), request.getSemester_id());

        if (cs != null && !cs.getStatus().equals(Constants.STATUS_TYPE.DELETED)) {
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            throw new AmsException(MessageCode.CURRICULUMSUB_ALREADY_EXISTED);
        }
        CurriculumSubject newCs = new CurriculumSubject();
        newCs.setCurriculumId(request.getCurriculum_id());
        newCs.setSubjectId(request.getSubject_id()); newCs.setSemesterId(request.getSemester_id());
        newCs.setStatus(Constants.STATUS_TYPE.ACTIVE);
        newCs.setModifiedBy(user.getId()); newCs.setModifiedAt(LocalDateTime.now());
        curriculumSubjectRepository.save(newCs);

        request.setStatus(Constants.REQUEST_STATUS.SUCCESS);
        return request;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EditCurriculumSubjectRequest editCurriculumSubject(Integer id, EditCurriculumSubjectRequest request) throws AmsException {
        Users user = BaseUserDetailsService.USER.get();
        if (user == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

//        int exists = curriculumRepository.existsCurriculumSubject(id);
        CurriculumSubject exist = curriculumSubjectRepository.getById(id);
        if (exist == null) {
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            throw new AmsException(MessageCode.CURRICULUMSUB_NOT_FOUND);
        }
        if (exist.getCurriculumId().equals(request.getCurriculum_id()) && exist.getSubjectId().equals(request.getSubject_id())
                && exist.getSemesterId().equals(request.getSemester_id()) && !exist.getStatus().equals("DELETED") ) {
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            throw new AmsException(MessageCode.CURRICULUMSUB_NO_CHANGES);
        }

        // Set the current to delete and create new one
        exist.setModifiedBy(user.getId());
        exist.setModifiedAt(LocalDateTime.now());
        exist.setStatus("DELETED");
        curriculumSubjectRepository.save(exist);

        CurriculumSubject newCs = new CurriculumSubject();
        newCs.setCurriculumId(request.getCurriculum_id());
        newCs.setSubjectId(request.getSubject_id()); newCs.setSemesterId(request.getSemester_id());
        newCs.setStatus(Constants.STATUS_TYPE.ACTIVE);
        newCs.setCreatedBy(user.getId()); newCs.setCreatedAt(LocalDateTime.now());

        request.setStatus(Constants.REQUEST_STATUS.SUCCESS);
        curriculumSubjectRepository.save(newCs);

        return request;
    }

    @Override
    public List<CurriculumSubjectDto> findAllByCurriculumId(Integer id) throws AmsException {
        Users user = BaseUserDetailsService.USER.get();
        if (user == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }
        return curriculumSubjectRepository.findAllByCurriculumId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCurriculumSub(Integer id) throws AmsException {
        Users user = BaseUserDetailsService.USER.get();
        if (user == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }
        CurriculumSubject exist = curriculumSubjectRepository.findById(id).orElse(null);
        if(exist == null || exist.getStatus().equals(Constants.STATUS_TYPE.DELETED)){
            throw  new AmsException(MessageCode.CURRICULUMSUB_NOT_FOUND);
        }
        exist.setModifiedAt(LocalDateTime.now());
        exist.setModifiedBy(user.getId());
        exist.setStatus(Constants.STATUS_TYPE.DELETED);
        curriculumSubjectRepository.save(exist);

    }


    @Override
    public Page<CurriculumSubjectDto> findCurriculumSubject(String search, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return curriculumSubjectRepository.findCurriculumSubject(search,pageable);
    }



}
