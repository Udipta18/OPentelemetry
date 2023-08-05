package com.opentelemetry.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancs.opentelemetryconfig.OpentelemetryConfig;
import com.opentelemetry.Config.GlobalClass;


import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.ContextStorage;

@Service
public class OpentelemetryService {

	@Autowired
	private OpentelemetryConfig opentelemetryConfig;

	// private Opente
	@Autowired
	private GlobalClass gbClass;

	private Tracer buildTracer() {
		return opentelemetryConfig.returnTelemetry().getTracer("opentelemetry-bancs_service", "1.0.0");
	}

	public void getResponseService() {

		Tracer buildTracer = this.buildTracer();

		

		//Object context = gbClass.getContext();
		//System.out.println(context.toString());
		
//		ContextStorage contextStorage = ContextStorage.get();
//		contextStorage.attach((Context)context);
		
		Context current = Context.current();
		
		Span span = buildTracer.spanBuilder("opentelemetry controller span Service1")
				.setParent(current).setSpanKind(SpanKind.SERVER).startSpan();
		
		span.end();
		
		Span span2 = buildTracer.spanBuilder("opentelemetry controller span Service1 same parent")
				.setParent(current).setSpanKind(SpanKind.SERVER).startSpan();

		/*
		 * Object context2 = gbClass.getContext();
		 * System.out.println(context2.toString());
		 */
		
		span2.end();
		
		

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
