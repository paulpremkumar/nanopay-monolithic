package com.nanopay.payeasy.auth.controller;

import com.nanopay.payeasy.auth.DTO.LoginRequest;
import com.nanopay.payeasy.auth.DTO.LoginResponse;
import com.nanopay.payeasy.auth.DTO.UserClient;
import com.nanopay.payeasy.auth.service.AuthService;
import com.nanopay.payeasy.auth.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtProvider jwtProvider;
    private final AuthService authService;
    public AuthController(JwtProvider jwtProvider,AuthService authService) {
        this.jwtProvider = jwtProvider;
        this.authService = authService;
    }

    // LOGIN → TOKEN
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        UserClient user=this.authService.verifyUserEmail(request.username);
        List<String> roles = List.of("ROLE_ADMIN");
        String token = jwtProvider.generateToken(user.getMobile(), roles);

        return new LoginResponse(token);
    }

    // TOKEN VALIDATION (used by Gateway if needed)
    @PostMapping("/validate")
    public Map<String, Object> validate(@RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        Claims claims = jwtProvider.validateToken(token);

        Map<String, Object> response = new HashMap<>();
        response.put("username", claims.getSubject());
        response.put("roles", claims.get("roles"));

        return response;
    }

}

