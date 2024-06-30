package vn.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.attendance.model.AccessLog;

@Repository
public interface LogRepository extends JpaRepository<AccessLog, Integer> {

}
