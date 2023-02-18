package cn.yiidii.web.interceptor;

import cn.yiidii.base.util.ContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 上下文过滤器
 *
 * @author ed w
 * @since 1.0
 */
@Slf4j
public class ContextInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ContextUtil.clear();
    }
}
