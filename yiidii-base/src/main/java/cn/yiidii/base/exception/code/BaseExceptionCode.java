package cn.yiidii.base.exception.code;

/**
 * 异常编码
 *
 * @author ed w
 */
public interface BaseExceptionCode {
    /**
     * 异常编码
     *
     * @return 异常编码
     */
    int getCode();

    /**
     * 异常消息
     *
     * @return 异常消息
     */
    String getMsg();
}
