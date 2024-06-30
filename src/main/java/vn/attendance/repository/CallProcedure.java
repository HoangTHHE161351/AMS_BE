package vn.attendance.repository;//package vn.sphinx.hysmart.logistic.repository;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import vn.sphinx.hysmart.logistic.service.dto.requests.CapNhapCongViecResquest;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
//
///**
// * @Author phucnv
// * @create 4/13/2021 3:41 PM
// */
//@Service
//@Slf4j
//public class CallProcedure {
//    @PersistenceContext(unitName = "entityManagerFactory")
//    EntityManager entityManager;
//
//    @Transactional
//    public void capNhatCongViecTW(CapNhapCongViecResquest luuQuaTrinhXuLyDTO) {
//        String strQuery = "{ call PCK_CONG_VIEC.ADD_CONG_VIEC(?, ?, ?, ?, ?, ?, ?, ?, ?) }";
//        Query query = entityManager.createNativeQuery(strQuery);
//        try {
//            int idx = 1;
//            query.setParameter(idx++, luuQuaTrinhXuLyDTO.getHoSoId());
//            query.setParameter(idx++, luuQuaTrinhXuLyDTO.getCongViecId());
//            query.setParameter(idx++, luuQuaTrinhXuLyDTO.getKetQuaCongViec());
//            query.setParameter(idx++, luuQuaTrinhXuLyDTO.getMaketQua());
//            query.setParameter(idx++, luuQuaTrinhXuLyDTO.getLyDo());
//            query.setParameter(idx++, luuQuaTrinhXuLyDTO.getNguoiTao());
//            query.setParameter(idx++, luuQuaTrinhXuLyDTO.getNguoiXuly());
//            query.setParameter(idx++, luuQuaTrinhXuLyDTO.getNgayXuLy());
//            query.setParameter(idx++, luuQuaTrinhXuLyDTO.getCapXuLy());
//            query.executeUpdate();
//        }catch (Exception e) {
//            log.error("Loi khi cap nhat cong viec");
//            log.error(e.toString());
//            throw e;
//        }
//    }
//
//
//}
