package cn.yiidii.web.constant;

/**
 * CommonConstant
 *
 * @author lengleng
 * @date 2019/2/1
 */
public interface CommonConstant {

    /**
     * 成功标记
     */
    Integer SUCCESS = 0;

    /**
     * 失败标记
     */
    Integer FAIL = 1;

    /**
     * 成功响应
     */
    String RESP_SUCCESS = "处理成功";

    /**
     * 失败响应
     */
    String RESP_FAILURE = "处理失败";

    String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    String NORM_DATE_PATTERN = "yyyy-MM-dd";
    String NORM_TIME_PATTERN = "HH:mm:ss";

    String SA_LOGIN_USER_KEY = "login-user";
    String UNKNOWN_ZH = "未知";
    String UNKNOWN_EN = "Unknown";

}
