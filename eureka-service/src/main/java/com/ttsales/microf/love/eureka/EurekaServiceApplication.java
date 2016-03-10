package com.ttsales.microf.love.eureka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(EurekaServiceApplication.class, args);
	}

}

@RefreshScope
@RestController
class MessageRestController {

	@Value("${messages}")
	private String messages;

	@RequestMapping("/message")
	String message() {
		return this.messages;
	}
}
