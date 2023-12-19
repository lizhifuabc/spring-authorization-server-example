package com.tomato.authorization.server.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Repository;

/**
 * 客户端repository实现
 *
 * @author lizhifu
 * @since 2023/12/19
 */
@Slf4j
@Repository
public class CustomRegisteredClientRepository extends JdbcRegisteredClientRepository {
    public CustomRegisteredClientRepository(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        log.info("保存客户端信息：{}", registeredClient);
        super.save(registeredClient);
    }

    @Override
    public RegisteredClient findById(String id){
        log.info("根据id查询客户端信息：{}", id);
        return super.findById(id);
    }

    @Override
    public RegisteredClient findByClientId(String clientId){
        log.info("根据clientId查询客户端信息：{}", clientId);
        return super.findByClientId(clientId);
    }
}
