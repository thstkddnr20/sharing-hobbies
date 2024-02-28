package com.toyproject.sh.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toyproject.sh.domain.Category;
import com.toyproject.sh.domain.Post;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostCommentsResponse {

    private Long count;

    private String author;

    private String thumbnail;

    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    private Category category;

    private List<CommentResponse> comments;

    private String tagName;

    public PostCommentsResponse(Post post, List<CommentResponse> comments, String tagName) {
        this.count = post.getCount();
        this.author = post.getMember().getEmail();
        this.thumbnail = post.getThumbnail();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.category = post.getCategory();
        this.comments = comments;
        this.tagName = tagName;
    }
}
