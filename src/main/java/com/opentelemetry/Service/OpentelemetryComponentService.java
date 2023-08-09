package com.opentelemetry.Service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bancs.opentelemetryconfig.OpentelemetryConfig;
import com.bancs.opentelemetryconfig.OpentelemetryConfigSingleton;
import com.opentelemetry.Dao.OpenDao;

import io.opentelemetry.context.Context;

@Component
public class OpentelemetryComponentService {
	
	@Autowired
	private OpenDao openDao;

	private Context context;
	
	
	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
	
	 @Autowired
	private OpentelemetryConfig opentelemetryConfig;
	
	public void componentService() {
		HashMap<String, Context> beforeRequest = OpentelemetryConfigSingleton.beforeRequest("opentelemetry_bancs_service1");
		Context context2 = beforeRequest.get("PARENT-CONTEXT");
		this.setContext(context2);
		openDao.dao1();
		HashMap<String,String> attributeMap=new HashMap<>();
		attributeMap.put("PLACE", "COMPONENT SERVICE");
		attributeMap.put("method", "abc");
		OpentelemetryConfigSingleton.afterRequest("opentelemetry_bancs_service1",this.getContext(),attributeMap);
	}

}
