/*
 * package com.opentelemetry.Config;
 * 
 * import java.time.Duration;
 * 
 * import org.springframework.stereotype.Component;
 * 
 * import io.opentelemetry.api.OpenTelemetry; import
 * io.opentelemetry.api.common.Attributes; import
 * io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator; import
 * io.opentelemetry.context.propagation.ContextPropagators; import
 * io.opentelemetry.contrib.awsxray.AwsXrayIdGenerator; import
 * io.opentelemetry.exporter.logging.LoggingMetricExporter; import
 * io.opentelemetry.exporter.logging.LoggingSpanExporter; import
 * io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter; import
 * io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter; import
 * io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter; import
 * io.opentelemetry.sdk.OpenTelemetrySdk; import
 * io.opentelemetry.sdk.logs.SdkLoggerProvider; import
 * io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor; import
 * io.opentelemetry.sdk.metrics.SdkMeterProvider; import
 * io.opentelemetry.sdk.metrics.export.MetricReader; import
 * io.opentelemetry.sdk.metrics.export.PeriodicMetricReader; import
 * io.opentelemetry.sdk.resources.Resource; import
 * io.opentelemetry.sdk.trace.SdkTracerProvider; import
 * io.opentelemetry.sdk.trace.export.BatchSpanProcessor; import
 * io.opentelemetry.semconv.resource.attributes.ResourceAttributes;
 * 
 * @Component public class OpentelemetryConfig { private final String endpoint =
 * "http://localhost:5555"; private final String resourceattr =
 * "service.name=opentelemetry-app,service.version=1.0"; private static final
 * long METRIC_EXPORT_INTERVAL_MS = 800L;
 * 
 * public OpenTelemetry returnTelemetry() {
 * 
 * final String ENDPOINT="http://localhost:5555";
 * 
 * Resource resource = Resource.getDefault()
 * .merge(Resource.create(Attributes.of(ResourceAttributes.SERVICE_NAME,
 * "opentelemetry_bancs_version_1.0.0")));
 * 
 * 
 * SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
 * .addSpanProcessor(BatchSpanProcessor
 * .builder(OtlpGrpcSpanExporter.builder().setEndpoint(endpoint).build()).build(
 * )) .setResource(resource).build();
 * 
 * SdkMeterProvider sdkMeterProvider = SdkMeterProvider.builder()
 * .registerMetricReader(PeriodicMetricReader
 * .builder(OtlpGrpcMetricExporter.builder().setEndpoint(endpoint).build()).
 * build()) .setResource(resource).build();
 * 
 * 
 * BatchSpanProcessor build = BatchSpanProcessor.builder(
 * OtlpGrpcSpanExporter.builder() .setEndpoint(ENDPOINT)
 * .setTimeout(Duration.ofSeconds(50)) .build()) .build(); SdkTracerProvider
 * sdkTracerProvider = SdkTracerProvider.builder()
 * .addSpanProcessor(BatchSpanProcessor.builder( OtlpGrpcSpanExporter.builder()
 * .setEndpoint(ENDPOINT) .setTimeout(Duration.ofSeconds(50)) .build())
 * .build()) .setResource(resource)
 * .setIdGenerator(AwsXrayIdGenerator.getInstance()) .build();
 * 
 * MetricReader periodicReader =
 * PeriodicMetricReader.builder(LoggingMetricExporter.create())
 * .setInterval(Duration.ofMillis(METRIC_EXPORT_INTERVAL_MS)) .build();
 * 
 * // This will be used to create instruments SdkMeterProvider sdkMeterProvider
 * = SdkMeterProvider.builder().registerMetricReader(periodicReader).build();
 * 
 * 
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
 * getInstance())) .build();
 * 
 * return openTelemetry; }
 * 
 * }
 */