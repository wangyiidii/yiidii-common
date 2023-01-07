package cn.yiidii.auth.config;

import cn.dev33.satoken.exception.DisableLoginException;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.yiidii.auth.consts.AuthExceptionCode;
import cn.yiidii.base.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

/**
 * AuthExceptionHandler
 *
 * @author ed w
 * @since 1.0
 */
@Order(1)
@Slf4j
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@RestControllerAdvice
@RequiredArgsConstructor
public class AuthExceptionHandler {

    public final HttpServletRequest request;

    @PostConstruct
    public void init() {
        System.out.println("AuthExceptionHandler init");
    }

    @ExceptionHandler({NotLoginException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public R<?> notLoginException(NotLoginException ex) {
        log.error("请求地址: {}, 未登录: {}", request.getRequestURI(), ex.getMessage());
        log.debug("请求地址: {}, 未登录: {}", request.getRequestURI(), ex);
        return R.failed(AuthExceptionCode.UNAUTHORIZED.getCode(), AuthExceptionCode.UNAUTHORIZED.getMsg());
    }

    @ExceptionHandler({NotPermissionException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public R<?> noPermissionException(NotPermissionException ex) {
        log.error("请求地址: {}, 无权限: {}", request.getRequestURI(), ex.getMessage());
        log.debug("请求地址: {}, 无权限: {}", request.getRequestURI(), ex);
        return R.failed(AuthExceptionCode.PERMISSION_FORBIDDEN.getCode(), AuthExceptionCode.PERMISSION_FORBIDDEN.getMsg());
    }

    @ExceptionHandler({NotRoleException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public R<?> notRoleException(NotRoleException ex) {
        log.error("请求地址: {}, 无角色: {}", request.getRequestURI(), ex.getMessage());
        log.debug("请求地址: {}, 无角色: {}", request.getRequestURI(), ex);
        return R.failed(AuthExceptionCode.ROLE_PERMISSION_FORBIDDEN.getCode(), AuthExceptionCode.ROLE_PERMISSION_FORBIDDEN.getMsg());
    }

    @ExceptionHandler({DisableLoginException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public R<?> disableLoginException(DisableLoginException ex) {
        log.error("请求地址: {}, 此账号已被封禁, 限制登录: {}", request.getRequestURI(), ex.getMessage());
        log.debug("请求地址: {}, 此账号已被封禁, 限制登录: {}", request.getRequestURI(), ex);
        return R.failed(AuthExceptionCode.ACCOUNT_DISABLED.getCode(), AuthExceptionCode.ACCOUNT_DISABLED.getMsg());
    }
}
