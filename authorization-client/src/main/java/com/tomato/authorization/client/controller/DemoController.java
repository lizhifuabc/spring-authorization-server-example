package com.tomato.authorization.client.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试
 *
 * @author lizhifu
 * @since 2023/12/19
 */
@RestController
public class DemoController {

    @GetMapping("/read")
    @PreAuthorize("hasAuthority('message.read')")
    public String read(){
        return "message.read";
    }

    @GetMapping("/write")
    @PreAuthorize("hasAuthority('SCOPE_message.write')")
    public String write(){
        return "message.write";
    }

    @GetMapping("/unkonw")
    @PreAuthorize("hasAuthority('unkonw')")
    public String unkonw(){
        return "unkonw";
    }
}
