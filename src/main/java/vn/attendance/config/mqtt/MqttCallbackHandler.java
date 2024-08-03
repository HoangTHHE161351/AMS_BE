package vn.attendance.config.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

@Slf4j
public class MqttCallbackHandler implements MqttCallbackExtended {
    private boolean connected = false;

    @Override
    public void connectionLost(Throwable cause) {
        System.err.println("MQTT connection lost" + cause.getMessage());
        setConnected(false);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        log.info("Message arrived. Topic: {}. Message: {}", topic, new String(message.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // Do nothing
    }

    public boolean isConnected() {
        return connected;
    }

    private void setConnected(boolean connected) {
        this.connected = connected;
    }

    @Override
    public void connectComplete(boolean b, String s) {
        System.out.println("MQTT connection lost");
        setConnected(true);
    }
}

