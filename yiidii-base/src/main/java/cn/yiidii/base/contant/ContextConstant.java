package cn.yiidii.base.contant;

/**
 * 跟上下文常量工具类
 *
 * @author zuihou
 * @date 2018/12/21
 */
public final class ContextConstant {
    private ContextConstant() {
    }

    public static final String JWT_SIGNING_KEY = "wang";

    /**
     * JWT中封装的 用户id
     */
    public static final String JWT_KEY_USER_ID = "userid";
    /**
     * JWT中封装的 用户名称
     */
    public static final String JWT_KEY_NAME = "name";
    /**
     * JWT中封装的 token 类型
     */
    public static final String JWT_KEY_TOKEN_TYPE = "token_type";
    /**
     * JWT中封装的 用户账号
     */
    public static final String JWT_KEY_ACCOUNT = "account";

    /**
     * JWT中封装的 客户端id
     */
    public static final String JWT_KEY_CLIENT_ID = "client_id";

    /**
     * JWT中封装的 租户编码
     */
    public static final String JWT_KEY_TENANT = "tenant";
    /**
     * 刷新 Token
     */
    public static final String REFRESH_TOKEN_KEY = "refresh_token";

    /**
     * User信息 认证请求头
     */
    public static final String BEARER_HEADER_KEY = "token";
    /**
     * User信息 认证请求头前缀
     */
    public static final String BEARER_HEADER_PREFIX = "Bearer ";
    /**
     * User信息 认证请求头前缀
     */
    public static final String BEARER_HEADER_PREFIX_EXT = "Bearer%20";

    /**
     * Client信息认证请求头
     */
    public static final String BASIC_HEADER_KEY = "Authorization";

    /**
     * Client信息认证请求头前缀
     */
    public static final String BASIC_HEADER_PREFIX = "Basic ";

    /**
     * Client信息认证请求头前缀
     */
    public static final String BASIC_HEADER_PREFIX_EXT = "Basic%20";

    /**
     * 是否boot项目
     */
    public static final String IS_BOOT = "boot";

    /**
     * 日志链路追踪id信息头
     */
    public static final String HEADER_PIGEON_TRACE_ID = "pigeon-trace-id";
    /**
     * 日志链路追踪id日志标志
     */
    public static final String KEY_LOG_TRACE_ID = "trace-id";

    /**
     * 灰度发布版本号
     */
    public static final String GRAY_VERSION = "grayversion";
}
