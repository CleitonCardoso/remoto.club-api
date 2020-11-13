package com.remototech.remototechapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class RemotoTechApiApplication {

	public static void main(String[] args) {
		SpringApplication.run( RemotoTechApiApplication.class, args );
	}

}
