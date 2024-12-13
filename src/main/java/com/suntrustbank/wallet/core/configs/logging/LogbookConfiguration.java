package com.suntrustbank.wallet.core.configs.logging;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.CorrelationId;

import java.util.Objects;


@Configuration
class LogbookConfiguration {
    private static final String TRACEPARENT = "traceparent";

    public static CorrelationId correlationId() {

        return request -> {
            SpanContext spanContext = Span.current().getSpanContext();
            String spanId = spanContext.getSpanId();
            String traceId = spanContext.getTraceId();
            String correlation = traceId + "-" + spanId;

            final String requestId = request.getHeaders().getFirst(TRACEPARENT);
            return Objects.toString(requestId, correlation);
        };
    }
}
