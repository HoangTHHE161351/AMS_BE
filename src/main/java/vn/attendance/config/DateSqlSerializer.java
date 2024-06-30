package vn.attendance.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateSqlSerializer extends JsonSerializer<Date> {

    private DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    private DateFormat dtf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (date == null) {
            jsonGenerator.writeNull();
        } else {
            String str;
            if (new Date(date.getTime()).getHours() > 0) {
                str = dtf.format(date);
                ;
            } else {
                str = df.format(date);
                ;
            }
            jsonGenerator.writeString(str);
        }
    }
}
