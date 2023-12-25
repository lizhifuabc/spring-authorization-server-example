package com.tomato.authorization.server.config;

import com.tomato.authorization.server.properties.ResourceSecurityProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.UrlUtils;

/**
 * 资源服务器配置，授权服务器和资源服务器可以是同一个服务，也可以是不同的服务
 * <p>
 * {@link EnableMethodSecurity} 开启全局方法认证，启用JSR250注解支持，启用注解 {@link Secured} 支持，
 * 在Spring Security 6.0版本中将@Configuration注解从@EnableWebSecurity, @EnableMethodSecurity, @EnableGlobalMethodSecurity
 * 和 @EnableGlobalAuthentication 中移除，使用这些注解需手动添加 @Configuration 注解
 * {@link EnableWebSecurity} 注解有两个作用:
 * 1. 加载了WebSecurityConfiguration配置类, 配置安全认证策略。
 * 2. 加载了AuthenticationConfiguration, 配置了认证信息。
 *
 * @author lizhifu
 * @since 2023/12/19
 */
@Slf4j
@AutoConfiguration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties(ResourceSecurityProperties.class)
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
public class ResourceServerConfig {
    private final ResourceSecurityProperties resourceSecurityProperties;
    @PostConstruct
    public void init() {
        log.info("资源服务器配置初始化：{}", resourceSecurityProperties);
    }

    /**
     * 配置认证相关的过滤器链(资源服务，客户端配置)
     *
     * @param http spring security核心配置类
     * @return 过滤器链
     * @throws Exception 抛出
     */
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize
                // 放行静态资源和不需要认证的url，被放行的接口上不能有权限注解，@PreAuthorize，否则无效
                .requestMatchers(resourceSecurityProperties.getIgnoreUriList().toArray(new String[0])).permitAll()
                // 其余都需要登录
                .anyRequest().authenticated()
        ).formLogin(formLogin -> {
            // 配置表单认证方式，它会使用表单提交用户名和密码进行认证。
            formLogin.loginPage(resourceSecurityProperties.getLoginUrl());
            if (UrlUtils.isAbsoluteUrl(resourceSecurityProperties.getLoginUrl())) {
                // 绝对路径代表是前后端分离，登录成功和失败改为写回json，不重定向了

            }
        }).oauth2ResourceServer(oauth2->{
            // 可在此处添加自定义解析设置
            oauth2.jwt(Customizer.withDefaults());
        });
        return http.build();
    }
}
