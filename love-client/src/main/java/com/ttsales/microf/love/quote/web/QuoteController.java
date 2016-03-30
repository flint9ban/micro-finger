package com.ttsales.microf.love.quote.web;

import com.ttsales.microf.love.quote.domain.QueryInfo;
import com.ttsales.microf.love.quote.service.QuoteService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    @RequestMapping("h1")
    public String h1(){
        return "quote/h1";
    }

    @RequestMapping("report")
    public String report(Model model,QueryInfo queryInfo){
        JSONObject json = quoteService.query(queryInfo);
        model.addAttribute("selfBrands",json.get("selfBrand"));
        model.addAttribute("competeBrands",json.get("competeBrand"));
        return "quote/report";
    }

    public List<JSONObject> wrapData(){
        List<JSONObject> list =  IntStream.range(0,5).mapToObj(index->{
            JSONObject json = new JSONObject();
            json.put("brandName","大众"+index);
            json.put("cxName","车系"+index);
            json.put("model","车型"+index);
            json.put("lowestCount",6-index);
            json.put("lowestPrice",12-index);
            json.put("price",15-index);
            json.put("decrease","42%");
            return json;
        }).collect(Collectors.toList());
        return list;
    }

    public List<JSONObject> wrapData1(){
        List<JSONObject> list =  IntStream.range(0,5).mapToObj(index->{
            JSONObject json = new JSONObject();
            json.put("brandName","大众"+index);
            json.put("cxName","自车系"+index);
            json.put("model","自车型"+index);
            json.put("lowestCount",7-index);
            json.put("lowestPrice",11.25-index);
            json.put("price",15.38-index);
            json.put("decrease","36%");
            return json;
        }).collect(Collectors.toList());
        return list;
    }


}
