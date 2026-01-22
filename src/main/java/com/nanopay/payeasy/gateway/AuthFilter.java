package com.nanopay.payeasy.gateway;

import com.nanopay.payeasy.auth.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    public AuthFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
// System.out.println("path= "+path);
        // Skip filter for public endpoints
        if (path.startsWith("/auth") || path.startsWith("/auth/login") || path.startsWith("/auth/register")) {
            filterChain.doFilter(request, response);
            return;
        }
        String authHeader = request.getHeader("Authorization");
// System.out.println(authHeader+"authHeader");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7);
// System.out.println(jwtProvider.isTokenValid(token)+"isvalid token");
        try {
            Claims claims = jwtProvider.validateToken(token);
            @SuppressWarnings("unchecked")
            List<String> roles = claims.get("roles", List.class); // List<String>
            List<GrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            // Optional: set Authentication for roles
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            claims.getSubject(),
                            null,
                            authorities // roles if needed
                    );
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JwtException e) {
            // response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String json = String.format(
                    "{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"%s\"}",
                    e.getMessage()
            );

            response.getWriter().write(json);
            response.getWriter().flush();
            return;
        }

        filterChain.doFilter(request, response);
    }
}
