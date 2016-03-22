package com.ttsales.microf.love.common.repository;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class SpecificationBuilder {

	public static <T> Specification<T> build(T params) {
		Field[] fields = params.getClass().getDeclaredFields();
		
		return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
			Predicate preSpec = null;
			for (Field field : fields) {
				Predicate spec = getSpec(root, params, builder, field);
				if(spec!=null){
					if(preSpec!=null){
						preSpec = builder.and(spec,preSpec);
					}else{
						preSpec = spec;
					}
				}
			}
			return preSpec;
		};
	}
 	
	static <T> Predicate getSpec(Root<T> root, T params,CriteriaBuilder builder,Field field){
		try {
			field.setAccessible(true);
			Object value = field.get(params);
			String name =  field.getName();
			if(value==null||"".equals(value)){
				return null;
			}
				if(name.equals("name")){
					return  builder.or(builder.like(
							root.get(name),"%"+ value+"%"),builder.like(
							root.get("mobile"),"%"+ value+"%"));

				}else{
					return builder.equal(
							root.get(name), value);
				}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// get date with 00:00:00
	private static Date getStartDate(Date date){
		LocalDateTime date1 = getTruncateDate(date);
		return Date.from(date1.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	//get localdatetime with 00:00:00
	private static LocalDateTime getTruncateDate(Date date){
		Instant instant = Instant.ofEpochMilli(date.getTime());
		LocalDateTime  date1 = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()) ;
		return date1.truncatedTo(ChronoUnit.DAYS);
	}
	//get next date with 00:00:00
	private static Date getEndDate(Date date){
		LocalDateTime truncateDate =  getTruncateDate(date);
		truncateDate = truncateDate.plusDays(1);
		return Date.from(truncateDate.atZone(ZoneId.systemDefault()).toInstant());
	}
	

}
