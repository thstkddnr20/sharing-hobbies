package com.toyproject.sh.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum Category {
    INTRODUCE("int"),
    GUIDE("gui"),
    SPECIALHOBBIES("spe"),
    TOGETHER("tog"),
    FREE("free"),
    QUESTION("qna");

    @Getter
    private final String value;

    Category(String value) {
        this.value = value;
    }

    @JsonCreator
    public static Category from(String value) {
        for (Category status : Category.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
