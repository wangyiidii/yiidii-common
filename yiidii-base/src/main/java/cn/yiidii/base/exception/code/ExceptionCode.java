package cn.yiidii.base.exception.code;


/**
 * 状态码
 *
 * @author ed w
 */
@SuppressWarnings("unused")
public enum ExceptionCode implements BaseExceptionCode {

    // 系统相关
    SUCCESS(0, "成功"),
    PARAM_EX(400, "参数错误"),
    REQUIRED_FILE_PARAM_EX(400, "请求中必须至少包含一个有效文件"),
    REQUIRED_TOO_LARGE_EX(400, "请求文件过大"),
    NOT_FOUND(404, "没有找到资源"),
    METHOD_NOT_ALLOWED(405, "不支持当前请求类型"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误，请稍后重试！"),
    SYSTEM_EX(503, "系统繁忙，请稍后再试！"),
    RATE_LIMIT(600, "请求过快"),

    ;

    private final int code;
    private String msg;

    ExceptionCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }


    public ExceptionCode build(String msg, Object... param) {
        this.msg = String.format(msg, param);
        return this;
    }

    public ExceptionCode param(Object... param) {
        msg = String.format(msg, param);
        return this;
    }
}
