package com.bancs.opentelemetryconfig;

import java.time.Duration;
import java.util.HashMap;
import java.util.Stack;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

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
import io.opentelemetry.contrib.awsxray.AwsXrayIdGenerator;
import io.opentelemetry.exporter.logging.LoggingMetricExporter;
import io.opentelemetry.exporter.logging.LoggingSpanExporter;
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter;
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
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
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes;

@Component
public class OpentelemetryConfig {
	private final String endpoint = "http://localhost:5555";
	private final String resourceattr = "service.name=opentelemetry-app,service.version=1.0";
	private static final long METRIC_EXPORT_INTERVAL_MS = 800L;
	Scope attach;

	public OpenTelemetry returnTelemetry() {

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

	public HashMap<String, Context> beforeRequest(String message) {
		HashMap<String, Context> map = new HashMap<>();
		Tracer buildTracer = this.returnTelemetry().getTracer(message, "1.0.0");
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
		if (this.getStack() != null) {
			Stack<Node> stack3 = this.getStack();
			stack3.add(node);

			this.setStack(stack3);
		} else {
			Stack<Node> nullStack = new Stack<>();
			nullStack.add(node);
			this.setStack(nullStack);
		}

		map.put("PARENT-CONTEXT", current);

		return map;

	}

	public void afterRequest(String meesage, Context context) {

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

		Stack<Node> stack2 = this.getStack();
		Node pop = stack2.pop();
		Context parent = pop.getParent();

		Context current = Context.current();
		Span currentSpan = Span.current();

		currentSpan.end();

		ContextStorage contextStorage2 = ContextStorage.get();
		contextStorage2.attach(parent);

	}

	private Stack<Node> stack;

	public Stack<Node> getStack() {
		return stack;
	}

	public void setStack(Stack<Node> stack) {
		this.stack = stack;
	}

}
