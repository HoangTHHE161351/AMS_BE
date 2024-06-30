package vn.attendance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.attendance.model.CameraRoom;

@Repository
public interface CameraRoomRepository extends JpaRepository<CameraRoom, Integer> {
    @Query("SELECT cr from CameraRoom cr order by cr.roomId")
    Page<CameraRoom> findAllCameraRoom(Pageable pageable);
}