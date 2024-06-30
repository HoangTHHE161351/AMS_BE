package vn.attendance.repository;

import org.hibernate.mapping.Any;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.attendance.model.Camera;

import java.util.List;
import java.util.Optional;

@Repository
public interface CameraRepository extends JpaRepository<Camera, Integer> {

        @Query("SELECT c FROM  Camera c WHERE c.status='ACTIVE'")
        Page<Camera> findAllCamera(Pageable pageable);

        @Query("SELECT c FROM  Camera c WHERE c.status<>'DELETED'")
        List<Camera> findAllCamera();

        @Query("select c from Camera c where c.id = :cameraId and c.status <> 'DELETED' ")
        Camera GetCameraById(Integer cameraId);

        @Query("select c from Camera c where c.channelId = :channelId and c.status <> 'DELETED' ")
        Camera GetCameraByChannel(Integer channelId);
        @Query("select c from Camera c where c.ipTcip = :ipTcpip and c.port = :port and c.status <> 'DELETED' ")
        Camera getCameraByIpPort(String ipTcpip, String port);
}