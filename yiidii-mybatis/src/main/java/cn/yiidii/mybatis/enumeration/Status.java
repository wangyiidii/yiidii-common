package cn.yiidii.mybatis.enumeration;

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
 * @create 2021-03-21 11:44
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Status", description = "通用状态枚举")
public enum Status {

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

    public static Status get(int val, Status def) {
        return Stream.of(values()).parallel().filter(item -> item.code == val).findAny().orElse(def);
    }

}
