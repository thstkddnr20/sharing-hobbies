package com.toyproject.sh.dto;

import com.toyproject.sh.domain.Category;
import com.toyproject.sh.domain.Member;
import com.toyproject.sh.domain.Post;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdatePostForm {

    private Long count;

    private Category category;

    @NotBlank(message = "제목은 필수항목입니다.")
    private String thumbnail;

    @NotBlank(message = "내용은 필수항목입니다.")
    private String content;

    private String tagName;

    public UpdatePostForm(Post post, String tagName) {
        this.count = post.getCount();
        this.category = post.getCategory();
        this.thumbnail = post.getThumbnail();
        this.content = post.getContent();
        this.tagName = tagName;
    }

    public UpdatePostForm() {
    }
}
