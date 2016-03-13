package com.ttsales.microf.love.tag.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by liyi on 2016/3/4.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name ="mf_tag")
public class Tag {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;


}
