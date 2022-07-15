package cn.yiidii.base.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumerator
 *
 * @author ed w
 * @since 1.0
 */
public interface Enumerator {
    /**
     * 获取枚举码值
     * 序列化时采用改值
     * @return  code
     */
    @JsonValue
    Integer code();

    /**
     * 获取枚举描述
     * @return  desc
     */
    String desc();
}
