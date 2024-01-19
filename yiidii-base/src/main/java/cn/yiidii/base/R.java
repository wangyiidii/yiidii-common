package cn.yiidii.base;

import cn.yiidii.base.contant.CommonConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @param <T>
 * @author lengleng
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel("通用响应模型")
@SuppressWarnings("unused")
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Setter
    @ApiModelProperty(value = "状态码")
    private int code;

    @Setter
    @ApiModelProperty(value = "信息")
    private String msg;

    @Setter
    @ApiModelProperty(value = "返回数据")
    private T data;

    @Setter
    @ApiModelProperty(value = "时间戳")
    private long timestamp;

    public static <T> R<T> ok() {
        return restResult(null, CommonConstant.SUCCESS, null);
    }

    public static <T> R<T> ok(T data) {
        return restResult(data, CommonConstant.SUCCESS, CommonConstant.RESP_SUCCESS);
    }

    public static <T> R<T> ok(T data, String msg) {
        return restResult(data, CommonConstant.SUCCESS, msg);
    }

    public static <T> R<T> failed() {
        return restResult(null, CommonConstant.FAIL, CommonConstant.RESP_FAILURE);
    }

    public static <T> R<T> failed(String msg) {
        return restResult(null, CommonConstant.FAIL, msg);
    }

    public static <T> R<T> failed(T data) {
        return restResult(data, CommonConstant.FAIL, CommonConstant.RESP_FAILURE);
    }

    public static <T> R<T> failed(T data, String msg) {
        return restResult(data, CommonConstant.FAIL, msg);
    }

    public static <T> R<T> failed(int code, String msg) {
        return restResult(null, code, msg);
    }

    public static <T> R<T> failed(int code, String msg, T data) {
        return restResult(data, code, msg);
    }

    private static <T> R<T> restResult(T data, int code, String msg) {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        apiResult.setTimestamp(System.currentTimeMillis());
        return apiResult;
    }

}
