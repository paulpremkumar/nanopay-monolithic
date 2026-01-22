package com.nanopay.payeasy.config;

import com.nanopay.payeasy.gateway.AuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;

@Configuration
public class SecurityConfig {
    private final AuthFilter authFilter; // your custom filter

    public SecurityConfig(AuthFilter authFilter) {
        this.authFilter = authFilter;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/auth/**").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/auth").permitAll()
//                        .requestMatchers("/auth","/auth/login", "/auth/register").permitAll() // public endpoints
                        .anyRequest().authenticated() // all other endpoints require auth
                )
                .addFilterBefore(authFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }



    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*")); // allow all origins
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
