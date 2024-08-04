package vn.attendance.config.mqtt;

import org.springframework.stereotype.Service;

@Service
public class MqttConnectionStatusService {

    private Boolean connected = null;

    public Boolean isConnected() {
        return connected;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }
}
