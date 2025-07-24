package com.unitech.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.List;
import java.util.function.Predicate;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    // Inject the JWT secret from application.yml
    @Value("${jwt.secret}")
    private String jwtSecret;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // Check if the request path is for an unsecured endpoint (like login/register)
            // In this setup, we apply the filter at the route level in YAML, so this check
            // is redundant but good for demonstrating a more complex scenario.
            // For this example, all routes using this filter are considered secure.

            // 1. Check if the Authorization header is present
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "No Authorization header", HttpStatus.UNAUTHORIZED);
            }

            // 2. Get the token from the header
            String authHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);
            }
            String token = authHeader.substring(7); // Remove "Bearer "

            // 3. Validate the JWT
            try {
                validateToken(token);
            } catch (Exception e) {
                return onError(exchange, "JWT validation failed: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
            }

            // If the token is valid, proceed to the next filter in the chain
            return chain.filter(exchange);
        };
    }

    // A simple method to handle errors by setting the HTTP status and writing a message
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    // Method to validate the JWT token using the jjwt library
    private void validateToken(String token) {
        // We need a SecretKey object for validation
        byte[] keyBytes = jwtSecret.getBytes();
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);

        // The parser will throw an exception if the token is invalid (e.g., expired, wrong signature)
        Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
    }

    // An empty config class is needed for the factory
    public static class Config {
    }
}
