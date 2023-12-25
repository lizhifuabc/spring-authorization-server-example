package com.tomato.authorization.common.resource.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

import static com.tomato.authorization.common.resource.properties.ResourceSecurityProperties.PREFIX;

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
     * 不需要认证的路径
     */
    private List<String> ignoreUriList = new ArrayList<>();

}
