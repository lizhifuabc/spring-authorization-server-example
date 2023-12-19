package com.tomato.authorization.server.config;

import com.tomato.authorization.server.properties.AuthorizationProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

/**
 * 认证配置
 *
 * @author lizhifu
 * @since 2023/12/18
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class AuthorizationConfig {

    private final AuthorizationProperties authorizationProperties;

    @PostConstruct
    public void init() {
        log.info("授权服务器配置初始化：{}", authorizationProperties);
    }

    /**
     * 配置端点的过滤器链
     *
     * @param http spring security核心配置类
     * @return 过滤器链
     * @throws Exception 抛出
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
                                                                      RegisteredClientRepository registeredClientRepository,
                                                                      AuthorizationServerSettings authorizationServerSettings) throws Exception {
        // 配置默认的设置，忽略认证端点的csrf校验, 通过下面的代码进行禁用
        // 因为这段代码调用了 .anyRequest().authenticated()) 方法，重复配置，会报错：Can't configure mvcMatchers after anyRequest
         OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        // 禁用 csrf 与 cors
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(AbstractHttpConfigurer::disable);

        // 当未登录时访问认证端点时重定向至login页面,注意这里是指认证端点，不是资源端点，例如/oauth2/token
        http.exceptionHandling((exceptions) -> exceptions
                .defaultAuthenticationEntryPointFor(
                        new LoginUrlAuthenticationEntryPoint(authorizationProperties.getLoginUrl()),
                        new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                )
        );

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                // 自定义用户确认授权页面
                .authorizationEndpoint((authorizationEndpoint) -> {
                            authorizationEndpoint.consentPage(authorizationProperties.getConsentPageUrl());
                        }
                );

        return http.build();
    }

    /**
     * 配置密码解析器，使用BCrypt的方式对密码进行加密和验证
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 利用工厂类PasswordEncoderFactories实现,工厂类内部采用的是委派密码编码方案.
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
