package com.opentelemetry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@ComponentScan(basePackages = {"com.opentelemetry.*", "com.bancs.*"}) 
public class OpetelemetryBancsApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpetelemetryBancsApplication.class, args);
	}

}
