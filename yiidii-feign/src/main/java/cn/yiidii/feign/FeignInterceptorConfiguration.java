package cn.yiidii.feign;

import cn.hutool.core.util.StrUtil;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * feign拦截器
 *
 * @author pangu
 */
@Slf4j
@Component
public class FeignInterceptorConfiguration {

    /**
     * 使用feign client发送请求时，传递请求头
     *
     * @return {@link RequestInterceptor}
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            String cookie = requestAttributes.getRequest().getHeader(HttpHeaders.COOKIE);
            if (StrUtil.isNotBlank(cookie)) {
                requestTemplate.header(HttpHeaders.COOKIE, cookie);
            }
        };
    }
}