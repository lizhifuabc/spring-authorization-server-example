package com.tomato.authorization.server.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义资源服务器配置
 *
 * @author lizhifu
 * @since 2023/12/19
 */
@Data
@ConfigurationProperties(prefix = ResourceSecurityProperties.PREFIX)
public class ResourceSecurityProperties {

    public static final String PREFIX = "resource.security";

    /**
     * 不需要认证的路径
     */
    private List<String> ignoreUriList = new ArrayList<>();
    /**
     * 登录页面地址,前后端分离的项目为完整路径
     * 用于处理 OAuth 2.0 认证服务器的登录请求
     */
    private String loginUrl = "/login";

}
