package com.tomato.authorization.resource.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

import static com.tomato.authorization.resource.properties.ResourceSecurityProperties.PREFIX;

/**
 * 自定义资源服务器配置
 *
 * @author lizhifu
 * @since 2023/12/19
 */
@Data
@ConfigurationProperties(prefix = PREFIX)
public class ResourceSecurityProperties {

    public static final String PREFIX = "resource.security";

    /**
     * 登录页面地址,前后端分离的项目为完整路径
     * 用于一般的应用程序认证
     */
    private String loginUrl = "/login";

    /**
     * 不需要认证的路径
     */
    private List<String> ignoreUriList = new ArrayList<>();

}
