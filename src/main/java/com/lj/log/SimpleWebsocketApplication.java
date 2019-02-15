package com.lj.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Configuration
@EnableScheduling
public class SimpleWebsocketApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleWebsocketApplication.class, args);
	}

}
