package com.shirish.finra;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.shirish.finra.services.FileUploadService;

@SpringBootApplication
// @EnableConfigurationProperties(ApplicationProperties.class)
@ComponentScan
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

	}

	@Bean
	CommandLineRunner init(FileUploadService uploadService) {
		return (args) -> {
			uploadService.cleanUp();
			uploadService.init();
		};
	}

}
