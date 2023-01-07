package cn.yiidii.auth.config;

import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpLogic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SaTokenConfig
 *
 * @author ed w
 * @since 1.0
 */
@Configuration
public class SaTokenConfig {

    /**
     * JWT 配置
     *
     * @return StpLogic
     */
    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }
}
