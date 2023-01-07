package cn.yiidii.auth.consts;

import cn.yiidii.base.exception.code.BaseExceptionCode;
import lombok.AllArgsConstructor;

/**
 * AuthExceptionCode
 *
 * @author ed w
 * @since 1.0
 */
@AllArgsConstructor
public enum AuthExceptionCode implements BaseExceptionCode {

    UNAUTHORIZED(1001, "登录状态失效"),
    PERMISSION_FORBIDDEN(1002, "无此权限"),
    ROLE_PERMISSION_FORBIDDEN(1003, "无此权限"),
    ACCOUNT_DISABLED(1004, "此账号已被限制登录"),

    ;

    private final int code;
    private final String message;

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.message;
    }
}
