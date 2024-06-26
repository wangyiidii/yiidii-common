package cn.yiidii.base.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * process执行结果
 *
 * @author ed w
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProcessResultDTO<T> {
    private Integer code;
    private T result;
    private String raw;
    private String message;

    public static <T> ProcessResultDTO<T> error(Integer code, T ret, String message) {
        return new ProcessResultDTO<T>(code, ret, "", message);
    }
}
