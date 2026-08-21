package code.travelplanner.Backend.configuration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    // Store in-memory buckets per client key (UserId/ IP)
    // Separate caches per endpoint type
    private final Map<String, Bucket> authBuckets = new ConcurrentHashMap<>();
    private final Map<String, Bucket> searchBuckets = new ConcurrentHashMap<>();
    private final Map<String, Bucket> externalApiBuckets = new ConcurrentHashMap<>();
    private final Map<String, Bucket> generalBuckets = new ConcurrentHashMap<>();

    protected void doFilterInternal (HttpServletRequest request, HttpServletResponse response
            , FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        String clientKey = getClientKey(request);
        Bucket bucket;

        // Unauthenticated endpoints - per-IP ceiling to stop bot floods
        // 5 requests per minute
        if (path.contains("/login") || path.contains("/register")) {
            String ipKey = getClientIp(request);
            bucket = authBuckets.computeIfAbsent(ipKey, k -> createBucket(5, Duration.ofMinutes(1)));
        }

        // High-cost database searches
        // 20 requests per minute
        else if (path.contains("/users/*/search") || path.contains("/retrieve-trips")) {
            bucket = searchBuckets.computeIfAbsent(clientKey, k -> createBucket(20, Duration.ofMinutes(1)));
        }

        // Contains external api which could cost money if limit exceeded
        else if (path.contains("*/map-data")) {
            bucket = externalApiBuckets.computeIfAbsent(clientKey, k -> createBucket(10, Duration.ofMinutes(1)));
        }

        // General endpoints
        else {
            bucket = generalBuckets.computeIfAbsent(clientKey, k -> createBucket(20, Duration.ofMinutes(1)));
        }

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {

            //Too many requests, 429 error
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write("""
                    { 
                    "error" : "Too Many Requests", 
                    "message": "Rate limit exceeded. Please wait before retrying"
                    }
                    """);
        }
    }

    private Bucket createBucket(int capacity, Duration refillPeriod) {

        Bandwidth limit = Bandwidth.builder()
                .capacity(capacity)
                .refillIntervally(capacity, refillPeriod)
                .build();

        return Bucket.builder().addLimit(limit).build();
    }

    private String getClientKey(HttpServletRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if user is authenticated
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            return "User:" + authentication.getName();
        }

        return "IP:" + getClientIp(request);
    }

    private String getClientIp(HttpServletRequest request) {

        // If not authenticated, use client IP address
        // HTTP header added by proxies
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            // First IP in proxy address chain
            return "IP:" + xForwardedFor.split(",")[0].trim();
        }

        // Get IP address of the TCP connection directly interacting with the app
        return request.getRemoteAddr();

    }
}
