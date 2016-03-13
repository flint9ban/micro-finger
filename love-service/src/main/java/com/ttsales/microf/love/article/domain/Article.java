package com.ttsales.microf.love.article.domain;

import com.ttsales.microf.love.domainUtil.SuperEntity;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by liyi on 2016/3/9.
 */
@Data
@Entity
public class Article extends SuperEntity{

    @Column(name="media_id")
    private String mediaId;

    private String title,content;

    @Column(name = "qrcode_ticket")
    private String qrcodeTicket;

    @Column(name="send_time")
    private LocalDateTime sendTime;

    //重新从微信端获取文章信息并更新的时间
    @Column(name="reload_time")
    private LocalDateTime reloadTime;

}
