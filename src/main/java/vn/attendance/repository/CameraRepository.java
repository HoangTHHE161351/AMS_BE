package vn.attendance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.attendance.model.Camera;
import vn.attendance.service.camera.response.ICameraAccessRes;
import vn.attendance.service.camera.response.ICameraRes;
import vn.attendance.service.mqtt.entity.CameraDto;
import vn.attendance.service.semester.response.SemesterDto;

import java.util.List;

@Repository
public interface CameraRepository extends JpaRepository<Camera, Integer> {

        @Query(value = "SELECT c.id as id, " +
                " CONCAT(c.camera_type, ' no.', c.id, ' ', r.room_name) as name, " +
                " c.ip_tcip as ip, " +
                " c.port as port, " +
                " c.description as description, " +
                " c.status as statusc, " +
                " r.id as roomId, " +
                " r.room_name as roomName, " +
                " c.camera_type as cameraType, " +
                " c.check_type as checkType, " +
                " c.status " +
                "FROM Cameras c " +
                "JOIN Rooms r ON c.room_id = r.id " +
                "WHERE (:roomId IS NULL OR r.id = :roomId) " +
                "AND (:status IS NULL OR c.status = :status) " +
                "AND (:search IS NULL OR c.ip_tcip LIKE concat('%',:search,'%') " +
                "OR c.port LIKE concat('%',:search,'%') " +
                "OR c.description LIKE concat('%',:search,'%')) " +
                "ORDER BY r.room_name DESC",
                nativeQuery = true)
        Page<ICameraRes> findAllCameraCCTV(String search, String status, Integer roomId, Pageable pageable);

        @Query("SELECT c FROM  Camera c WHERE c.status<>'DELETED'")
        List<Camera> findAllCamera();

        @Query("SELECT c FROM  Camera c WHERE c.status<>'DELETED'")
        Page<CameraDto> findCamera(String search, Pageable pageable);

        @Query(value = "SELECT c.* FROM  Cameras c WHERE c.camera_type = 'CCTV' and c.status<>'DELETED'", nativeQuery = true)
        List<Camera> findAllCameraCCTV();

        @Query("select c from Camera c where c.id = :cameraId and c.status <> 'DELETED' ")
        Camera getCameraById(Integer cameraId);

        @Query("select c from Camera c where c.channelId = :channelId and c.status <> 'DELETED' ")
        Camera getCameraByChannel(Integer channelId);

        @Query("select c from Camera c where c.ipTcip = :ipTcpip and c.port = :port and c.status <> 'DELETED' ")
        Camera getCameraByIpPort(String ipTcpip, String port);

        @Query("select c from Camera c where c.ipTcip = :ip and c.cameraType = 'LCD' and c.status <> 'DELETED' ")
        Camera findCameraLCDByIp(String ip);

        @Query(value = "select c.id as id, " +
                " c.channel_id as channelId" +
                " from Cameras c where c.room_id = :roomId and c.camera_type = 'LCD' and c.status <> 'DELETED' and c.channel_id is not null", nativeQuery = true)
        List<CameraDto> findLcdByRoom(@Param("roomId") Integer roomId);

        @Query("select c.id as id, " +
                " c.channelId" +
                " from Camera c where c.id = :id and c.cameraType = 'LCD' and c.status <> 'DELETED' and c.channelId is not null")
        CameraDto findLcdById(Integer id);

        @Query(value = "select c.id as id, c.ip_tcip as ip, c.username as username, c.password as password " +
                " from cameras c where c.id = :cameraId and c.status = 'ACTIVE' ", nativeQuery = true)
        ICameraAccessRes getCameraAccess(Integer cameraId);

        @Query(value = "select * from cameras c where c.room_id = :roomId and c.camera_type = 'CCTV' and c.check_type = 'IN' and c.status = 'ACTIVE' ", nativeQuery = true)
        List<Camera> findAllCameraCCTV(Integer roomId);

        @Query(value = "select count(*) from cameras c where c.status <> 'DELETED' and c.room_id = :id", nativeQuery = true)
         Integer countCameraByRoom(Integer id);
}