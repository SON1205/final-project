package com.ssafy.urs.entity;

import lombok.Data;

@Data
public class RegisterRequest {
    private String userId;
    private String password;
    private String name;
    private String phone;
}

