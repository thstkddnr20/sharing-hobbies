package com.toyproject.sh.dto;

import com.toyproject.sh.domain.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreatePostRequest {

    private Category category;

    @NotBlank(message = "제목은 필수항목입니다.")
    private String thumbnail;

    @NotBlank(message = "내용은 필수항목입니다.")
    private String content;

    private String tagName;


}
