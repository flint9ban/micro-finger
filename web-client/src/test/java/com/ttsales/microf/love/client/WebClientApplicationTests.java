package com.ttsales.microf.love.client;

import com.ttsales.microf.love.client.weixin.MPApi;
import com.ttsales.microf.love.client.weixin.dto.NewsMaterial;
import com.ttsales.microf.love.util.WXApiException;
import org.apache.http.HttpException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebClientApplication.class)
@WebAppConfiguration
public class WebClientApplicationTests {

	@Test
	public void contextLoads() {
	}



}
