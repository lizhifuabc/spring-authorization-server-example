package com.tomato.authorization.server.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * 认证服务器登录接口
 *
 * @author lizhifu
 * @since 2023/12/18
 */
@Controller
@RequiredArgsConstructor
public class LoginController {

    /**
     * 登录接口，用于用户处理登录请求
     *
     * @return 返回登录页面的名称
     */
    @GetMapping("/login")
    public String login(Model model, HttpSession session) {
        Object attribute = session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        if (attribute instanceof AuthenticationException exception) {
            model.addAttribute("error", exception.getMessage());
        }
        return "login";
    }
    /**
     * 登录接口,用于处理 OAuth 2.0 认证服务器的登录请求
     *
     * @return 返回登录页面的名称
     */
    @GetMapping("/oauthLogin")
    public String oauthLogin(Model model, HttpSession session) {
        Object attribute = session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        if (attribute instanceof AuthenticationException exception) {
            model.addAttribute("error", exception.getMessage());
        }
        return "oauthLogin";
    }
}
