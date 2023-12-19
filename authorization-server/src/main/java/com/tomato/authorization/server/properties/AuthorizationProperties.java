package com.tomato.authorization.server.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.tomato.authorization.server.properties.AuthorizationProperties.PREFIX;

/**
 * 自定义授权服务器配置
 *
 * @author lizhifu
 * @since 2023/12/18
 */
@Data
@Configuration
@ConfigurationProperties(prefix = PREFIX)
public class AuthorizationProperties {

    public static final String PREFIX = "authorization.security";

    /**
     * 登录页面地址,前后端分离的项目为完整路径
     * 用于处理 OAuth 2.0 认证服务器的登录请求
     */
    private String loginUrl = "/oauthLogin";

    /**
     * 授权确认页面,前后端分离的项目为完整路径
     */
    private String consentPageUrl = "/oauth2/consent";
}
