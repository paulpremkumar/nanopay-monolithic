package com.nanopay.payeasy.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtProvider {

        private final Key signingKey;
        private final long expirationMs;

        public JwtProvider(
                @Value("${jwt.secret}") String secret,
                @Value("${jwt.expiration-ms}") long expirationMs
        ) {
            this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
            this.expirationMs = expirationMs;
        }

        /** Generate JWT Token */
        public String generateToken(String username, List<String> roles) {
            return Jwts.builder()
                    .setSubject(username)
                    .claim("roles", roles)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                    .signWith(signingKey, SignatureAlgorithm.HS256)
                    .compact();
        }

        /** Validate and parse JWT */
        public Claims validateToken(String token) {
            return Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }
    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException ex) {
            return false;
        }
    }

        /** Helper methods */
        public String extractUsername(String token) {
            return validateToken(token).getSubject();
        }

        @SuppressWarnings("unchecked")
        public List<String> extractRoles(String token) {
            return validateToken(token).get("roles", List.class);
        }


}
