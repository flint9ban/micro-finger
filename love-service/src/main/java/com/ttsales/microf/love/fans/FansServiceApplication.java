package com.ttsales.microf.love.fans;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Created by liyi on 2016/3/8.
 */

@SpringBootApplication
@EnableDiscoveryClient
public class FansServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FansServiceApplication.class, args);
    }
}
