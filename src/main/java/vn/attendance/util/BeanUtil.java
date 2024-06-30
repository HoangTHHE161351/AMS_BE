package vn.attendance.util;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

public class BeanUtil {
    public static final BeanUtilsBean beanUtilsBean = new BeanUtilsBean(new ConvertUtilsBean() {
        @Override
        public Object convert(String value, Class clazz) {
            if (clazz.equals(UUID.class)) {
                return UUID.fromString(value);
            } else if (clazz.equals(LocalDate.class)) {
                try {
                    return LocalDate.parse(value, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } catch (DateTimeParseException ex) {
                    throw new DateTimeParseException("Invalid date format. It should be dd/MM/yyyy", value, 0);
                }
            } else {
                return super.convert(value, clazz);
            }
        }
    });
}
