package com.tomato.authorization.server.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

/**
 * 认证服务器接口
 *
 * @author lizhifu
 * @since 2023/12/18
 */
@Controller
@RequiredArgsConstructor
public class AuthorizationController {

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
    /**
     * 获取OAuth2同意页面
     *
     * @param principal 当前用户
     * @param model 模型
     * @param clientId 客户端ID
     * @param scope 作用域
     * @param state 状态
     * @param userCode 用户代码
     * @return 返回同意页面
     */
    @GetMapping(value = "/oauth2/consent")
    public String consent(Principal principal, Model model,
                          @RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId,
                          @RequestParam(OAuth2ParameterNames.SCOPE) String scope,
                          @RequestParam(OAuth2ParameterNames.STATE) String state,
                          @RequestParam(name = OAuth2ParameterNames.USER_CODE, required = false) String userCode) {
        return "consent";
    }
}
