package cn.yiidii.mybatis.enumeration;

import cn.yiidii.base.enumeration.Enumerator;
import com.baomidou.mybatisplus.annotation.EnumValue;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;

/**
 * 通用状态枚举
 *
 * @author YiiDii Wang
 * @since 1.0
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Status", description = "通用状态枚举")
public enum Status implements Enumerator {

    /**
     * 可用
     */
    ENABLED(0, "可用"),
    /**
     * 禁用
     */
    DISABLED(10, "禁用"),
    /**
     * 已删除
     */
    DELETED(20, "已删除");

    @EnumValue
    private int code;
    private String desc;

    @Override
    public Integer code() {
        return this.code;
    }

    @Override
    public String desc() {
        return this.desc;
    }

    public static Status get(int val, Status def) {
        return Stream.of(values()).parallel().filter(item -> item.code == val).findAny().orElse(def);
    }

}
