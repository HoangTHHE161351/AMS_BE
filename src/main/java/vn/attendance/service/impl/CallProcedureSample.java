//package vn.sphinx.hysmart.logistic.service.impl;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.stereotype.Service;
//import vn.sphinx.hysmart.logistic.model.VneidYcDbTrongNgoai;
//import vn.sphinx.hysmart.logistic.repository.VneidYcDbTrongNgoaiRepository;
//import vn.sphinx.hysmart.logistic.service.dto.requests.TrangThaiToCaoRequest;
//import vn.sphinx.hysmart.logistic.util.Constants;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
//import javax.transaction.Transactional;
//import java.util.List;
//import java.util.Optional;
//
///**
// * @Author phucnv
// * @create 4/13/2021 3:41 PM
// */
//@Service
//@Slf4j
//public class CallProcedureToGiac {
//    @PersistenceContext(unitName = "entityManagerFactory")
//    EntityManager entityManager;
//
//    @Autowired
//    VneidYcDbTrongNgoaiRepository vneidYcDbTrongNgoaiRepository;
//
//    @Autowired
//    QuetYcDbTrongNgoaiImpl ycDbTrongNgoai;
//
//    public void insertProcessingStatus(TrangThaiToCaoRequest trangThaiDto) throws Exception {
//        StringBuilder strQuery = new StringBuilder();
//        strQuery.append("call vneid_dvc.PCK_UPDATE_DENOUNCE_STATUS.UPDATE_PROCESSING_STATUS(:uuid, :statusCode, :citizenId, :timeProcess, :message)");
//
//        Query query = entityManager.createNativeQuery(strQuery.toString());
//
//
//        try {
//            query.setParameter("uuid", trangThaiDto.getDenunciationUuid());
//            query.setParameter("statusCode", trangThaiDto.getStatusCode());
//            query.setParameter("citizenId", trangThaiDto.getCitizenId());
//            query.setParameter("timeProcess", trangThaiDto.getTimeProcessing());
//            query.setParameter("message", trangThaiDto.getMessage());
//            log.info("[Dto][{}][P1:{}][P2:{}]", new ObjectMapper().writeValueAsString(trangThaiDto),
//                    query.getParameter("uuid").getName(),
//                    query.getParameter("message").getName());
//            query.executeUpdate();
//            log.info("[Execute Prcedure success!");
//        } catch (Exception e) {
//            log.error("Loi khi cap nhat trang thai to cao");
//            throw new Exception(e);
//        }
//    }
//
//}
