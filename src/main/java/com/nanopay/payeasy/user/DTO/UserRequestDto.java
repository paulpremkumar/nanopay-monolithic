package com.nanopay.payeasy.user.DTO;

import lombok.Data;

@Data
public class UserRequestDto {
    private long id;
    private String name;
    private String email;
    private String mobile;
    private Long user_type;
    private boolean active;
}
