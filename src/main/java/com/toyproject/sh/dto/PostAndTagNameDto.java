package com.toyproject.sh.dto;

import com.toyproject.sh.domain.Post;
import lombok.Data;

@Data
public class PostAndTagNameDto {
    private Post post;
    private String tagName;

    public PostAndTagNameDto(Post post, String tagName) {
        this.post = post;
        this.tagName = tagName;
    }
}
