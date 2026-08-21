package com.digisevasolution.security;

import com.digisevasolution.dto.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final int MAX_REQUESTS_PER_MINUTE = 10;
    private static final long ONE_MINUTE_MS = 60 * 1000L;

    private static class RequestTracker {
        long windowStart;
        int requestCount;

        RequestTracker(long windowStart) {
            this.windowStart = windowStart;
            this.requestCount = 1;
        }
    }

    private final Map<String, RequestTracker> ipTrackers = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    public RateLimitFilter() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Enforce rate limiting on sensitive auth endpoints and public enquiry submissions
        boolean isAuthEndpoint = path.equals("/api/admin/auth/login") || path.equals("/api/admin/auth/verify-otp");
        boolean isEnquirySubmission = path.equals("/api/enquiries") && "POST".equalsIgnoreCase(request.getMethod());

        if (isAuthEndpoint || isEnquirySubmission) {
            String clientIp = getClientIp(request);
            long now = System.currentTimeMillis();

            boolean rateLimitExceeded = ipTrackers.compute(clientIp, (ip, tracker) -> {
                if (tracker == null || (now - tracker.windowStart) > ONE_MINUTE_MS) {
                    return new RequestTracker(now);
                } else {
                    tracker.requestCount++;
                    return tracker;
                }
            }).requestCount > MAX_REQUESTS_PER_MINUTE;

            if (rateLimitExceeded) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                ApiResponse<Object> errorResponse = ApiResponse.error("Too many authentication requests from your IP. Please try again after 1 minute.");
                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    public void clearTrackers() {
        this.ipTrackers.clear();
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null && !xfHeader.isBlank()) {
            return xfHeader.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
