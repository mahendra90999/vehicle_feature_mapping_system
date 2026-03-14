package com.example.jpa.jpa1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class JpatutApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpatutApplication.class, args);
	}

}
