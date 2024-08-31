package dev.jihogrammer.redistudy.localsearch;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LocalsearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(LocalsearchApplication.class, args);
	}

	@Bean
	public String apiKey(@Value("${localsearch.routo.api-key}") final String apiKey) {
		return apiKey;
	}

}
