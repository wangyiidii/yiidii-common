package cn.yiidii.web.filter;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.yiidii.base.contant.ContextConstant;
import cn.yiidii.base.util.ContextUtil;
import cn.yiidii.base.util.ServletUtil;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 链路ID过滤器
 *
 * @author ed w
 */
@Component
@WebFilter(filterName = "TraceLogFilter", urlPatterns = "/*")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TraceLogFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String traceId = ServletUtil.getHeader((HttpServletRequest) request, ContextConstant.KEY_LOG_TRACE_ID, StandardCharsets.UTF_8);
        if (StrUtil.isBlank(traceId)) {
            traceId = RandomUtil.randomString(32);
        }

        MDC.put(ContextConstant.KEY_LOG_TRACE_ID, traceId);
        ContextUtil.set(ContextConstant.KEY_LOG_TRACE_ID, traceId);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        MDC.remove(ContextConstant.KEY_LOG_TRACE_ID);
    }
}
