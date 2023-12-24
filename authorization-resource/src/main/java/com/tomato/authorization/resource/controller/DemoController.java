package com.tomato.authorization.resource.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * demo
 *
 * @author lizhifu
 * @since 2023/12/24
 */
@RestController
public class DemoController {
    @GetMapping("/demo01")
    @PreAuthorize("hasAuthority('SCOPE_message.read')")
    public String demo01() {
        return "test01";
    }

    @GetMapping("/demo02")
    @PreAuthorize("hasAuthority('SCOPE_message.write')")
    public String demo02() {
        return "test02";
    }

    @GetMapping("/app")
    @PreAuthorize("hasAuthority('app')")
    public String app() {
        return "app";
    }
}
