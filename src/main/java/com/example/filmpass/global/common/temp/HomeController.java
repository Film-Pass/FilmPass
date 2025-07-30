package com.example.filmpass.global.common.temp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String homepage() {
        return "홈페이지 입니다.";
    }

}
