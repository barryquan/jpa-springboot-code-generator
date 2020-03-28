package com.github.barry.akali.base.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.github.barry.akali.base.utils.UserUtil;

/**
 * 开启jpa自动填充创建人、创建时间、更新人、更新时间的配置
 * 
 * @author barry
 *
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {

    /**
     * 实现AuditorAware接口，这样才会填充创建和更新的操作人
     * 
     * @return
     */
    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditorAware<String>() {
            @Override
            public Optional<String> getCurrentAuditor() {
                return Optional.ofNullable(UserUtil.getCurrentUser());
            }
        };
    }
}
