package com.remototech.remototechapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RemotoTechApiApplication {

	public static void main(String[] args) {
		SpringApplication.run( RemotoTechApiApplication.class, args );
	}

}
