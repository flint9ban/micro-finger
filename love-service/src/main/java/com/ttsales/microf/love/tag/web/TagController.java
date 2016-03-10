package com.ttsales.microf.love.tag.web;

import com.ttsales.microf.love.tag.domain.Tag;
import com.ttsales.microf.love.tag.service.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by liyi on 2016/3/7.
 */

@RestController
@RequestMapping("tagController/tags")
public class TagController {

    @Autowired
    private TagRepository tagRepository;

    @RequestMapping(method = RequestMethod.GET)
    public List<Tag> getAll(){
        System.out.println("call in tag controller");
        return tagRepository.findAll();
    }
}
