package com.ttsales.microf.love.client;

import com.ttsales.microf.love.client.weixin.MPApi;
import com.ttsales.microf.love.client.weixin.dto.NewsMaterial;
import com.ttsales.microf.love.util.WXApiException;
import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class WebClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebClientApplication.class, args);
	}


}
@RestController
class MaterialController{

	@Autowired
	private MPApi mpApi;

	@RequestMapping
	public List<NewsMaterial> getMaterial() throws WXApiException, HttpException {
		return mpApi.getNewsMaterials();
	}

}