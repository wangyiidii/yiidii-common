package cn.yiidii.auth.config;

import cn.dev33.satoken.exception.DisableServiceException;
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

    @ExceptionHandler({NotLoginException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public R<?> notLoginException(NotLoginException ex) {
        return R.failed(AuthExceptionCode.UNAUTHORIZED.getCode(), AuthExceptionCode.UNAUTHORIZED.getMsg());
    }

    @ExceptionHandler({NotPermissionException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public R<?> noPermissionException(NotPermissionException ex) {
        return R.failed(AuthExceptionCode.PERMISSION_FORBIDDEN.getCode(), AuthExceptionCode.PERMISSION_FORBIDDEN.getMsg());
    }

    @ExceptionHandler({NotRoleException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public R<?> notRoleException(NotRoleException ex) {
        return R.failed(AuthExceptionCode.ROLE_PERMISSION_FORBIDDEN.getCode(), AuthExceptionCode.ROLE_PERMISSION_FORBIDDEN.getMsg());
    }

    @ExceptionHandler({DisableServiceException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public R<?> disableLoginException(DisableServiceException ex) {
        return R.failed(AuthExceptionCode.ACCOUNT_DISABLED.getCode(), AuthExceptionCode.ACCOUNT_DISABLED.getMsg());
    }
}
