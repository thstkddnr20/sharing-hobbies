package com.toyproject.sh.dto;

import com.toyproject.sh.domain.Category;
import lombok.Data;

@Data
public class CreatePostRequest {

    private Category category;

    private String thumbnail;

    private String content;

    private String tagName;


}
