package vn.attendance.service.curriculum.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Curriculum;
import vn.attendance.repository.CurriculumRepository;
import vn.attendance.service.curriculum.request.AddCurriculumRequest;
import vn.attendance.service.curriculum.request.EditCurriculumRequest;
import vn.attendance.service.curriculum.response.CurriculumDto;
import vn.attendance.service.curriculum.service.CurriculumService;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CurriculumServiceImpl implements CurriculumService {
    @Autowired
    CurriculumRepository curriculumRepository;

    @Override
    public Curriculum findById(Integer id) throws AmsException {
        Curriculum curriculum = curriculumRepository.findById(id).orElse(null);
        if (curriculum == null) {
            throw new AmsException(MessageCode.NOT_FOUND);
        }
        return curriculum;
    }

    @Override
    public Page<CurriculumDto> findCurriculum(String search, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return curriculumRepository.findCurriculum(search,pageable);
    }

    @Override
    public List<Curriculum> findAllCurriculum() {
        return List.of();
    }

    @Override
    public void deleteCurriculum(Integer id) throws AmsException {
        Curriculum curriculum = curriculumRepository.findById(id).orElse(null);
        if (curriculum == null) {
            throw new AmsException(MessageCode.CURRICULUM_NOT_FOUND);
        }
        curriculumRepository.deleteCurriculum(id);
    }


    @Override
    public void addCurriculum(AddCurriculumRequest addCurriculumRequest) throws AmsException {
        List<Curriculum> curriculumList = curriculumRepository.findAll();

        for (Curriculum curriculum : curriculumList) {
            if (curriculum.getCurriculumName().equals(addCurriculumRequest.getCurriculumName())) {
                throw new AmsException(MessageCode.CURRICULUM_NAME_EXIST);
            }
        }

        Curriculum curriculum = new Curriculum();
        curriculum.setCurriculumName(addCurriculumRequest.getCurriculumName());
        curriculum.setDescription(addCurriculumRequest.getDescription());
        curriculum.setStatus(Constants.STATUS_TYPE.ACTIVE);
        curriculum.setModifiedAt(LocalDateTime.now());
        curriculum.setModifiedBy(BaseUserDetailsService.USER.get().getId());
        curriculum.setCreatedAt(LocalDateTime.now());
        curriculum.setCreatedBy(BaseUserDetailsService.USER.get().getId());
        curriculumRepository.save(curriculum);
    }

    @Override
    public void editCurriculum(int id, EditCurriculumRequest editCurriculumRequest) throws AmsException {
        Curriculum curriculum = curriculumRepository.findById(id).orElse(null);
        if (curriculum == null) {
            throw new AmsException(MessageCode.CURRICULUM_NOT_FOUND);
        }

        List<Curriculum> curriculumList = curriculumRepository.findAll();

        for (Curriculum curriculum1 : curriculumList) {
            if (curriculum1.getCurriculumName().equals(editCurriculumRequest.getCurriculumName()) && curriculum1.getId() != id) {
                throw new AmsException(MessageCode.CURRICULUM_NAME_EXIST);
            }
        }

        curriculum.setCurriculumName(editCurriculumRequest.getCurriculumName());
        curriculum.setDescription(editCurriculumRequest.getDescription());
        curriculum.setModifiedAt(LocalDateTime.now());
        curriculum.setModifiedBy(BaseUserDetailsService.USER.get().getId());
        curriculumRepository.save(curriculum);
    }
}
