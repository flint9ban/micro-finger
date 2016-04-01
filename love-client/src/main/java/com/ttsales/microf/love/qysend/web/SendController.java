package com.ttsales.microf.love.qysend.web;

import com.ttsales.microf.love.qysend.domain.Log;
import com.ttsales.microf.love.qysend.domain.OrgMember;
import com.ttsales.microf.love.qysend.repository.LogRepository;
import com.ttsales.microf.love.qysend.repository.OrgMemberRepository;
import com.ttsales.microf.love.qysend.repository.OrgStructureRepository;
import com.ttsales.microf.love.util.WXApiException;
import com.ttsales.microf.love.weixin.MPApi;
import com.ttsales.microf.love.weixin.MPApiConfig;
import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by liyi on 2016/3/31.
 *
 */
@Controller
public class SendController {

    @Autowired
    private OrgStructureRepository orgStructureRepository;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private OrgMemberRepository orgMemberRepository;

    @Autowired
    private MPApi mpApi;

    @Autowired
    private MPApiConfig mpApiConfig;


    @RequestMapping("send")
    @ResponseBody
    public String send(){
        List<OrgMember> members =  orgMemberRepository.getAllStoreMember();
        members.parallelStream().forEach(orgMember -> {
            Integer state = Log.STATE_Y;
            try {
                sendMsg(orgMember);
            } catch (Exception e) {
                e.printStackTrace();
                state = Log.STATE_N;
            }
            Log log = new Log();
            log.setMemberId(orgMember.getMemberId());
            log.setStoreId(orgMember.getStoreId());
            log.setStoreName(orgMember.getStoreName());
            log.setState(state);
            logRepository.save(log);
        });
        return "发送成功！";
    }

    private void sendMsg(OrgMember orgMember) throws WXApiException, HttpException {
        String title = orgMember.getStoreName()+"：如何从报价上抢占客户";
        String desc=  "直击客户心坎的报价技巧，看完这篇就够了";
        String url = mpApiConfig.getAppUrl()+"/auth/check?scope=snsapi_base&target_uri=quoteHome%2finit%3fmemberId%3d"+orgMember.getMemberId()+"&redirect";
        String pic_url=mpApiConfig.getAppUrl()+"img/share.jpg";
        mpApi.sendQyMsg(orgMember.getMemberId(),1,title,desc,url,pic_url);
    }

    @RequestMapping("send-user")
    @ResponseBody
    public String sendByUser(String user) throws WXApiException, HttpException {
        String title = "大众：如何从报价上抢占客户";
        String desc=  "直击客户心坎的报价技巧，看完这篇就够了";
        String url = mpApiConfig.getAppUrl()+"/auth/check?scope=snsapi_base&target_uri=quoteHome%2finit%3fmemberId%3d"+user+"&redirect";
        String pic_url=mpApiConfig.getAppUrl()+"img/share.jpg";
        mpApi.sendQyMsg(user,1,title,desc,url,pic_url);
        return "发送成功！";
    }


}
