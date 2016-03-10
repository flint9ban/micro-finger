package com.ttsales.microf.love;

import com.ttsales.microf.love.tag.domain.Tag;
import com.ttsales.microf.love.tag.service.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class LoveServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(LoveServiceApplication.class, args);
	}

}
@Component
class DummyAR implements ApplicationRunner {

	@Autowired
	private TagRepository tagRepository;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		Stream.of("Josh", "Dave", "Stephane", "Mark", "Phil")
				.forEach(x -> tagRepository.save(new Tag(null,x)));
	}
}