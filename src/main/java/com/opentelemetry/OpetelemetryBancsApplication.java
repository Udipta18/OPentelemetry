package com.opentelemetry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

import com.opentelemetry.Service.OpentelemetryService;

@SpringBootApplication
@ComponentScan(basePackages = {"com.opentelemetry.*", "com.bancs.*"}) 
public class OpetelemetryBancsApplication implements CommandLineRunner {
	
	
	@Autowired
	private OpentelemetryService opentelemetryService;

	public static void main(String[] args) {
		SpringApplication.run(OpetelemetryBancsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
		opentelemetryService.getResponseService();
		  
		
	}

}
