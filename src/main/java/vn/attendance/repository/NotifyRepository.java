package vn.attendance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.attendance.model.Notify;
import vn.attendance.service.notify.entity.NotifyDto;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface NotifyRepository extends JpaRepository<Notify, Integer> {

    @Query(value = "SELECT n.id as id, " +
            "n.title as title, " +
            "n.content as content, " +
            "n.destination_page as destinationPage, " +
            "n.page_params as pageParams, " +
            "n.time as time, " +
            "nu.is_read as isRead " +
            "FROM notify n " +
            "JOIN notify_user nu ON n.id = nu.notify_id " +
            "WHERE " +
            " nu.user_id = :userId " +
            "ORDER BY n.time DESC",
            nativeQuery = true)
    List<NotifyDto> findNotify(@Param("userId") Integer userId);

    @Query(value = "SELECT COUNT(*) FROM notify n " +
            "JOIN notify_user nu ON n.id = nu.notify_id " +
            "WHERE nu.user_id = :userId " +
            "AND nu.is_read = 0", nativeQuery = true)
    Integer checkReadNotify(@Param("userId") Integer userId);


    @Modifying
    @Transactional
    @Query(value = "UPDATE notify_user nu " +
            "SET nu.is_read = 1 " +
            "WHERE nu.user_id = :userId " +
            "AND nu.is_read = 0 " +
            "AND (:notifyId is null or nu.notify_Id = :notifyId )", nativeQuery = true)
    void setReadNotify(@Param("userId") Integer userId, @Param("notifyId") Integer notifyId);

}

