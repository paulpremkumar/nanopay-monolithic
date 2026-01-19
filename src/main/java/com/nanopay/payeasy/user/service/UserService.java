package com.nanopay.payeasy.user.service;


import com.nanopay.payeasy.user.DTO.UserRequestDto;
import com.nanopay.payeasy.user.DTO.UserResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface UserService {
    UserResponseDto createUser(UserRequestDto UserDto);
    UserResponseDto getUserById(Long id);
    List<UserResponseDto> getAllUsers();
    UserResponseDto updateUser(Long id,UserRequestDto userDto);
    void deleteUser(Long id);
    UserResponseDto getUserByMobile(String mobile);
    UserResponseDto getUserByEmail(String email);
}

