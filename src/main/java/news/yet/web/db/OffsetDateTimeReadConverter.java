package news.yet.web.db;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

public class OffsetDateTimeReadConverter implements Converter<Date, OffsetDateTime> {
    @Override
    public OffsetDateTime convert(@NonNull Date date) {
        return date.toInstant().atOffset(ZoneOffset.UTC);
    }
}