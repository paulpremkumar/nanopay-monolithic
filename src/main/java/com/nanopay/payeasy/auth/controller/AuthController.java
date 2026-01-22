package com.nanopay.payeasy.auth.controller;

import com.nanopay.payeasy.auth.DTO.LoginRequest;
import com.nanopay.payeasy.auth.DTO.LoginResponse;
import com.nanopay.payeasy.auth.DTO.UserClient;
import com.nanopay.payeasy.auth.DTO.VerifyRequest;
import com.nanopay.payeasy.auth.service.AuthService;
import com.nanopay.payeasy.auth.jwt.JwtProvider;
import com.nanopay.payeasy.common.response.ApiResponse;
import com.nanopay.payeasy.common.service.OtpService;
import com.nanopay.payeasy.notification.service.EmailNotificationService;
import io.jsonwebtoken.Claims;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtProvider jwtProvider;
    private final AuthService authService;
    private final OtpService otpService;
    private final EmailNotificationService emailNotificationService;
    public AuthController(JwtProvider jwtProvider,AuthService authService,OtpService otpService,EmailNotificationService emailNotificationService) {
        this.jwtProvider = jwtProvider;
        this.authService = authService;
        this.otpService = otpService;
        this.emailNotificationService = emailNotificationService;
    }

    // LOGIN → TOKEN
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        UserClient user=this.authService.verifyUserEmailOrMobile(request.getIdentifier());

        List<String> roles = List.of("ROLE_ADMIN");
        String token = jwtProvider.generateToken(user.getMobile(), roles);

        return new LoginResponse(token);
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<Boolean,String,String>> checkUserExist(@RequestParam String EmailOrMobile){
        UserClient user=this.authService.verifyUserEmailOrMobile(EmailOrMobile);
        if(user==null){
           return  ResponseEntity.ok(
                    new ApiResponse<>(false,"Email or mobile not found "+EmailOrMobile,"")
            );
        }
       String keyOpt=buildOtpKey(user);
        String otp=this.otpService.generateOtp(keyOpt);
        this.emailNotificationService.sendVerificationEmail(user.getName(),otp,user.getEmail());
      return ResponseEntity.ok(
              new ApiResponse<>(true,"Verify your otp register email "+user.getEmail(),"")
        );
    }
    private String buildOtpKey(UserClient user) {
        return user.getMobile() != null
                ? "otp:login:mobile:" + user.getMobile()
                : "otp:login:email:" + user.getEmail();
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
    @PostMapping("/verify-otp")
    public  ResponseEntity<ApiResponse<Boolean,String,LoginResponse>> verify(@RequestBody VerifyRequest request) {
        UserClient user=this.authService.verifyUserEmailOrMobile(request.getEmailOrMobile());
        if(user==null){
            return ResponseEntity.ok(new ApiResponse<>(false,"Email or mobile not found",new LoginResponse(null))) ;
        }
        boolean isOtpVerified=this.otpService.validateOtp(this.buildOtpKey(user),request.getOtp());
       if(!isOtpVerified){
          return  ResponseEntity.ok(new ApiResponse<>(false,"Invalid OTP",new LoginResponse(null))) ;
       }
        List<String> roles = List.of("ROLE_ADMIN");
        String token = jwtProvider.generateToken(user.getMobile(), roles);

        return  ResponseEntity.ok(new ApiResponse<>(true,"OTP verified successfully",new LoginResponse(token))) ;

    }


}

