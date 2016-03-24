package com.ttsales.microf.love.weixin.handler;


import com.ttsales.microf.love.article.domain.Article;
import com.ttsales.microf.love.article.domain.ArticleTag;
import com.ttsales.microf.love.article.service.ArticleService;
import com.ttsales.microf.love.fans.service.FansService;
import com.ttsales.microf.love.keyword.domain.Keyword;
import com.ttsales.microf.love.keyword.service.KeywordRepository;
import com.ttsales.microf.love.qrcode.domain.QrCode;
import com.ttsales.microf.love.qrcode.service.QrcodeService;
import com.ttsales.microf.love.tag.domain.TagContainer;
import com.ttsales.microf.love.tag.service.TagService;
import com.ttsales.microf.love.util.WXApiException;
import com.ttsales.microf.love.weixin.message.PubResponseMessage;
import com.ttsales.microf.love.weixin.message.TextMsgContent;
import com.ttsales.microf.love.weixin.web.support.WXCallbackContext;
import com.ttsales.microf.love.weixin.web.support.WXCallbackHandler;
import javafx.util.converter.IntegerStringConverter;
import org.apache.catalina.util.URLEncoder;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.IntRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by liyi on 2016/3/10.
 * 扫描文章中的兴趣点二维码
 *
 */
@Component
public class FocusHandler implements WXCallbackHandler {

    @Autowired
    private KeywordRepository keywordRepository;

    @Override
    public void handle(WXCallbackContext context) {
        String content = "/:handclap恭喜您发现汽车营销行业新大陆——微车数据服务平台！\n\n回复：\n";
        List<Keyword> keywords =  keywordRepository.findAllBySubSend(Keyword.SUBSEND_Y);
        content = keywords.stream()
                .reduce(content
                        ,(item,keyword)->item+=keyword.getKeyword()+"--"+keyword.getName()+"\n"
                        ,(item1,item2)->item1);
        content+="\n" +
                "/::+汽车品牌销量、报价、促销活动、舆情等信息数据，点击菜单“我的”进行订阅和留言！\n" +
                "/:,@-D有任何需求或者不满，欢迎随时骚扰哦！/:rose";
        try {
            context.writeXML(getResponseMessageXML(context,content));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WXApiException e) {
            e.printStackTrace();
        }
    }


    private String getResponseMessageXML(WXCallbackContext context,String content ) throws WXApiException {
        PubResponseMessage message = new PubResponseMessage();
        message.setFromUserName(context.readToUserName());
        message.setToUserName(context.readFromUserName());
        message.setCreateTime(System.currentTimeMillis() / 1000);
        TextMsgContent textMsgContent=new TextMsgContent();
        textMsgContent.setContent(content);
        message.setContent(textMsgContent);
        return message.toMsgString();
    }

    @Override
    public boolean accept(WXCallbackContext context) {
        if (!"event".equals(context.readMsgType())) {
            return false;
        }
        String eventType = context.readChildValue(context.readRoot(), "Event");
        if("subscribe".equals(eventType)){
            String eventKey = context.readChildValue(context.readRoot(),"EventKey");
            return eventKey==null||"".equals(eventKey);
        }
        return false;
    }

}
