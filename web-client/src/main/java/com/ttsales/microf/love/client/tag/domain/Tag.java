package com.ttsales.microf.love.client.tag.domain;

import lombok.Data;

import java.util.List;

/**
 * Created by liyi on 16/3/13.
 */

@Data
public class Tag {

    private Long id;

    private String name;

    private String containerNames;

    private List<Long> containerIds;
}
