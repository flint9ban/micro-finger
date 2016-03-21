package com.ttsales.microf.love.domainUtil;

import javax.persistence.AttributeConverter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by liyi on 2016/3/9.
 */
public class LongDateTimeAttrConvertor implements AttributeConverter<LocalDateTime,Long> {

    @Override
    public Long convertToDatabaseColumn(LocalDateTime localDateTime) {
        return LocalDateTimeUtil.convertToLong(localDateTime);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Long aLong) {
        return LocalDateTimeUtil.convertToDateTime(aLong);
    }


}
