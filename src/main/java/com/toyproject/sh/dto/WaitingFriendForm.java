package com.toyproject.sh.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WaitingFriendForm {

    private List<String> waitingEmail = new ArrayList<>();
}
