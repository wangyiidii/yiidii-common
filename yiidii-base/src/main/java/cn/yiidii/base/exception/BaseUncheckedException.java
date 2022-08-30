package cn.yiidii.base.exception;

import cn.yiidii.base.exception.code.BaseExceptionCode;

/**
 * 非运行时异常基类，所有自定义非运行时异常继承该类
 *
 * @author YiiDii Wang
 * @date 2021/2/11 14:46:57
 */
public class BaseUncheckedException extends RuntimeException implements BaseException {

    private static final long serialVersionUID = -778887391066124051L;

    /**
     * 异常信息
     */
    private String message;

    /**
     * 具体异常码
     */
    private int code;

    public BaseUncheckedException(Throwable cause) {
        super(cause);
    }

    public BaseUncheckedException(final int code, Throwable cause) {
        super(cause);
        this.code = code;
    }


    public BaseUncheckedException(final int code, final String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BaseUncheckedException(final int code, final String message, Throwable cause) {
        super(cause);
        this.code = code;
        this.message = message;
    }

    public BaseUncheckedException(final int code, final String format, Object... args) {
        super(String.format(format, args));
        this.code = code;
        this.message = String.format(format, args);
    }

    public BaseUncheckedException(BaseExceptionCode baseExceptionCode) {
        super(baseExceptionCode.getMsg());
        this.code = baseExceptionCode.getCode();
        this.message = baseExceptionCode.getMsg();
    }

    public BaseUncheckedException() {

    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getCode() {
        return code;
    }
}
