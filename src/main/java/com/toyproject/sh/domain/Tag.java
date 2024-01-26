package com.toyproject.sh.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Entity
@Getter
public class Tag {

    @Id @GeneratedValue
    private Long id;

    private String name;

    public Tag(String name) {
        this.name = name;
    }
    public Tag() {
    }
}
