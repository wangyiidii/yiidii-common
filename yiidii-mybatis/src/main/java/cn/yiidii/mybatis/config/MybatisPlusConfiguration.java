package cn.yiidii.mybatis.config;

import cn.yiidii.mybatis.handler.MetaObjectHandler;
import cn.yiidii.mybatis.interceptor.SqlLogInterceptor;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * mybatis plus配置
 *
 * @author YiiDii Wang
 * @create 2021-09-27 23:27
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
@AllArgsConstructor
@EnableTransactionManagement
public class MybatisPlusConfiguration {

    /**
     * 自动填充数据
     */
    @Bean
    @ConditionalOnMissingBean(MetaObjectHandler.class)
    public MetaObjectHandler mateMetaObjectHandler() {
        return new MetaObjectHandler();
    }

    /**
     * sql 日志
     */
    @Bean
    @ConditionalOnProperty(value = "mybatis-plus.sql-log.enable")
    public SqlLogInterceptor sqlLogInterceptor() {
        return new SqlLogInterceptor();
    }

    /**
     * 分页插件
     *
     * @return MybatisPlusInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
