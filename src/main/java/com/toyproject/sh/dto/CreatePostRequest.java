package com.toyproject.sh.dto;

import com.toyproject.sh.domain.Category;
import lombok.Data;

@Data
public class CreatePostRequest {

    private String thumbnail;

    private String content;

    private Category category;


}
