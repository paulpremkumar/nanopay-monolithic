package com.nanopay.payeasy.user.DTO;

import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private String mobile;
    private boolean active;
    private Long user_type;
}

