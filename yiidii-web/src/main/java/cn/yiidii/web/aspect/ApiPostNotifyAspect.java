package cn.yiidii.web.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.yiidii.base.R;
import cn.yiidii.base.annotation.ApiPostNotify;
import cn.yiidii.base.exception.BizException;
import cn.yiidii.base.util.DesensitizedUtil;
import cn.yiidii.base.util.JsonUtils;
import cn.yiidii.base.util.ServletUtil;
import cn.yiidii.boot.base.BaseAspect;
import cn.yiidii.boot.util.ThreadUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * 后置通切面
 *
 * @author ed w
 * @since 1.0
 */
@Slf4j
@Aspect
@Component
public class ApiPostNotifyAspect extends BaseAspect {

    /**
     * 正常返回通知msg
     *
     * @param point      point
     * @param postNotify {@link ApiPostNotify}
     * @param ret        返回值
     */
    @AfterReturning(value = "@annotation(postNotify)", returning = "ret")
    public void afterReturning(JoinPoint point, ApiPostNotify postNotify, Object ret) {
        String pushPlusToken = ServletUtil.getParameter("pushPlusToken");
        ApiOperation apiOperation = this.getAnnotation(point, ApiOperation.class);
        if (Objects.nonNull(apiOperation) && ret instanceof R) {
            String title = apiOperation.value();
            String msg = ((R<?>) ret).getMsg();
            this.send(pushPlusToken, title, msg);
        }
    }

    /**
     * 发生异常通知msg
     *
     * @param point      point
     * @param postNotify {@link ApiPostNotify}
     * @param ex         异常
     */
    @AfterThrowing(value = "@annotation(postNotify)", throwing = "ex")
    public void afterReturning(JoinPoint point, ApiPostNotify postNotify, Exception ex) {
        String pushPlusToken = ServletUtil.getParameter("pushPlusToken");
        ApiOperation apiOperation = this.getAnnotation(point, ApiOperation.class);
        if (ex instanceof BizException && Objects.nonNull(apiOperation)) {
            String title = apiOperation.value();
            String msg = ex.getMessage();
            this.send(pushPlusToken, title, msg);
        }
    }

    private void send(String token, String title, String msg) {
        if (StrUtil.isAllNotBlank(token, title, msg)) {
            Map<String, String> body = Map.of("token", token, "title", title, "content", msg);
            ThreadUtil.execute(() -> {
                HttpResponse execute = HttpRequest.post("https://www.pushplus.plus/send")
                        .body(JsonUtils.toJsonString(body))
                        .execute();
                log.debug(StrUtil.format("API后置通知结果({}): {}",
                        DesensitizedUtil.address(token, 4), execute.body()));
            });
        }
    }
}