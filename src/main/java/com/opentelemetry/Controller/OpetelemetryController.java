
package com.opentelemetry.Controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bancs.opentelemetryconfig.OpentelemetryConfig;
import com.bancs.opentelemetryconfig.OpentelemetryConfigSingleton;
import com.opentelemetry.Config.GlobalClass;

import com.opentelemetry.Service.OpentelemetryService;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.ContextStorage;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor;
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes;

@RestController
@RequestMapping("/opentelemetry")
public class OpetelemetryController {
	
	
	
    private Context context;
	
	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
	
	@Autowired
	private GlobalClass gbClass;
	
	@Autowired
	private OpentelemetryConfig opentelemetryConfig;
	
	@Autowired
	private OpentelemetryService opentelemetryService;

	/*
	 * private final String endpoint = "http://localhost:5555"; private final String
	 * resourceattr = "service.name=opentelemetry-app,service.version=1.0";
	 * 
	 * Resource resource = Resource.getDefault()
	 * .merge(Resource.create(Attributes.of(ResourceAttributes.SERVICE_NAME,
	 * "logical-service-name")));
	 * 
	 * SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
	 * .addSpanProcessor(
	 * BatchSpanProcessor.builder(OtlpGrpcSpanExporter.builder().setEndpoint(
	 * endpoint).build()).build()) .setResource(resource).build();
	 * 
	 * SdkMeterProvider sdkMeterProvider = SdkMeterProvider.builder()
	 * .registerMetricReader(PeriodicMetricReader
	 * .builder(OtlpGrpcMetricExporter.builder().setEndpoint(endpoint).build()).
	 * build()) .setResource(resource).build();
	 * 
	 * SdkLoggerProvider sdkLoggerProvider = SdkLoggerProvider.builder()
	 * .addLogRecordProcessor(BatchLogRecordProcessor
	 * .builder(OtlpGrpcLogRecordExporter.builder().setEndpoint(endpoint).build()).
	 * build()) .setResource(resource).build();
	 * 
	 * OpenTelemetry openTelemetry =
	 * OpenTelemetrySdk.builder().setTracerProvider(sdkTracerProvider)
	 * .setMeterProvider(sdkMeterProvider).setLoggerProvider(sdkLoggerProvider)
	 * .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.
	 * getInstance())) .buildAndRegisterGlobal();
	 */

	@GetMapping("/get")
	public ResponseEntity<String> getResponse() {
		/*
		 * OpenTelemetry openTelemetry = opentelemetryConfig.returnTelemetry();
		 * 
		 * 
		 * Tracer tracer = openTelemetry.getTracer("opentelemetry-bancs", "1.0.0");
		 * 
		 * 
		 * Span span = tracer.spanBuilder("opentelemetry controller span").startSpan();
		 * Context current = Context.current(); span.getSpanContext(); Context with =
		 * current.with(span);
		 * 
		 * ContextStorage contextStorage = ContextStorage.get();
		 * contextStorage.attach((Context)with);
		 */
		
		
		
			
			//gbClass.setContext(with);
			
			/*
			 * HashMap<String, Context> beforeRequest =
			 * opentelemetryConfig.beforeRequest("opentelemetry_bancs_Controller1");
			 */
		HashMap<String, Context> beforeRequest = OpentelemetryConfigSingleton.beforeRequest("opentelemetry_bancs_Controller1");
		Context context2 = beforeRequest.get("PARENT-CONTEXT");
		this.setContext(context2);
			try {
				opentelemetryService.getResponseService();
				System.out.println("hello");
				return new ResponseEntity<String>("ALL WORKING", HttpStatus.OK);
			}finally {
				//this.opentelemetryConfig.getStack();
				HashMap<String,String> attributeMap=new HashMap<>();
				attributeMap.put("PLACE", "CONTROLLER");
				attributeMap.put("method", "abc");
				OpentelemetryConfigSingleton.afterRequest("opentelemetry_bancs_Controller1",this.getContext(),attributeMap);
			}
			
		
		    
		

		
	}

	
	@GetMapping("/getTwo")
	public ResponseEntity<String> getResponseTwo() {
		OpenTelemetry openTelemetry = OpentelemetryConfigSingleton.returnTelemetry();
		
		
		Tracer tracer =
			    openTelemetry.getTracer("opentelemetry-bancs", "1.0.0");
		
		
		Span span = tracer.spanBuilder("opentelemetry controller span 2").startSpan();
		
		Context current = Context.current();
		
			span.getSpanContext();
			Context with = current.with(span);
			gbClass.setContext(with);
			
			
			try {
				opentelemetryService.getResponseServicetwo();
				System.out.println("hello");
				return new ResponseEntity<String>("ALL WORKING", HttpStatus.OK);
			}finally {
				span.end();
			}
			
		
		    
		

		
	}

}
