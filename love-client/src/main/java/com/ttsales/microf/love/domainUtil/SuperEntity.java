package com.ttsales.microf.love.domainUtil;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by liyi on 2016/3/9.
 */
@Data
@MappedSuperclass
public class SuperEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "create_at")
//    @Convert(converter = LongDateTimeAttrConvertor.class)
    private Long creatAt;

    @Column(name="last_update_at",updatable =false )
//    @Convert(converter = LongDateTimeAttrConvertor.class)
    private Long lastUpdateAt;

    @Column
    private Integer isdel;

    public static final Integer ISDEL_N=0;

    @PrePersist
    void onCreate(){
        this.setCreatAt(System.currentTimeMillis());
        this.setLastUpdateAt(System.currentTimeMillis());
        this.setIsdel(ISDEL_N);
    }

    @PreUpdate
    void onUpdate(){
        this.setLastUpdateAt(System.currentTimeMillis());
    }

}
