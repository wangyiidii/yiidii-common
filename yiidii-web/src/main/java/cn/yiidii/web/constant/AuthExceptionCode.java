package cn.yiidii.web.constant;

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

    // auth相关
    UNAUTHORIZED(100001, "登录状态失效"),
    ACCOUNT_DISABLED(100002, "此账号已被限制登录"),

    PASSWORD_INCORRECT(100002, "密码不正确"),
    EMAIL_EXIST(100003, "邮箱已被注册"),


    // user相关
    USERNAME_EXIST(110001, "用户名已存在"),
    USER_UNREGISTER(110002, "用户未注册"),

    USER_UN_DISABLED(110004, "用户未禁用"),


    // role相关
    ROLE_CODE_EXIST(120001, "角色编码已存在"),
    ROLE_DISABLED(120002, "角色已被禁用"),
    ROLE_USER_EXIST(120003, "角色存在关联用户，请解除绑定后再删除角色"),
    ROLE_NOT_EXIST(120004, "角色不存在或已被删除"),


    // permission 相关
    PERMISSION_FORBIDDEN(130001, "无此权限"),
    ROLE_PERMISSION_FORBIDDEN(130002, "无此权限"),
    PERMISSION_NOT_EXIST_OR_DELETED(130004, "权限不存在或已删除"),
    PERMISSION_ROLE_EXIST(130005, "权限已绑定角色，请解绑后再操作"),
    PERMISSION_DELETE_EX(130006, "权限批量删除发生错误"),
    PERMISSION_CODE_EXIST(130007, "权限编码已存在"),

    // resource相关
    UN_SUPPORTED_RESOURCE_TYPE(140001, "不支持的资源类型"),

    // third party相关
    UNSUPPORTED_PLATFORM(150001, "暂不支持此平台"),


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
