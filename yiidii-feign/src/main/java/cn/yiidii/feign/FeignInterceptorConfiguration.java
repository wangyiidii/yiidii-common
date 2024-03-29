package cn.yiidii.feign;

import cn.yiidii.base.contant.HttpConstant;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

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
            // inner-feign请求头忽略鉴权
            requestTemplate.header(HttpConstant.InnerHeader.INNER_FEIGN, "xxx");

            // 传递cookie
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            String cookie = requestAttributes.getRequest().getHeader(HttpHeaders.COOKIE);
            if (Objects.nonNull(cookie) && cookie.length() > 0) {
                requestTemplate.header(HttpHeaders.COOKIE, cookie);
            }
        };
    }
}