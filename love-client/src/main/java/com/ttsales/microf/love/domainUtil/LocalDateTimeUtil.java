package com.ttsales.microf.love.domainUtil;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Created by liyi on 2016/3/9.
 */
public class LocalDateTimeUtil {

    public static LocalDateTime convertToDateTime(Long timeInMill){
        if (timeInMill==null){
            return null;
        }
        Instant instant = Instant.ofEpochMilli(timeInMill);
        return  LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static Long convertToLong(LocalDateTime dateTime){
        if (dateTime==null){
            return null;
        }
        Instant instant = dateTime.atZone(ZoneId.systemDefault()).toInstant();
        return instant.toEpochMilli();
    }
}
