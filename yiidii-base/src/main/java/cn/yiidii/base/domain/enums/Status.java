package cn.yiidii.base.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Objects;
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
public enum Status implements IEnum {

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

    public boolean in(Status... status) {
        if (Objects.isNull(status)) {
            return false;
        }
        return Arrays.stream(status).parallel().filter(this::equals).findAny().isPresent();
    }

    public static Status get(int val, Status def) {
        return Stream.of(values()).parallel().filter(item -> item.code == val).findAny().orElse(def);
    }

}
