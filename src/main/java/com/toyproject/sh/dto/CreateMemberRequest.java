package com.toyproject.sh.dto;


import lombok.Data;

@Data
public class CreateMemberRequest {
    private String email;

    private String password;
}
