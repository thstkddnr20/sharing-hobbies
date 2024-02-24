package com.toyproject.sh.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RequestFriendForm {

    private List<String> email = new ArrayList<>();
}
