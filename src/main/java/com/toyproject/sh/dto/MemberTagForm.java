package com.toyproject.sh.dto;

import lombok.Data;

import java.util.List;

@Data
public class MemberTagForm {

    private List<String> tags;

    public MemberTagForm(List<String> tags) {
        this.tags = tags;
    }
}
