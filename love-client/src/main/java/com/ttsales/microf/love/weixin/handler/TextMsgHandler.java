package com.ttsales.microf.love.weixin.handler;


import com.ttsales.microf.love.keyword.domain.Keyword;
import com.ttsales.microf.love.keyword.service.KeywordRepository;
import com.ttsales.microf.love.util.WXApiException;
import com.ttsales.microf.love.weixin.MPApi;
import com.ttsales.microf.love.weixin.message.PubResponseMessage;
import com.ttsales.microf.love.weixin.message.TextMsgContent;
import com.ttsales.microf.love.weixin.web.support.WXCallbackContext;
import com.ttsales.microf.love.weixin.web.support.WXCallbackHandler;
import org.apache.http.HttpException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Created by liyi on 2016/3/10.
 * 扫描文章中的兴趣点二维码
 *
 */
@Component
public class TextMsgHandler implements WXCallbackHandler {

    @Autowired
    private KeywordRepository keywordRepository;

    @Autowired
    private MPApi mpApi;

    Logger logger =  Logger.getLogger(TextMsgHandler.class);

    @Override
    public void handle(WXCallbackContext context) {
        String openId = context.readFromUserName();
        String keyword = context.readChildValue(context.readRoot(),"Content");
        Keyword word = keywordRepository.findOneByKeyword(keyword);
        if(word!=null){
            String mediaId = word.getMediaId();
            if(mediaId!=null&&!"".equals(mediaId)){
                try {
                    mpApi.sendMpnewsMessage(openId,mediaId);
                } catch (Exception e) {
                    logger.error(e);
                }
            }else{
                List<Keyword> keywords = keywordRepository.findAllByKeywordLike(keyword+"_");
                String content = "回复：\n";
                content = keywords.stream().reduce(content,
                        (item,keywordItem)->item+=keywordItem.getKeyword()+"--"+keywordItem.getName()+"\n",
                        (item1,item2)->item1);
                try {
                    context.writeXML(getResponseMessageXML(context,content));
                } catch (Exception e) {
                    logger.error(e);
                }
            }
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
        return "text".equals(context.readMsgType());
    }
}
