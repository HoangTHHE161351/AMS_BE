package vn.attendance.service.camera.response;



public interface ICameraRes {
    Integer getId();
    String getName();
    String getIp();
    Integer getPort();
    String getDescription();
    String getStatus();
    Integer getRoomId();
    String getRoomName();
    String getCameraType();
    String getCheckType();
}
