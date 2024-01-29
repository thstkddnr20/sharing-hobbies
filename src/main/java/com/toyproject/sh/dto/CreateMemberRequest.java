package com.toyproject.sh.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateMemberRequest {

    @NotEmpty(message = "이메일은 필수항목입니다.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    @Size(min = 5, max = 20, message = "비밀번호는 5~20 자리여야 합니다.")
    private String password;
}
