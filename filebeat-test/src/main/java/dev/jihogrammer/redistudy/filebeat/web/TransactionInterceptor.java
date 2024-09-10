package dev.jihogrammer.redistudy.filebeat.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
@RequiredArgsConstructor
class TransactionInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    private final ThreadLocal<UUID> uuid = ThreadLocal.withInitial(UUID::randomUUID);

    private final ThreadLocal<Long> instant = ThreadLocal.withInitial(() -> Instant.now().toEpochMilli());

    @Override
    public boolean preHandle(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            @NonNull final Object handler
    ) {
        MDC.put("tid", this.uuid.get() + "@GEN");
        MDC.put("api", request.getRequestURI());

        log.info("REQUEST IN");
        this.logRequest(request);
        return true;
    }

    @Override
    public void afterCompletion(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            @NonNull final Object handler,
            @Nullable final Exception ex
    ) {
        response.setHeader("tid", MDC.get("tid"));

        if (nonNull(ex)) {
            log.error("An exception occurred.", ex);
        }

        var elapsed = "%d".formatted(Instant.now().toEpochMilli() - this.instant.get());
        MDC.put("elapsed", elapsed);
        log.info("RESPONSE OUT; elapsed {} ms", elapsed);

        this.uuid.remove();
        this.instant.remove();
        MDC.clear();
    }

    private void logRequest(@NonNull final HttpServletRequest request) {
        if (hasText(request.getQueryString())) {
            log.info("{} {}?{}", request.getMethod(), request.getRequestURI(), request.getQueryString());
        } else {
            log.info("{} {}", request.getMethod(), request.getRequestURI());
        }

        this.logRequestHeaders(request);
    }

    private void logRequestHeaders(@NonNull final HttpServletRequest request) {
        final var headers = new HashMap<String, List<String>>();

        request.getHeaderNames().asIterator().forEachRemaining(name -> {
            final var values = new ArrayList<String>();
            headers.put(name, values);
            request.getHeaders(name).asIterator().forEachRemaining(values::add);
        });

        try {
            log.debug("HEADERS={}", this.objectMapper.writeValueAsString(headers));
        } catch (Exception e) {
            log.warn("Failed to log request headers.", e);
        }
    }

}
