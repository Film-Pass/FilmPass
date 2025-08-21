package com.example.filmpass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;

@SpringBootApplication
@EnableSpringConfigured
public class FilmPassApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilmPassApplication.class, args);
    }

}
