package com.toyproject.sh.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toyproject.sh.domain.Category;
import com.toyproject.sh.domain.Post;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostCommentsResponse {

    private Long id;

    private String author;

    private String thumbnail;

    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    private Category category;

    private List<CommentResponse> comments;

    public PostCommentsResponse(Post post, List<CommentResponse> comments) {
        this.id = post.getId();
        this.author = post.getMember().getEmail();
        this.thumbnail = post.getThumbnail();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.category = post.getCategory();
        this.comments = comments;
    }
}
