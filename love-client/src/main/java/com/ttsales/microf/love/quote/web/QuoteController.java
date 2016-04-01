package com.ttsales.microf.love.quote.web;

import com.ttsales.microf.love.quote.domain.QueryInfo;
import com.ttsales.microf.love.quote.service.QuoteService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by liyi on 2016/3/24.
 */
@Controller
@RequestMapping("quote")
public class QuoteController {

    @Autowired
    private QuoteService quoteService;

    @RequestMapping("index")
    public String index(Model model, String openId){
        QueryInfo queryInfo = quoteService.queryQueryInfo(openId);
        model.addAttribute("queryInfo",queryInfo);
        JSONObject quote = quoteService.queryQuote(queryInfo.getOpenId());
        model.addAttribute("quote",quote);
        return "quote/index";
    }

    @RequestMapping("report")
    public String report(Model model,QueryInfo queryInfo){
        JSONObject json = quoteService.query(queryInfo);
        model.addAttribute("selfBrands",json.get("selfBrand"));
        model.addAttribute("competeBrands",json.get("competeBrand"));
        return "quote/report";
    }




}
