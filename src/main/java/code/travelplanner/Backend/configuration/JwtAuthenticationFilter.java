package code.travelplanner.Backend.configuration;

import code.travelplanner.Backend.user.Service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtService jwtService;

    public JwtAuthenticationFilter (JwtService jwtService, HandlerExceptionResolver handlerExceptionResolver) {

        this.handlerExceptionResolver = handlerExceptionResolver;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        System.out.println("DEBUG: JWT Filter intercepted request to: " + request.getRequestURI());
        // Get authorization header
        final String authHeader = request.getHeader("Authorization");

        // If no header or doesn't start with Bearer, skip this part of filter chain
        // As no need for validation
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {

            // extract token, remove "Bearer " prefix
            final String token = authHeader.substring(7);
            // Extract userId from token
            Long userId = jwtService.extractUserId(token);

            // Check token is valid
            if (!jwtService.isTokenValid(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Get who this person is
            // Check they're not already authenticated first
            if (authentication == null && userId != null) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userId, // Principle - Who they are - In this case, a Long UserId
                        null, // Credentials - null as token validated already
                        List.of() // authorities - empty
                );

                // Attach request details to the authentication
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Put authentication into spring security's context
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            // Continue the chain
            filterChain.doFilter(request, response);

        } catch (Exception exception) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }
}
