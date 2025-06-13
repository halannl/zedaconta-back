package com.zedaconta.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ZedaContaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZedaContaApplication.class, args);
    }
}
