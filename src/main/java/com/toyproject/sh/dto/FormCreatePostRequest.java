package com.toyproject.sh.dto;

import com.toyproject.sh.domain.Category;
import com.toyproject.sh.domain.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class FormCreatePostRequest {

    private Long id;

    private Category category;

    @NotBlank(message = "제목은 필수항목입니다.")
    private String thumbnail;

    @NotBlank(message = "내용은 필수항목입니다.")
    private String content;

    private String tagName;

    public FormCreatePostRequest() {
    }

    public FormCreatePostRequest(Post post, String tagName) {
        this.id = post.getId();
        this.category = post.getCategory();
        this.thumbnail = post.getThumbnail();
        this.content = post.getContent();
        this.tagName = tagName;
    }
}
