package com.nanopay.payeasy.user.controller;


import com.nanopay.payeasy.common.ApiResponse;
import com.nanopay.payeasy.user.DTO.UserRequestDto;
import com.nanopay.payeasy.user.DTO.UserResponseDto;
import com.nanopay.payeasy.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService){
        this.userService=userService;
    }
    @PostMapping("/save")
    public ResponseEntity<ApiResponse<Boolean, String, UserResponseDto>>  createUser(@RequestBody UserRequestDto userDto){
        UserResponseDto user=this.userService.createUser(userDto);
        ApiResponse<Boolean, String, UserResponseDto> response=new ApiResponse<Boolean, String, UserResponseDto>(true,"User Saved successfully",user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Boolean, String, UserResponseDto>> updateUser(@PathVariable Long id,@RequestBody UserRequestDto userDto){
        UserResponseDto user=this.userService.updateUser(id,userDto);
        ApiResponse< Boolean, String, UserResponseDto> response= new ApiResponse<Boolean, String, UserResponseDto>(true,"User Updated successfully",user);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/getUserById/{id}")
    public ResponseEntity<ApiResponse<Boolean, String, UserResponseDto>> getUserById(@PathVariable Long id){
        UserResponseDto user=this.userService.getUserById(id);
        ApiResponse< Boolean, String, UserResponseDto> response= new ApiResponse<Boolean, String, UserResponseDto>(true,"User Data available",user);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/getUserByEmail/{email}")
    public ResponseEntity<ApiResponse<Boolean, String, UserResponseDto>> getUserByEmail(@PathVariable String email){
        UserResponseDto user=this.userService.getUserByEmail(email);
        ApiResponse< Boolean, String, UserResponseDto> response= new ApiResponse<Boolean, String, UserResponseDto>(true,"User Data available",user);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/getAllUser")
    public ResponseEntity<ApiResponse<Boolean,String,List<UserResponseDto>>> getAllUsers(){
        List<UserResponseDto> userList=this.userService.getAllUsers();
        ApiResponse<Boolean,String,List<UserResponseDto>> response=new ApiResponse<>(true,"Data available",userList);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Boolean,String,Void>> deleteUser(@PathVariable Long id){
        this.userService.deleteUser(id);
        ApiResponse<Boolean,String,Void> response=new ApiResponse<>(true,"User Deleted Successfully");
        return ResponseEntity.ok(response);
    }
}
