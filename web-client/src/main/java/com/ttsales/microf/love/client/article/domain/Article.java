package com.ttsales.microf.love.client.article.domain;

import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDateTime;

/**
 * Created by liyi on 2016/3/11.
 */
@Data
public class Article {

    private Long id;

    private String mediaId;

    private String title,content;

    private String qrcodeTicket;

    private LocalDateTime sendTime;

    //重新从微信端获取文章信息并更新的时间
    private LocalDateTime reloadTime;
}
