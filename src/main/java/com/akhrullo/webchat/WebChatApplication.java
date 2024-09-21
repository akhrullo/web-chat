package com.akhrullo.webchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WebChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebChatApplication.class, args);
    }
}
