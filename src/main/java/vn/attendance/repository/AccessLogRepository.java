package vn.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.attendance.model.AccessLog;

public interface AccessLogRepository extends JpaRepository<AccessLog, Integer> {
}