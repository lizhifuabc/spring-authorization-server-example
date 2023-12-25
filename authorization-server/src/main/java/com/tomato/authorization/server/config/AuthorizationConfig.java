package com.tomato.authorization.server.config;

import com.tomato.authorization.server.properties.AuthorizationProperties;
import com.tomato.authorization.server.util.SecurityUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.filter.CorsFilter;

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

    private final CorsFilter corsFilter;

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

        // 添加跨域过滤器
        http.addFilter(corsFilter);

        http
                // 当未登录时访问认证端点时重定向至login页面,注意这里是指认证端点，不是资源端点，例如/oauth2/token
                .exceptionHandling((exceptions) -> exceptions
                .defaultAuthenticationEntryPointFor(
                        new LoginUrlAuthenticationEntryPoint(authorizationProperties.getLoginUrl()),
                        new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                    )
                )
                // 添加 BearerTokenAuthenticationFilter，将认证服务当做一个资源服务，解析请求头中的token
                // 指定令牌解析的方式，比如：从 Request Head 的 token 获取
                // 默认在 Request Head 中以 Authorization: Bearer token 的格式提供
                .oauth2ResourceServer((resourceServer) -> resourceServer
                        .jwt(Customizer.withDefaults())
                        // 添加未携带token和权限不足异常处理
                        .accessDeniedHandler(SecurityUtils::exceptionHandler)
                        .authenticationEntryPoint(SecurityUtils::exceptionHandler));

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                // 开启认证服务OIDC配置
                .oidc(Customizer.withDefaults())
                // 自定义用户确认授权页面
                .authorizationEndpoint((authorizationEndpoint) -> {
                            authorizationEndpoint.consentPage(authorizationProperties.getConsentPageUrl());
                        }
                )
        ;

        return http.build();
    }

    /**
     * 配置授权确认服务
     *
     * @param jdbcTemplate              jdbcTemplate
     * @param registeredClientRepository registeredClientRepository
     * @return JdbcOAuth2AuthorizationConsentService
     */
    @Bean
    public JdbcOAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate,
                                                                             RegisteredClientRepository registeredClientRepository) {
        // 将由 AuthorizationConsentController  使用
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    }

    /**
     * 配置授权服务,保存用户认证信息
     * @param jdbcTemplate jdbcTemplate
     * @param registeredClientRepository registeredClientRepository
     * @return JdbcOAuth2AuthorizationService
     */
    @Bean
    public JdbcOAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate,
                                                               RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
    }

    /**
     * 添加认证服务器配置，设置jwt签发者、默认端点请求地址等
     * 用于配置Spring Authorization Server的一些全局设置，例如访问令牌的有效期、刷新令牌的策略和认证页面的URL等
     * @return AuthorizationServerSettings
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                // 设置token签发地址(http(s)://{ip}:{port}/context-path, http(s)://domain.com/context-path)
                .issuer(authorizationProperties.getIssuerUrl())
                .build();
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
