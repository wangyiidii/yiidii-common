package cn.yiidii.base.util;

import cn.hutool.core.convert.Convert;
import cn.yiidii.base.contant.ContextConstant;
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

    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = new TransmittableThreadLocal<>();

    public static void set(String key, Object value) {
        Map<String, Object> map = getLocalMap();
        map.put(key, value);
    }

    public static <T> T get(String key, Class<T> type) {
        Map<String, Object> map = getLocalMap();
        return Convert.convert(type, map.get(key));
    }

    public static <T> T get(String key, Class<T> type, Object def) {
        Map<String, Object> map = getLocalMap();
        return Convert.convert(type, map.getOrDefault(key, def));
    }

    public static Map<String, Object> getLocalMap() {
        Map<String, Object> map = THREAD_LOCAL.get();
        if (map == null) {
            map = new ConcurrentHashMap<>(10);
            THREAD_LOCAL.set(map);
        }
        return map;
    }

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
