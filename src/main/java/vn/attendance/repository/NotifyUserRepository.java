package vn.attendance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.attendance.model.Notify;
import vn.attendance.model.NotifyUser;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Repository
public interface NotifyUserRepository extends JpaRepository<NotifyUser, Integer> {


}

