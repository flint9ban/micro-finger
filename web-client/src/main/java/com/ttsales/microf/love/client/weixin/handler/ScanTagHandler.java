package com.ttsales.microf.love.client.weixin.handler;


import com.netflix.discovery.converters.Auto;
import com.sun.tools.javac.tree.JCTree;
import com.ttsales.microf.love.client.article.domain.Article;
import com.ttsales.microf.love.client.article.domain.ArticleTag;
import com.ttsales.microf.love.client.article.service.ArticleService;
import com.ttsales.microf.love.client.fans.service.FansService;
import com.ttsales.microf.love.client.qrcode.Qrcode;
import com.ttsales.microf.love.client.qrcode.service.QrcodeService;
import com.ttsales.microf.love.client.tag.domain.Tag;
import com.ttsales.microf.love.client.tag.domain.TagContainer;
import com.ttsales.microf.love.client.tag.service.TagService;
import com.ttsales.microf.love.client.weixin.web.support.WXCallbackContext;
import com.ttsales.microf.love.client.weixin.web.support.WXCallbackHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Table;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by liyi on 2016/3/10.
 * 扫描文章中的兴趣点二维码
 *
 */
@Component
public class ScanTagHandler implements WXCallbackHandler {

    @Autowired
    private QrcodeService qrcodeService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private TagService tagService;

    @Autowired
    private FansService fansService;

    @Override
    public void handle(WXCallbackContext context) {
        String eventKey = context.readChildValue(context.readRoot(),"EventKey");
        String ticket = context.readChildValue(context.readRoot(),"Ticket");
        String openId = context.readFromUserName();
        List<Long> tags = null;
        if(eventKey.startsWith("qrscene_")){
            eventKey = eventKey.substring("qrscene_".length());
        }
        Qrcode qrcode = qrcodeService.getQrcode(eventKey);
        if (qrcode != null) {
            if(Qrcode.REF_TYPE_ARTICLE.equals(qrcode.getRefType())){
                Article article = articleService.getArticleByTicket(ticket);
                tags = articleService.getArticleTags(article.getId())
                                .stream().map(ArticleTag::getTagId).collect(Collectors.toList());

            }else if(Qrcode.REF_TYPE_TAG_CONTAINER.equals(qrcode.getRefType())){
                tags = tagService.getTagContainer(ticket).stream().map(TagContainer::getTagId)
                        .collect(Collectors.toList());
            }
        }
        fansService.createFansTag(openId,tags);
    }

    @Override
    public boolean accept(WXCallbackContext context) {
        if (!"event".equals(context.readMsgType())) {
            return false;
        }
        String eventType = context.readChildValue(context.readRoot(), "Event");
        if("SCAN".equals(eventType)){
            return true;
        }
        if("subscribe".equals(eventType)){
            String eventKey = context.readChildValue(context.readRoot(),"EventKey");
            return eventKey!=null&&eventKey.startsWith("qrscene_");
        }
        return false;
    }
}
