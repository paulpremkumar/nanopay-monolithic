package com.nanopay.payeasy.gateway;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple rate limiting filter: limits requests per IP per time window
 */
@Component
public class RatelimitFilter extends OncePerRequestFilter {

    // Max requests per IP per window
    private static final int MAX_REQUESTS = 5;

    // Time window in milliseconds (e.g., 1 minute)
    private static final long TIME_WINDOW_MS = 60 * 1000;

    // Store IP -> [request count, window start timestamp]
    private final Map<String, RateLimitInfo> requestCounts = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String clientIp = getClientIP(request);

        RateLimitInfo info = requestCounts.computeIfAbsent(clientIp, k -> new RateLimitInfo());

        synchronized (info) {
            long now = Instant.now().toEpochMilli();

            // Reset counter if time window passed
            if (now - info.windowStart > TIME_WINDOW_MS) {
                info.windowStart = now;
                info.counter.set(0);
            }

            // Increment counter
            int count = info.counter.incrementAndGet();

            if (count > MAX_REQUESTS) {
                // Rate limit exceeded
                response.setStatus(429); // 429
                response.setContentType("application/json");
                response.getWriter().write("{\"status\":429,\"error\":\"Too Many Requests\",\"message\":\"Rate limit exceeded\"}");
                return;
            }
        }

        // Proceed with the filter chain
        filterChain.doFilter(request, response);
    }

    // Helper to get client IP (handles proxies)
    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    // Class to hold rate limit info per IP
    private static class RateLimitInfo {
        AtomicInteger counter = new AtomicInteger(0);
        long windowStart = Instant.now().toEpochMilli();
    }
}

