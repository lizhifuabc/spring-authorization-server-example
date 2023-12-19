package com.tomato.authorization.server.repository;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * UserDetailsService
 *
 * @author lizhifu
 * @since 2023/12/19
 */
@SpringBootTest
public class UserDetailsServiceTest {
    @Resource
    UserDetailsService userDetailsService;

    @Test
    public void loadUserByUsername() {
    }
}
