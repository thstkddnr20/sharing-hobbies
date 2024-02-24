package com.toyproject.sh.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FriendListForm {

    private List<String> friendList = new ArrayList<>();
}
