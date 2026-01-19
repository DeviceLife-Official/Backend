package com.devicelife.devicelife_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling //무야호
public class DevicelifeApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevicelifeApiApplication.class, args);
	}

}
