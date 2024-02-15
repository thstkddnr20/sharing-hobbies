package com.toyproject.sh.dto;

import com.toyproject.sh.domain.Post;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FormCreatePostRequest {

    private Long id;

    @NotBlank(message = "카테고리는 필수항목입니다.")
    private String category;

    @NotBlank(message = "제목은 필수항목입니다.")
    private String thumbnail;

    @NotBlank(message = "내용은 필수항목입니다.")
    private String content;

    private String tagName;

    public FormCreatePostRequest() {
    }

    public FormCreatePostRequest(Post post, String tagName) {
        this.id = post.getId();
        this.category = post.getCategory().getValue();
        this.thumbnail = post.getThumbnail();
        this.content = post.getContent();
        this.tagName = tagName;
    }
}
