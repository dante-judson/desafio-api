package com.judson.desafio.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication//(exclude = {SecurityAutoConfiguration.class })
@ComponentScan(basePackages = {"com.judson.desafio"})
public class AppDesafioApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppDesafioApiApplication.class, args);
	}

}
