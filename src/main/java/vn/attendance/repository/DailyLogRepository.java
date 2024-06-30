package vn.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.attendance.model.DailyLog;

public interface DailyLogRepository extends JpaRepository<DailyLog, Integer> {
}