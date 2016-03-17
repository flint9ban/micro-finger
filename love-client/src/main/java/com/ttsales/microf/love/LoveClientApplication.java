package com.ttsales.microf.love;

import com.ttsales.microf.love.article.service.ArticleService;
import com.ttsales.microf.love.tag.domain.Container;
import com.ttsales.microf.love.tag.domain.ContainerType;
import com.ttsales.microf.love.tag.domain.Tag;
import com.ttsales.microf.love.tag.repository.TagRepository;
import com.ttsales.microf.love.tag.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SpringBootApplication
@EnableDiscoveryClient
public class LoveClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoveClientApplication.class, args);
	}
}

@Component
class DummyAR implements ApplicationRunner {

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private TagService tagService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		List<Tag> tags = Stream.of("Josh", "Dave", "Stephane", "Mark", "Phil","Kyle","Tyler")
				.map(x -> {
					Tag tag = new Tag();
					tag.setName(x);
					return tagRepository.save(tag);
				}).collect(Collectors.toList());
		IntStream.range(0,4).forEach(i->{
			Container container = new Container();
			container.setName("COMMON");
			container.setContainerType(ContainerType.COMMON);
			tagService.createCotnainerWithTags(container,tags.subList(i,i+3).stream().map(Tag::getId).collect(Collectors.toList()));
		});

	}
}