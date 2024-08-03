package vn.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.attendance.model.Facial;
import vn.attendance.service.facial.response.FacialResponseDto;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface FacialRepository extends JpaRepository<Facial, Integer> {

    @Query("select new vn.attendance.service.facial.response.FacialResponseDto(f.id, u.firstName, u.lastName, " +
            "f.image, f.status, f.createdAt, uc.username, f.modifiedAt, um.username) " +
            "from Facial f join Users u on f.userId = u.id and u.status = 'ACTIVE' " +
            "left join Users uc on f.createdBy = uc.id and uc.status = 'ACTIVE' " +
            "left join Users um on f.modifiedBy = um.id and um.status = 'ACTIVE' " +
            "where f.userId = :userId")
    List<FacialResponseDto> findAllFacialByUserId(Integer userId);

    @Query("select f " +
            "from Facial f join Users u on u.id = f.userId and u.status = 'ACTIVE' " +
            " where f.userId = :userId")
    List<Facial> findFacialByUserId(Integer userId);

    @Query("select f from Facial f where f.userId = :userId and f.status = 'ACTIVE' order by f.createdAt desc ")
    Facial findFacialByUserIdActive(Integer userId);

    @Modifying
    @Query(value = "update facial set status = 'DELETED' where id in :facialIds and userId = :userId ",nativeQuery = true)
    int deleteFacialByUserId(Integer userId, List<Integer> facialIds);

    @Query(value = "select f.* from Facial f where f.userId = :userId and f.id = :faceId", nativeQuery = true)
    Facial findFacialById(Integer userId, Integer faceId);
}
