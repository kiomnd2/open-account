package com.kakao.openaccount;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class OpenAccountApplication {


    public static void main(String[] args) {
        SpringApplication.run(OpenAccountApplication.class, args);
    }

}
