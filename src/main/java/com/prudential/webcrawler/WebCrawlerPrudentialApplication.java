package com.prudential.webcrawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.prudential.webcrawler")

public class WebCrawlerPrudentialApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(WebCrawlerPrudentialApplication.class, args);
	}
}
