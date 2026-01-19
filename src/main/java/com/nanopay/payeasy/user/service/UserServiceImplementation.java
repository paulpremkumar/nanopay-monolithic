package com.nanopay.payeasy.user.service;

import com.nanopay.payeasy.user.DTO.UserRequestDto;
import com.nanopay.payeasy.user.DTO.UserResponseDto;
import com.nanopay.payeasy.user.entity.UserEntity;
import com.nanopay.payeasy.user.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
@Component
public class UserServiceImplementation implements UserService {
    private final UserRepository userRepository;
    public UserServiceImplementation(UserRepository userRepository){
        this.userRepository=userRepository;
    }
    @Override
    public UserResponseDto createUser(UserRequestDto userDto){
        UserEntity user=new UserEntity();
        user.setName(userDto.getName());
        user.setMobile(userDto.getMobile());
        user.setEmail(userDto.getEmail());
        user.setUser_type(userDto.getUser_type());
        user.setActive(userDto.isActive());
        UserEntity saved=this.userRepository.save(user);
        return toDto(saved);
    }
    public UserResponseDto getUserById(Long id){
        UserEntity user=this.userRepository.findById(id).
                orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));

        return toDto(user);
    }
    public List<UserResponseDto> getAllUsers(){
        return this.userRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }
    public UserResponseDto updateUser(Long id,UserRequestDto userDto){
        UserEntity userExist=this.userRepository.findById(id).orElseThrow();
        userExist.setName(userDto.getName());
        userExist.setEmail(userDto.getEmail());
        userExist.setMobile(userDto.getMobile());
        userExist.setUser_type(userDto.getUser_type());
        userExist.setActive(userDto.isActive());
        UserEntity updated=this.userRepository.save(userExist);
        return toDto(updated);
    }
    public void deleteUser(Long id){
        this.userRepository.deleteById(id);
    }
    public UserResponseDto getUserByMobile(String mobile){
        UserEntity user = this.userRepository.findByMobile(mobile);
        return toDto(user);
    }
    public UserResponseDto getUserByEmail(String email){
        UserEntity user = this.userRepository.findByEmail(email);
        return toDto(user);
    }
    private UserResponseDto toDto(UserEntity user){
        UserResponseDto response=new UserResponseDto();
        response.setName(user.getName());
        response.setMobile(user.getMobile());
        response.setEmail(user.getEmail());
        response.setUser_type(user.getUser_type());
        response.setActive(user.isActive());
        response.setId(user.getId());
        return response;
    }
}
