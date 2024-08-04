package vn.attendance.config.mqtt;

import org.springframework.stereotype.Service;

@Service
public class MqttConnectionStatusService {

    private boolean connected;

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
