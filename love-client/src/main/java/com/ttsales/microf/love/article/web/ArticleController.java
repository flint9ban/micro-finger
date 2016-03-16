package com.ttsales.microf.love.article.web;

import com.ttsales.microf.love.article.domain.Article;
import com.ttsales.microf.love.article.domain.ArticleTag;
import com.ttsales.microf.love.article.service.ArticleService;
import com.ttsales.microf.love.tag.domain.Tag;
import com.ttsales.microf.love.tag.service.TagService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by liyi on 16/3/15.
 */
@Controller
@RequestMapping("article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private TagService tagService;

    @RequestMapping("/init")
    public String init(){
        try {
            articleService.sychnoizeArticle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "article/article";

    }

    @RequestMapping("/query")
    @ResponseBody
    public JSONObject query(Integer page,Integer rows,String title,Date startDate,Date endDate){
        if (page == null||rows==null) {
            page = 0;
            rows = 10;
        }
        PageRequest pageRequest = new PageRequest(page,rows);
        Page<Article> articles = articleService.queryArticle(pageRequest,title,startDate,endDate);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total",articles.getTotalElements());
        jsonObject.put("rows",articles.getContent());
        return jsonObject;
    }

    @RequestMapping(value = "send",method = RequestMethod.POST)
    public void send(Long articleId,String mediaId){
        List<Long> tagIds = articleService.getArticleTags(articleId).stream().map(ArticleTag::getTagId).collect(Collectors.toList());
        articleService.sendArticleByTags(mediaId,tagIds);
    }

    @RequestMapping(value = "getQrcodeTicket",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getQrcodeTicket(Long articleId)  {
        JSONObject json = new JSONObject();
        try {
            String qrcode = articleService.createQrcodeTicket(articleId);
            json.put("qrcode",qrcode);
        } catch (Exception e) {
            json.put("error","系统错误");
        }
        return json;
    }

    @RequestMapping(value = "findTagByName")
    @ResponseBody
    public List<Tag> findTagByName(String tagName){
        return tagService.queryTags(tagName,null);
    }

    @RequestMapping(value = "setTagTypeCommon",method =RequestMethod.POST)
    public void setTagTypeCommon(String tagIds){

    }

    @RequestMapping(value = "update",method = RequestMethod.POST)
    public void update(Long articleId,String tagIds,String tip,String url){
        Article article = articleService.getArticle(articleId);
        article.setTip(tip);
        article.setUrl(url);
        List<Long> tags = null;
        if (tagIds != null) {
            tags = Arrays.asList(tagIds.split(",")).stream().map(Long::parseLong).collect(Collectors.toList());
        }
        articleService.updateArticleTags(articleId,tags);
    }


}
