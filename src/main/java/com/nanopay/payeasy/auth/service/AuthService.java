package com.nanopay.payeasy.auth.service;
import com.nanopay.payeasy.auth.DTO.UserClient;
import com.nanopay.payeasy.common.exception.InvalidRequestException;
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
    public UserClient verifyUserEmailOrMobile(String identifier){
        if (identifier == null || identifier.isBlank()) {
            throw new InvalidRequestException("Identifier (email or mobile) is required");
        }
       if(identifier.contains("@")){
           return mapToUserClient(this.userService.getUserByEmail(identifier));
       }else{
           return mapToUserClient(this.userService.getUserByMobile(identifier));
       }


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
