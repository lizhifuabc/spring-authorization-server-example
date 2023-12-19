package com.tomato.authorization.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.stereotype.Service;

/**
 * 授权确认服务
 *
 * @author lizhifu
 * @since 2023/12/19
 */
@Service
@Slf4j
public class OAuth2AuthorizationConsentServiceImpl implements OAuth2AuthorizationConsentService {
    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        log.info("保存授权确认信息:{}", authorizationConsent);
    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        log.info("删除授权确认信息:{}", authorizationConsent);
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        log.info("查询授权确认信息:{}", registeredClientId);
        return null;
    }
}
