package vn.attendance.config.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.scheduling.annotation.Scheduled;
import vn.attendance.exception.AmsException;
import vn.attendance.service.mqtt.service.MqttService;
import vn.attendance.service.notify.service.NotifyService;
import vn.attendance.service.server.service.FaceRecognitionService;
import vn.attendance.util.Constants;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Configuration
public class MqttConfig {
    @Value("${mqtt.server}")
    String uri;
    @Value("${mqtt.username}")
    String username;
    @Value("${mqtt.password}")
    String pass;
    @Value("${mqtt.id}")
    String clientId;

    @Autowired
    FaceRecognitionService faceRecognitionService;

    @Autowired
    private MqttConnectionStatusService connectionStatusService;

    @Autowired
    NotifyService notifyService;

    @Autowired
    MqttService mqttService;

    private boolean connected = false;

    @Bean
    public MqttPahoClientFactory mqttPahoClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{uri});
        options.setUserName(username);
        options.setPassword(pass.toCharArray());
        options.setCleanSession(true);
        options.setAutomaticReconnect(true); // Đảm bảo tự động kết nối lại khi mất kết nối
        factory.setConnectionOptions(options);

        return factory;
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() throws MqttException {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientId, mqttPahoClientFactory(), "#");
        adapter.setCompletionTimeout(10 * 1000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(2);
        adapter.setOutputChannel(mqttInputChannel());
        adapter.setRecoveryInterval(5000);
        adapter.setCompletionTimeout(10000);
        adapter.setRecoveryInterval(10000);
        return adapter;
    }

    @Scheduled(fixedDelay = 10000)
    @PostConstruct// Kiểm tra kết nối mỗi 10 giây
    public void checkConnection() throws AmsException {
        try {
            MqttClient testClient = new MqttClient(uri, clientId + "_test", null);
            testClient.connect(mqttPahoClientFactory().getConnectionOptions());
            testClient.disconnect();
            testClient.close();
            if (connectionStatusService.isConnected() == null || !connectionStatusService.isConnected()) {
                connectionStatusService.setConnected(true);
                notifyService.addNotifyForDevice(Constants.NOTIFY_TITLE.DEVICE_CONNECT, "Connect to MQTT Broker");
            }
        } catch (MqttException e) {
            log.error("Failed to connect to MQTT broker: {}", e.getMessage());
            if (connectionStatusService.isConnected() == null || connectionStatusService.isConnected()) {
                connectionStatusService.setConnected(false);
                notifyService.addNotifyForDevice(Constants.NOTIFY_TITLE.DEVICE_DISCONNECT, "Unable to connect to MQTT Broker");
            }
        }
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return message -> {
            String topic = Objects.requireNonNull(message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC)).toString();

            if (topic.equals("mqtt/face/1792832/Rec")) {
                System.out.println("Detected face");
            }

            if (topic.equals("mqtt/face/1792832/Snap")) {
                System.out.println("Stranger detected");
            }
            System.out.println(message.getPayload());

            // Gọi lcd service xử lý dữ liệu từ message.getPayload()
            CompletableFuture.supplyAsync(() -> {
                try {
                    faceRecognitionService.extractJsonLcdLog(message.getPayload().toString());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                return null;
            });
        };
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(clientId, mqttPahoClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic("mqtt/face/1792832");
        return messageHandler;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}


