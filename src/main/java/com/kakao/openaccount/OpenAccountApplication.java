package com.kakao.openaccount;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

@EnableCaching
@SpringBootApplication
public class OpenAccountApplication {


    public static void main(String[] args) {
        SpringApplication.run(OpenAccountApplication.class, args);
    }

}
