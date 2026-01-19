package com.nanopay.payeasy.auth.service;
import com.nanopay.payeasy.auth.DTO.UserClient;
import com.nanopay.payeasy.user.DTO.UserResponseDto;
import org.springframework.stereotype.Service;
import com.nanopay.payeasy.user.service.UserService;
import java.util.List;
@Service
public class AuthService {
    private final UserService userService;
    public AuthService(UserService userService){
        this.userService = userService;
    }
    public UserClient verifyUserEmail(String email){
        // System.out.println(user);
        // 🔴 Replace with DB / User Service check
//        if (!email.equals(user.getEmail()) && !email.equals(user.getMobile())) {
//            throw new RuntimeException("Invalid Email id or mobile number");
//        }

        return mapToUserClient(this.userService.getUserByEmail(email));
    }
    public UserClient mapToUserClient(UserResponseDto dto) {
        UserClient client = new UserClient();
        client.setId(dto.getId());
        client.setName(dto.getName());
        client.setEmail(dto.getEmail());
        client.setActive(dto.isActive());
        client.setMobile(dto.getMobile());
        client.setUser_type(dto.getUser_type());
        return client;
    }
}
