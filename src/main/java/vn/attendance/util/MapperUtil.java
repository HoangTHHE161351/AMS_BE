package vn.attendance.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.stream.Stream;

@Service
public class MapperUtil {

    public static String convertToJsonName(Class clazz, String fieldName) {
        return Stream.of(clazz.getDeclaredFields())
                .filter(field -> field.getName().equals(fieldName))
                .map(Field::getDeclaredAnnotations)
                .flatMap(Stream::of)
                .filter(annotation -> annotation instanceof JsonProperty)
                .map(annotation -> ((JsonProperty) annotation).value())
                .findFirst().orElse(fieldName);
    }
}
