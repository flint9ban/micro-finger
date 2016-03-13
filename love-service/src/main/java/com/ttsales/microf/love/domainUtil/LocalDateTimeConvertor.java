package com.ttsales.microf.love.domainUtil;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by liyi on 2016/3/9.
 */
@Converter(autoApply = true)
public class LocalDateTimeConvertor implements AttributeConverter<LocalDateTime,Timestamp>{
    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return Timestamp.valueOf(dateTime);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return timestamp.toLocalDateTime();
    }
}
