package com.devicelife.devicelife_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DevicelifeApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevicelifeApiApplication.class, args);
	}

}
