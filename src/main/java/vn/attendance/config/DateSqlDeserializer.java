package vn.attendance.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.SneakyThrows;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateSqlDeserializer extends JsonDeserializer<Date> {

    private DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    private DateFormat dtf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @SneakyThrows
    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String datePattern = "\\d{2}-\\d{2}-\\d{4}";
        String dateTimePattern = "\\d{2}-\\d{2}-\\d{4}\\s\\d{2}:\\d{2}:\\d{2}";
        if (p.getText().matches(datePattern)) {
            return new Date(df.parse(p.getText()).getTime());
        }
        if (p.getText().matches(dateTimePattern)) {
            return new Date(dtf.parse(p.getText()).getTime());
        }
        return null;
    }

}
