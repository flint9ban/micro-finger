package com.ttsales.microf.love.domainUtil;

import javax.persistence.AttributeConverter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
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

    public static void main(String[] args) {
        Long value = System.currentTimeMillis();
        Instant instant = Instant.ofEpochMilli(value);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant,ZoneId.systemDefault());
        System.out.println("args = [" +  value+ "]");
        System.out.println("args = [" +  dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()+ "]");
        System.out.println(new Date(value));
        System.out.println(dateTime);
    }
}
