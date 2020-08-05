package com.kakao.openaccount;

import com.kakao.openaccount.transfer.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OpenAccountApplication {


    public static void main(String[] args) {
        SpringApplication.run(OpenAccountApplication.class, args);
    }

}
