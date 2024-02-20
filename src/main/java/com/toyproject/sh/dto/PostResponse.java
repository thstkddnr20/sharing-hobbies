package com.toyproject.sh.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toyproject.sh.domain.Post;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
public class PostResponse {

    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime time;

    private String thumbnail;

    private String author;

    public PostResponse(Post post) {
        this.id = post.getCount(); // 채번된 count 가져오기
        this.time = post.getCreatedAt();
        this.thumbnail = post.getThumbnail();
        this.author = post.getMember().getEmail();
    }
}
