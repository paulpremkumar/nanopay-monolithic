package com.nanopay.payeasy.auth.DTO;

import lombok.Data;

@Data
public class VerifyRequest {
   private String emailOrMobile;
   private String otp;
}
