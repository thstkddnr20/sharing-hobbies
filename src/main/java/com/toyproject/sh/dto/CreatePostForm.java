package com.toyproject.sh.dto;

import com.toyproject.sh.domain.Category;
import com.toyproject.sh.domain.Post;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreatePostForm {

    private Category category;

    @NotBlank(message = "제목은 필수항목입니다.")
    private String thumbnail;

    @NotBlank(message = "내용은 필수항목입니다.")
    private String content;

    private String tagName;

    public CreatePostForm() {
    }

    public CreatePostForm(Post post, String tagName) {
        this.category = post.getCategory();
        this.thumbnail = post.getThumbnail();
        this.content = post.getContent();
        this.tagName = tagName;
    }
}
