package com.unitech.auth.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    // Externalized configuration
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    // Secure key derivation (fixed across restarts)
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a JWT token with user details
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        // Store user roles as a comma-separated string
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(",")));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername()) // Typically the email
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts the username (email) from the token
     * @throws JwtException if token is invalid
     */
    public String extractUsername(String token) throws JwtException {
        return parseToken(token).getSubject();
    }

    /**
     * Extracts user roles from the token
     */
    public String extractRoles(String token) throws JwtException {
        return parseToken(token).get("roles", String.class);
    }

    /**
     * Validates token against user details
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return username.equals(userDetails.getUsername()) &&
                    !isTokenExpired(token);
        } catch (JwtException e) {
            return false;
        }
    }

    // Helper methods
    private Claims parseToken(String token) throws JwtException {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            throw new JwtException("Token expired", ex);
        } catch (SignatureException ex) {
            throw new JwtException("Invalid token signature", ex);
        } catch (MalformedJwtException ex) {
            throw new JwtException("Invalid token format", ex);
        }
    }

    private boolean isTokenExpired(String token) throws JwtException {
        return parseToken(token).getExpiration().before(new Date());
    }
}