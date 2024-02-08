package com.toyproject.sh.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FormCreatePostRequest {

    @NotBlank(message = "카테고리는 필수항목입니다.")
    private String category;

    @NotBlank(message = "제목은 필수항목입니다.")
    private String thumbnail;

    @NotBlank(message = "내용은 필수항목입니다.")
    private String content;

    private String tagName;


}
