package com.ttsales.microf.love;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class LoveClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoveClientApplication.class, args);
	}
}
