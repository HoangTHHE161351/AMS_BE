package vn.attendance.service.server.dto;

import lombok.Data;

import java.util.List;

@Data
public class CameraRule {
    int cameraId;
    boolean enable;
    List<LinkGroup> groups;
    boolean strangerMode;
}
