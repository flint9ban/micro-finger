package com.ttsales.microf.love.article.web;

import com.ttsales.microf.love.article.domain.Article;
import com.ttsales.microf.love.article.domain.ArticleTag;
import com.ttsales.microf.love.article.service.ArticleService;
import com.ttsales.microf.love.tag.domain.Container;
import com.ttsales.microf.love.tag.domain.ContainerType;
import com.ttsales.microf.love.tag.domain.Tag;
import com.ttsales.microf.love.tag.service.TagService;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
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

    @RequestMapping("/editAritcle")
    public String toEdit(Model model, Long id){
        model.addAttribute("id",id);
        List<Container> containers = tagService.findCommonContainer();
        List<JSONObject> tagContainers =  containers.stream().map(container  -> {
            List<Tag> tags = tagService.findTagByContainerId(container);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",container.getId());
            jsonObject.put("tags",tags);
            return jsonObject;
        }).collect(Collectors.toList());
        model.addAttribute("tagContainers",tagContainers);
        return "article/editArticle";
    }

    @RequestMapping("/getArticle")
    @ResponseBody
    public JSONObject getArticle(Long articleId){
        Article article = articleService.getArticle(articleId);

        List<ArticleTag> articleTags = articleService.getArticleTags(articleId);
        List<JSONObject> tags = articleTags.stream().map(articleTag -> {
            JSONObject jsonObject = new JSONObject();
            Tag tag = tagService.getTag(articleTag.getTagId());
            jsonObject.put("text",tag.getName());
            jsonObject.put("value",tag.getId());
            return jsonObject;
        }).collect(Collectors.toList());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("article",article);
        jsonObject.put("tags",tags);
        return jsonObject;
    }

    @RequestMapping("/query")
    @ResponseBody
    public JSONObject query(Integer page,Integer rows,String title,Date startDate,Date endDate){
        if (page == null||rows==null) {
            page = 1;
            rows = 10;
        }
        Sort sort = new Sort(Sort.Direction.DESC,"creatAt");
        PageRequest pageRequest = new PageRequest(page-1,rows,sort);
        Page<Article> articles = articleService.queryArticle(pageRequest,title,startDate,endDate);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total",articles.getTotalElements());
        jsonObject.put("rows",articles.getContent().stream().map(this::convertToJSON).collect(Collectors.toList()));
        return jsonObject;
    }

    private JSONObject convertToJSON(Article article){
        JSONObject json = new JSONObject();
        json.put("id",article.getId());
        json.put("content",article.getContent());
        json.put("mediaId",article.getMediaId());
        json.put("qrcodeTicke",article.getQrcodeTicket());
        json.put("reloadTime",getDate(article.getReloadTime()));
        json.put("sendTime",getDate(article.getSendTime()));
        json.put("tip",article.getTip());
        json.put("title",article.getTitle());
        json.put("url",article.getUrl());
        json.put("creatAt",getDate(article.getCreatAt()));
        json.put("lastUpdateAt",getDate(article.getLastUpdateAt()));
        return  json;
    }

    private String getDate(LocalDateTime dateTime){
        if(dateTime!=null)
            return dateTime.getYear()+"-"+dateTime.getMonthValue()+"-"+dateTime.getDayOfMonth();

        return null;
    }

    @RequestMapping(value = "send",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject send(Long articleId,String mediaId){
        List<Long> tagIds = articleService.getArticleTags(articleId).stream().map(ArticleTag::getTagId).collect(Collectors.toList());
        articleService.sendArticleByTags(articleId,mediaId,tagIds);
        return new JSONObject();
    }

    @RequestMapping(value = "getQrcodeTicket",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getQrcodeTicket(Long articleId)  {
        JSONObject json = new JSONObject();
        try {
            String qrcode = articleService.createQrcodeTicket(articleId);
            json.put("qrcodeTicket",qrcode);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("error","系统错误");
        }
        return json;
    }

    @RequestMapping(value = "findTagByName")
    @ResponseBody
    public List<JSONObject> findTagByName(String name){
        return tagService.queryTags(name,null).stream()
                .map(tag->{
                    JSONObject json = new JSONObject();
                    json.put("text",tag.getName());
                    json.put("value",tag.getId());
                    return json;
                }).collect(Collectors.toList());
    }

    @RequestMapping(value = "setCommon",method =RequestMethod.POST)
    @ResponseBody
    public JSONObject setTagTypeCommon(String tagIds){
        List<Long> tags = null;
        if (tagIds != null&&tagIds.length()>0) {
            tags = Arrays.asList(tagIds.split(",")).stream().map(Long::parseLong).collect(Collectors.toList());
        }
        Container container = new Container();
        container.setName("COMMON");
        container.setContainerType(ContainerType.COMMON);
        Long containerId = tagService.createCotnainerWithTags(container,tags);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("containerId",containerId);
        return jsonObject;
    }

    @RequestMapping(value = "update",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject update(Long articleId,String tagIds,String tip,String url){
        Article article = articleService.getArticle(articleId);
        article.setTip(tip);
        article.setUrl(url);
        List<Long> tags = null;
        if (tagIds != null&&tagIds.length()>0) {
            tags = Arrays.asList(tagIds.split(",")).stream().map(Long::parseLong).collect(Collectors.toList());
        }
        articleService.updateArticleTags(articleId,tags);
        return new JSONObject();
    }

    @RequestMapping(value = "removeContainer",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject removeContainer(Long containerId){
        tagService.removeContainer(containerId);
        return new JSONObject();
    }

    public static void main(String[] args) {
        LocalDateTime dateTime = LocalDateTime.now();
        System.out.print(dateTime.getYear()+"-"+dateTime.getMonthValue()+"-"+dateTime.getDayOfMonth());
    }

}
