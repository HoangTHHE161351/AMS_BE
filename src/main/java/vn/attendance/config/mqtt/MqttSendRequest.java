package vn.attendance.config.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.attendance.service.mqtt.entity.AddFaceRequest;
import vn.attendance.service.mqtt.entity.ChangeStateRequest;
import vn.attendance.service.mqtt.entity.DelFaceRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MqttSendRequest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MqttGateway mqttGateway;

    public void sendEditFaceRequest(AddFaceRequest addFaceRequest) {
        String json = null;
        try {
            json = convertToAddFaceRequestJson(addFaceRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        mqttGateway.sendToMqtt(json, "mqtt/face/1792832");
    }

    public void sendDeleteFaceRequest(DelFaceRequest request){
        String json = null;
        try {
            json = convertToDeleteFaceRequestJson(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        mqttGateway.sendToMqtt(json, "mqtt/face/1792832");
    }

    public void sendUpdateFaceRequest(AddFaceRequest request){
        String json = null;
        try {
            json = convertToUpdateFaceRequestJson(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        mqttGateway.sendToMqtt(json, "mqtt/face/1792832");
    }

    public void sendChangeStateFaceRequest(ChangeStateRequest request){
        String json = null;
        try {
            json = convertToChangeStatRequestJson(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        mqttGateway.sendToMqtt(json, "mqtt/face/1792832");
    }

    private String convertToChangeStatRequestJson(ChangeStateRequest request) throws JsonProcessingException {
        Map<String, Object> message = new HashMap<>();
        message.put("operator", "EditPerson");
        message.put("messageId", "uniqueMessageId12345"); // Consider generating unique IDs dynamically

        Map<String, Object> info = new HashMap<>();
        info.put("customId", request.getCustomId());
        if(request.getDeviceId()!=null) info.put("facesluiceId", request.getDeviceId());
        info.put("personType", request.getPersonType());

        message.put("info", info);

        return objectMapper.writeValueAsString(message);
    }

    private String convertToDeleteFaceRequestJson(DelFaceRequest request) throws JsonProcessingException{
        Map<String, Object> message = new HashMap<>();
        Map<String, Object> info = new HashMap<>();
        if(request.getCustomId()!=null){
            message.put("operator", "DelPerson");
            info.put("customId", request.getCustomId());
        }else{
            message.put("operator", "DeleteAllPerson");
            info.put("deleteall", 1);
        }
        message.put("messageId", "uniqueMessageId12345"); // Consider generating unique IDs dynamically

        if(request.getDeviceId()!=null) info.put("facesluiceId", request.getDeviceId());
        message.put("info", info);

        return objectMapper.writeValueAsString(message);
    }

    private String convertToAddFaceRequestJson(AddFaceRequest addFaceRequest) throws JsonProcessingException {
        Map<String, Object> message = new HashMap<>();
        message.put("operator", "EditPerson");
        message.put("messageId", "uniqueMessageId12345"); // Consider generating unique IDs dynamically

        Map<String, Object> info = new HashMap<>();
        info.put("customId", addFaceRequest.getCustomId());
        if(addFaceRequest.getDeviceId()!=null) info.put("facesluiceId", addFaceRequest.getDeviceId());
        info.put("name", addFaceRequest.getName());
        info.put("gender", addFaceRequest.getGender());
        info.put("tempCardType", addFaceRequest.getTempCardType());
        info.put("personType", addFaceRequest.getPersonType());
        info.put("cardType", addFaceRequest.getCardType());
        info.put("pic", addFaceRequest.getPic());

        message.put("info", info);

        return objectMapper.writeValueAsString(message);
    }

    private String convertToUpdateFaceRequestJson(AddFaceRequest addFaceRequest) throws JsonProcessingException {
        Map<String, Object> message = new HashMap<>();
        message.put("operator", "EditPerson");
        message.put("messageId", "uniqueMessageId12345"); // Consider generating unique IDs dynamically

        Map<String, Object> info = new HashMap<>();
        info.put("customId", addFaceRequest.getCustomId());
        if(addFaceRequest.getDeviceId()!=null) info.put("facesluiceId", addFaceRequest.getDeviceId());
        info.put("name", addFaceRequest.getName());
        info.put("gender", addFaceRequest.getGender());
        info.put("pic", addFaceRequest.getPic());

        message.put("info", info);

        return objectMapper.writeValueAsString(message);
    }
}
