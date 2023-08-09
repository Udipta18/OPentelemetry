package com.bancs.opentelemetryconfig;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.ContextStorage;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.logging.LoggingMetricExporter;
import io.opentelemetry.exporter.logging.LoggingSpanExporter;
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.MetricReader;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes;

public class OpentelemetryConfigSingleton {
	
	private static OpentelemetryConfigSingleton instance=null;
	private final static  String endpoint = "http://localhost:5555";
	private final String resourceattr = "service.name=opentelemetry-app,service.version=1.0";
	private static final long METRIC_EXPORT_INTERVAL_MS = 800L;
	static Scope attach;

	
	
	public static  OpentelemetryConfigSingleton getExample(){
	    if(instance==null){
	      synchronized(OpentelemetryConfigSingleton.class)
	      {
	        if(instance==null)
	        {
	        	instance=new OpentelemetryConfigSingleton();
	        }
	      }
	    }
	    return instance;
	}
	
	public static OpenTelemetry returnTelemetry() {

		final String ENDPOINT = "http://localhost:5555";

		Resource resource = Resource.getDefault().merge(
				Resource.create(Attributes.of(ResourceAttributes.SERVICE_NAME, "opentelemetry_bancs_version_1.0.0")));

		/*
		 * SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
		 * .addSpanProcessor(BatchSpanProcessor
		 * .builder(OtlpGrpcSpanExporter.builder().setEndpoint(endpoint).build()).build(
		 * )) .setResource(resource).build();
		 * 
		 * SdkMeterProvider sdkMeterProvider = SdkMeterProvider.builder()
		 * .registerMetricReader(PeriodicMetricReader
		 * .builder(OtlpGrpcMetricExporter.builder().setEndpoint(endpoint).build()).
		 * build()) .setResource(resource).build();
		 */
		/* LoggingSpanExporter.create()) */
		/*
		 * OtlpGrpcSpanExporter.builder().setEndpoint(ENDPOINT).setTimeout(Duration.
		 * ofSeconds(50)).build()) .build())
		 */
		BatchSpanProcessor build = BatchSpanProcessor
				.builder(
						OtlpGrpcSpanExporter.builder().setEndpoint(ENDPOINT).setTimeout(Duration.ofSeconds(50)).build())
				.build();
		/*
		 * SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
		 * 
		 * 
		 * 
		 * .addSpanProcessor(BatchSpanProcessor.builder(LoggingSpanExporter.create()).
		 * build())
		 * 
		 * .addBatchSpanProcessor(
		 * (OtlpGrpcSpanExporter.builder().setEndpoint(endpoint).build()).build())
		 * .setResource(resource) .setIdGenerator(AwsXrayIdGenerator.getInstance())
		 * .build();
		 */

		SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
				.addSpanProcessor(BatchSpanProcessor
						.builder(OtlpGrpcSpanExporter.builder().setEndpoint(endpoint).build()).build())
				 .addSpanProcessor(SimpleSpanProcessor.create(LoggingSpanExporter.create()))
				.setResource(resource).build();

		MetricReader periodicReader = PeriodicMetricReader.builder(LoggingMetricExporter.create())
				.setInterval(Duration.ofMillis(METRIC_EXPORT_INTERVAL_MS)).build();

		// This will be used to create instruments
		SdkMeterProvider sdkMeterProvider = SdkMeterProvider.builder().registerMetricReader(periodicReader).build();

		SdkLoggerProvider sdkLoggerProvider = SdkLoggerProvider.builder()
				.addLogRecordProcessor(BatchLogRecordProcessor
						.builder(OtlpGrpcLogRecordExporter.builder().setEndpoint(endpoint).build()).build())
				.setResource(resource).build();

		OpenTelemetry openTelemetry = OpenTelemetrySdk.builder().setTracerProvider(sdkTracerProvider)
				.setMeterProvider(sdkMeterProvider).setLoggerProvider(sdkLoggerProvider)
				.setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance())).build();

		return openTelemetry;
	}
	
	public static HashMap<String, Context> beforeRequest(String message) {
		HashMap<String, Context> map = new HashMap<>();
		Tracer buildTracer = returnTelemetry().getTracer(message, "1.0.0");
		Context current = Context.current();
		Span span;
		if (current == null || current.equals(" ")) {
			span = buildTracer.spanBuilder(message).setSpanKind(SpanKind.SERVER).startSpan();
		} else {
			span = buildTracer.spanBuilder(message).setAttribute("message", message).setParent(current)
					.setSpanKind(SpanKind.SERVER).startSpan();
		}

		Context with = current.with(span);
		// attach = with.makeCurrent();
		ContextStorage contextStorage = ContextStorage.get();
		attach = contextStorage.attach((Context) with);

		Node node = new Node();
		node.setParent(current);
		node.setCurrent(with);
		if (getStack() != null) {
			Stack<Node> stack3 = getStack();
			stack3.add(node);

		setStack(stack3);
		} else {
			Stack<Node> nullStack = new Stack<>();
			nullStack.add(node);
			setStack(nullStack);
		}

		map.put("PARENT-CONTEXT", current);

		return map;

	}

	public static void afterRequest(String meesage, Context context,Map<String,String> attributeMap) {

		/*
		 * Context current = Context.current(); Span span = Span.current(); Scope
		 * makeCurrent = span.makeCurrent();
		 * 
		 * Context current = Context.current(); Context with = current.with(span);
		 * ContextStorage contextStorage2= ContextStorage.get();
		 * contextStorage2.attach((Context)with);
		 * 
		 * //attach.close(); makeCurrent.close();
		 */

		Stack<Node> stack2 = getStack();
		Node pop = stack2.pop();
		Context parent = pop.getParent();

		Context current = Context.current();
		Span currentSpan = Span.current();
		
		
		  for (Map.Entry<String,String> entry : attributeMap.entrySet()) 
		  {
			  
		  currentSpan.setAttribute(entry.getKey(), entry.getValue()); 
		  }
		 

		currentSpan.end();

		ContextStorage contextStorage2 = ContextStorage.get();
		contextStorage2.attach(parent);

	}

	private static Stack<Node> stack;



	public static Stack<Node> getStack() {
		return stack;
	}

	public static void setStack(Stack<Node> stack) {
		OpentelemetryConfigSingleton.stack = stack;
	}

	


}
