package com.toyproject.sh.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum Category {
    INTRODUCE("취미를 소개합니다"),
    GUIDE("취미 가이드"),
    SPECIALHOBBIES("나만의 특별한 취미"),
    TOGETHER("취미를 함께 즐겨요"),
    FREE("자유게시판"),
    QUESTION("질문게시판");

    private final String value;

    Category(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
