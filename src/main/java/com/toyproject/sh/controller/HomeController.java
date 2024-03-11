package com.toyproject.sh.controller;

import com.toyproject.sh.argumentResolver.Login;
import com.toyproject.sh.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Slf4j
@Controller
public class HomeController {


    @GetMapping("/")
    public String homeLoginV3(@Login Member member, Model model) {

        if (member == null) {
            return "home";
        }
        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", member);

        return "loginHome";
    }

}
