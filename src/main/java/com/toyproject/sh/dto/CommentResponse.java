package com.toyproject.sh.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toyproject.sh.domain.Comment;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponse {

    private Long id;

    private boolean isParent;

    private String author;

    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public CommentResponse(Comment comment) {
        this.author = comment.getMember().getEmail();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.id = comment.getId();
        this.isParent = comment.isParent();
    }
}
