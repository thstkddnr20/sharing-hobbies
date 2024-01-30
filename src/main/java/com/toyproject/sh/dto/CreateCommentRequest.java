package com.toyproject.sh.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateCommentRequest {

    @NotEmpty(message = "내용이 없습니다.")
    private String content;
}
