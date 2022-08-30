package cn.yiidii.base.exception;


import cn.yiidii.base.exception.code.BaseExceptionCode;

/**
 * 限流异常
 *
 * @author ed w
 * @version 1.0
 */
public class RateLimitException extends BaseUncheckedException {
    public RateLimitException(BaseExceptionCode baseExceptionCode) {
        super(baseExceptionCode);
    }
}
