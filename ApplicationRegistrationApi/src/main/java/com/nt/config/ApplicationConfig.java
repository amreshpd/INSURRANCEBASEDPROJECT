package com.nt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApplicationConfig {
	
/*	@Bean(name = "template")
	public RestTemplate createRestTemplate() {
		return new RestTemplate();
	}
	*/
	@Bean(name = "webClient")
	public WebClient createWebClient() {
		return WebClient.create();
	}
}
