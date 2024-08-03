package vn.attendance.converter;

import org.apache.commons.beanutils.Converter;

import java.util.Objects;

public class EnumConverter implements Converter {

    @Override
    public <T> T convert(Class<T> type, Object value) {
        if (Objects.isNull(value)) {
            return null;
        /*} else if (type.equals(Gender.class)) {
            return (T) Gender.fromId(Integer.parseInt(value.toString()));*/
        } else {
            return (T) value;
        }
    }
}
