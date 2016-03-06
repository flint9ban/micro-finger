package com.ttsales.microf.fans.domain;

import lombok.Data;
import org.aspectj.lang.annotation.DeclareAnnotation;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by liyi on 16/3/6.
 */
@Data
@Entity
@Table(name="fans_info")
public class FansInfo {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String mobile;
}
