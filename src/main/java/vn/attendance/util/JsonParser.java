package vn.attendance.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParser {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T jsonToObject(String jsonStr, Class<T> clazz) throws JsonProcessingException {
        return mapper.readValue(jsonStr, clazz);
    }
}
