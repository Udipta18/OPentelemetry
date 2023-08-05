package com.opentelemetry.Dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bancs.opentelemetryconfig.OpentelemetryConfig;
import com.opentelemetry.Config.GlobalClass;


@Component
public class OpentelemetryDao {
	
	
	@Autowired
	private OpentelemetryConfig opentelemetryConfig;

	// private Opente
	@Autowired
	private GlobalClass gbClass;

}
