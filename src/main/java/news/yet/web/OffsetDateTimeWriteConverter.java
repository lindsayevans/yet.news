package news.yet.web;

import java.time.OffsetDateTime;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

public class OffsetDateTimeWriteConverter implements Converter<OffsetDateTime, Date> {
    @Override
    public Date convert(OffsetDateTime offsetDateTime) {
        return Date.from(offsetDateTime.toInstant());
    }
}