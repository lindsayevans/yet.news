package news.yet.web.db;

import java.time.OffsetDateTime;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

public class OffsetDateTimeWriteConverter implements Converter<OffsetDateTime, Date> {
    @Override
    public Date convert(@NonNull OffsetDateTime offsetDateTime) {
        return Date.from(offsetDateTime.toInstant());
    }
}