package com.hypeng.example.boardgamebuddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class BoardGameBuddyApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoardGameBuddyApplication.class, args);
    }

}
