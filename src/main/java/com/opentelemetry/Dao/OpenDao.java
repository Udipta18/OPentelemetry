package com.opentelemetry.Dao;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bancs.opentelemetryconfig.OpentelemetryConfig;
import com.bancs.opentelemetryconfig.OpentelemetryConfigSingleton;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;

@Component
public class OpenDao {
	
	private Context context;
	
	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}


	@Autowired
	private OpentelemetryConfig opentelemetryConfig;
	
	private Tracer buildTracer() {
		return opentelemetryConfig.returnTelemetry().getTracer("opentelemetry-bancs_service_dao", "1.0.0");
	}
	
	public String dao1() {
		/*
		 * Tracer buildTracer = this.buildTracer();
		 * 
		 * 
		 * 
		 * //Object context = gbClass.getContext();
		 * //System.out.println(context.toString());
		 * 
		 * // ContextStorage contextStorage = ContextStorage.get(); //
		 * contextStorage.attach((Context)context);
		 * 
		 * Context current = Context.current();
		 * 
		 * Span span = buildTracer.spanBuilder("opentelemetry controller span dao 1")
		 * .setParent(current).setSpanKind(SpanKind.SERVER).startSpan();
		 * 
		 * SpanContext spanContext = span.getSpanContext();
		 */
		HashMap<String, Context> beforeRequest = OpentelemetryConfigSingleton.beforeRequest("opentelemetry_bancs_dao");
		this.setContext(beforeRequest.get("PARENT-CONTEXT"));
		HashMap<String,String> attributeMap=new HashMap<>();
		attributeMap.put("PLACE", "DAO1");
		attributeMap.put("method", "abc");
		OpentelemetryConfigSingleton.afterRequest("opentelemetry_bancs_dao",this.getContext(),attributeMap);
		
		//span.end();
		return "dAO 1";
	}

	
	public String dao2() {
         Tracer buildTracer = this.buildTracer();

		

		//Object context = gbClass.getContext();
		//System.out.println(context.toString());
		
//		ContextStorage contextStorage = ContextStorage.get();
//		contextStorage.attach((Context)context);
		
		Context current = Context.current();
		
		Span span = buildTracer.spanBuilder("opentelemetry controller span Service1")
				.setParent(current).setSpanKind(SpanKind.SERVER).startSpan();
		
		span.end();
		return "dAO 2";
	}

}
