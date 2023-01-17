package cn.yiidii.auth.satoken;


import cn.dev33.satoken.strategy.SaStrategy;
import cn.hutool.core.util.StrUtil;
import cn.yiidii.base.contant.HttpConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * sa-token 鉴权切面
 *
 * @author ed w
 */
@Slf4j
@Aspect
@Component
@Order(-100)
@RequiredArgsConstructor
public class SaCheckAspect {

    private final HttpServletRequest request;

    @Pointcut("@within(cn.dev33.satoken.annotation.SaCheckLogin) || @annotation(cn.dev33.satoken.annotation.SaCheckLogin) || @within(cn.dev33.satoken.annotation.SaCheckRole) || @annotation(cn.dev33.satoken.annotation.SaCheckRole) || @within(cn.dev33.satoken.annotation.SaCheckPermission) || @annotation(cn.dev33.satoken.annotation.SaCheckPermission) || @within(cn.dev33.satoken.annotation.SaCheckSafe) || @annotation(cn.dev33.satoken.annotation.SaCheckSafe) || @within(cn.dev33.satoken.annotation.SaCheckBasic) || @annotation(cn.dev33.satoken.annotation.SaCheckBasic)")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void around(JoinPoint joinPoint) {
        String inner = request.getHeader(HttpConstant.InnerHeader.INNER_FEIGN);
        if (StrUtil.isNotBlank(inner)) {
            log.debug("内部接口调用，忽略鉴权");
            return;
        }

        log.debug("进入鉴权");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        SaStrategy.me.checkMethodAnnotation.accept(signature.getMethod());
    }
}
