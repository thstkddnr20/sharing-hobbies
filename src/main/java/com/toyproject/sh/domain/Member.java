package com.toyproject.sh.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String email;

    private String password;

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Member() {
    }
}
