package com.ttsales.microf.love.tag.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by liyi on 2016/3/4.
 */

@Data
@Entity
@Table(name="dat_tag_container_ref")
public class TagContainer {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="tag_id")
    private Long tagId;

    @Column(name="container_id")
    private Long containerId;
    
}
