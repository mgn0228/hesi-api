package com.test.back.hesi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HesiApplication {

	public static void main(String[] args) {
		SpringApplication.run(HesiApplication.class, args);
	}

}
