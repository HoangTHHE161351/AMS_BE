package vn.attendance.util;

import lombok.Data;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;


@Data
public class ApiUtil {

    public static String callApiBodyRaw(String url, HttpMethod method, String json, HttpHeaders headers) throws IOException, JSONException {
        RestTemplate restTemplate = new RestTemplate();
        JSONObject jsonObj = new JSONObject(json);
        HttpEntity<String> entity = new HttpEntity<String>(jsonObj.toString(), headers);
        ResponseEntity<String> resTemp = restTemplate.exchange(url, method, entity, String.class);
        return resTemp.getBody();
    }
}
