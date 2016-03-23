package com.ttsales.microf.love.weixin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by liyi on 2016/3/22.
 */
@Data
@AllArgsConstructor
public class MaterialItem {

    private String title;

    private String content;

    private String contentSourceUrl;

    private Integer itemIndex;
}
