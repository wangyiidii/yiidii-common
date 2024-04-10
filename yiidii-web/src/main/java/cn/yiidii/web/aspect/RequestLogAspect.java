package cn.yiidii.web.aspect;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.yiidii.base.exception.BaseUncheckedException;
import cn.yiidii.base.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * 请求日志记录
 *
 * @author ed w
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class RequestLogAspect {

    public final HttpServletRequest httpServletRequest;

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)"
    )
    public void pointCut() {
    }

    @Before("pointCut()")
    public void before(JoinPoint jp) {
        log.info("[{}] {}", httpServletRequest.getRequestURI(), getMethodArgs(jp));
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        long startTime = System.currentTimeMillis();
        Object res = pjp.proceed();
        long took = System.currentTimeMillis() - startTime;

        log.info("[{}ms] [{}] {} ", took, httpServletRequest.getRequestURI(), JsonUtils.toJsonString(res));

        return res;
    }

    @AfterThrowing(pointcut = "pointCut()", throwing = "ex")
    public void afterThrow(Exception ex) {
        if (ex instanceof BaseUncheckedException) {
            log.warn("[{}], ex: {}", httpServletRequest.getRequestURI(), ExceptionUtil.stacktraceToString(ex));
        } else {
            log.error("[{}], ex: {}", httpServletRequest.getRequestURI(), ExceptionUtil.stacktraceToString(ex));
        }

    }

    private String getMethodArgs(JoinPoint point) {
        Object[] args = point.getArgs();
        if (args == null || args.length == 0) {
            return "";
        }
        try {
            Map<String, Object> params = new HashMap<>();
            String[] parameterNames = ((MethodSignature) point.getSignature()).getParameterNames();
            for (int i = 0; i < parameterNames.length; i++) {
                Object arg = args[i];
                // 过滤不能转换成JSON的参数
                if ((arg instanceof ServletRequest) || (arg instanceof ServletResponse)) {
                    continue;
                } else if ((arg instanceof MultipartFile)) {
                    arg = arg.toString();
                }
                params.put(parameterNames[i], arg);
            }
            return JsonUtils.toJsonString(params);
        } catch (Exception e) {
            log.error("接口出入参日志打印切面处理请求参数异常", e);
        }
        return Arrays.toString(args);
    }
}
