package com.tomato.authorization.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * 客户端
 *
 * @author lizhifu
 * @since 2023/12/18
 */
@SpringBootApplication
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
public class AuthorizationClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthorizationClientApplication.class);
    }
}
