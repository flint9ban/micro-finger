package com.ttsales.microf.love.client.weixin.handler;

import com.ttsales.microf.love.weixin.web.support.WXCallbackContext;
import com.ttsales.microf.love.weixin.web.support.WXCallbackHandler;
import org.springframework.stereotype.Component;

/**
 * Created by liyi on 2016/3/10.
 * 扫描文章中的兴趣点二维码
 *
 */
@Component
public class ScanTagHandler implements WXCallbackHandler{

    @Override
    public void handle(WXCallbackContext context) {

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
