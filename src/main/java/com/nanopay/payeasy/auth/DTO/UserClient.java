package com.nanopay.payeasy.auth.DTO;
import lombok.Data;

@Data
public class UserClient {
    private Long id;
    private String name;
    private String email;
    private String mobile;
    private boolean active;
    private Long user_type;
}
