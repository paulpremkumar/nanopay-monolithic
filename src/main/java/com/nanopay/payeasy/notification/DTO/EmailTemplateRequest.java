package com.nanopay.payeasy.notification.DTO;

import lombok.Data;

@Data
public class EmailTemplateRequest {

    private String to;
    private String subject;
    private String name;
    private String company;
    private String accountId;

}

