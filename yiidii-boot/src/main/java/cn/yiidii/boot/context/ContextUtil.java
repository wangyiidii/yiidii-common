package cn.yiidii.boot.context;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 上下文工具类
 *
 * @author YiiDii Wang
 * @create 2021-04-13 11:44
 */
public class ContextUtil {

    private ContextUtil() {
    }

    private static final ThreadLocal<Map<String, String>> THREAD_LOCAL = new TransmittableThreadLocal<>();

    public static void set(String key, Object value) {
        Map<String, String> map = getLocalMap();
        map.put(key, value == null ? StrUtil.EMPTY : value.toString());
    }

    public static <T> T get(String key, Class<T> type) {
        Map<String, String> map = getLocalMap();
        return Convert.convert(type, map.get(key));
    }

    public static <T> T get(String key, Class<T> type, Object def) {
        Map<String, String> map = getLocalMap();
        return Convert.convert(type, map.getOrDefault(key, String.valueOf(def == null ? StrUtil.EMPTY : def)));
    }

    public static String get(String key) {
        Map<String, String> map = getLocalMap();
        return map.getOrDefault(key, StrUtil.EMPTY);
    }

    public static Map<String, String> getLocalMap() {
        Map<String, String> map = THREAD_LOCAL.get();
        if (map == null) {
            map = new ConcurrentHashMap<>(10);
            THREAD_LOCAL.set(map);
        }
        return map;
    }

    public static void setLocalMap(Map<String, String> localMap) {
        THREAD_LOCAL.set(localMap);
    }

    /**
     * 获取Authorization
     *
     * @return Authorization
     */
    public static String getAuthorization() {
        return get(ContextConstant.BASIC_HEADER_KEY, String.class);
    }

    public static void setAuthorization(String authorization) {
        set(ContextConstant.BASIC_HEADER_KEY, authorization);
    }

    /**
     * 用户ID
     *
     * @return 用户ID
     */
    public static Long getUserId() {
        return get(ContextConstant.JWT_KEY_USER_ID, Long.class, 0L);
    }

    public static String getUserIdStr() {
        return String.valueOf(getUserId());
    }

    /**
     * 用户ID
     *
     * @param userId 用户ID
     */
    public static void setUserId(Long userId) {
        set(ContextConstant.JWT_KEY_USER_ID, userId);
    }

    public static void setUserId(String userId) {
        set(ContextConstant.JWT_KEY_USER_ID, userId);
    }
}
