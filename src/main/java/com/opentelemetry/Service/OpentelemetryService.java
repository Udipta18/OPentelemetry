package com.opentelemetry.Service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancs.opentelemetryconfig.OpentelemetryConfig;
import com.bancs.opentelemetryconfig.OpentelemetryConfigSingleton;
import com.opentelemetry.Config.GlobalClass;
import com.opentelemetry.Dao.OpenDao;
import io.opentelemetry.context.Context;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;

import io.opentelemetry.context.ContextStorage;

@Service
public class OpentelemetryService {

	@Autowired
	private OpentelemetryConfig opentelemetryConfig;
	
	
	@Autowired
	private OpentelemetryComponentService opentelemetryComponentService;
	
	
	private Context context;
	
	
	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	@Autowired
	private OpenDao openDao;

	// private Opente
	@Autowired
	private GlobalClass gbClass;

	private Tracer buildTracer() {
		return OpentelemetryConfigSingleton.returnTelemetry().getTracer("opentelemetry-bancs_service", "1.0.0");
	}

	@SuppressWarnings("static-access")
	public void getResponseService() {
		Tracer buildTracer = this.buildTracer();

	

	//Object context = gbClass.getContext();
	//System.out.println(context.toString());
	
//	ContextStorage contextStorage = ContextStorage.get();
//	contextStorage.attach((Context)context);
	
	/*
	 * Context current = Context.current();
	 * 
	 * Span span = buildTracer.spanBuilder("opentelemetry controller span Service")
	 * .setParent(current).setSpanKind(SpanKind.SERVER).startSpan();
	 * span.getSpanContext(); Context with = current.with(span);
	 */
	


	
	/*
	 * ContextStorage contextStorage = ContextStorage.get();
	 * contextStorage.attach((Context)with);
	 */
	HashMap<String, Context> beforeRequest = OpentelemetryConfigSingleton.beforeRequest("opentelemetry_bancs_service1");
	Context context2 = beforeRequest.get("PARENT-CONTEXT");
	this.setContext(context2);
	
	
	try {
		openDao.dao1();
		
		/*
		 * SpanContext spanContext3 = span.getSpanContext(); Context current2 =
		 * Context.current(); System.out.println("hello");
		 */
		//return new ResponseEntity<String>("ALL WORKING", HttpStatus.OK);
	}finally {
		/*
		 * Context with2 = current.with(span);
		 * 
		 * 
		 * 
		 * 
		 * ContextStorage contextStorage2= ContextStorage.get();
		 * contextStorage2.attach((Context)with2); Context current4 = Context.current();
		 * span.end();
		 */
		HashMap<String,String> attributeMap=new HashMap<>();
		attributeMap.put("PLACE", "SERVICE");
		attributeMap.put("method", "abc");
		OpentelemetryConfigSingleton.afterRequest("opentelemetry_bancs_service1",this.getContext(),attributeMap);
	}
	
	
	/*
	 * Span span2 =
	 * buildTracer.spanBuilder("opentelemetry controller span Service1 same parent")
	 * .setParent(current).setSpanKind(SpanKind.SERVER).startSpan();
	 */

	HashMap<String, Context> beforeRequest2 = OpentelemetryConfigSingleton.beforeRequest("opentelemetry_bancs_service2");
	Context context3 = beforeRequest.get("PARENT-CONTEXT");
	this.setContext(context3);
	
	opentelemetryComponentService.componentService();
	/*
	 * Object context2 = gbClass.getContext();  
	 * System.out.println(context2.toString());
	 */
	/*
	 * SpanContext spanContext2 = span2.getSpanContext(); span2.end();
	 */
	HashMap<String,String> attributeMap=new HashMap<>();
	attributeMap.put("PLACE", "SERVICE2");
	attributeMap.put("method", "abc");
	OpentelemetryConfigSingleton.afterRequest("opentelemetry_bancs_service2",this.getContext(),attributeMap);
	}

	public void getResponseServicetwo() {
		Tracer buildTracer = this.buildTracer();

		Span span = buildTracer.spanBuilder("opentelemetry controller span Service2")
				.setParent((Context)gbClass.getContext()).setSpanKind(SpanKind.SERVER).startSpan();

		Object context = gbClass.getContext();
		System.out.println(context.toString());
		
		span.end();
	}

}
