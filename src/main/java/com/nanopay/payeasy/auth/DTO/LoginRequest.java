package com.nanopay.payeasy.auth.DTO;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
    private String identifier;
}

