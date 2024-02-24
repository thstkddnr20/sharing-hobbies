package com.toyproject.sh.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;



@Data
public class SearchFriendForm {

    @NotBlank
    private String Email;

    private String foundEmail;

}
