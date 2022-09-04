package cn.yiidii.web.support;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.yiidii.web.constant.CommonConstant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 客户端工具类
 *
 * @author ruoyi
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServletUtil extends cn.hutool.extra.servlet.ServletUtil {

    /**
     * 获取String参数
     */
    public static String getParameter(String name) {
        return getRequest().getParameter(name);
    }

    /**
     * 获取String参数
     */
    public static String getParameter(String name, String defaultValue) {
        return Convert.toStr(getRequest().getParameter(name), defaultValue);
    }

    /**
     * 获取Integer参数
     */
    public static Integer getParameterToInt(String name) {
        return Convert.toInt(getRequest().getParameter(name));
    }

    /**
     * 获取Integer参数
     */
    public static Integer getParameterToInt(String name, Integer defaultValue) {
        return Convert.toInt(getRequest().getParameter(name), defaultValue);
    }

    /**
     * 获取Boolean参数
     */
    public static Boolean getParameterToBool(String name) {
        return Convert.toBool(getRequest().getParameter(name));
    }

    /**
     * 获取Boolean参数
     */
    public static Boolean getParameterToBool(String name, Boolean defaultValue) {
        return Convert.toBool(getRequest().getParameter(name), defaultValue);
    }

    /**
     * 获取request
     */
    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    /**
     * 获取response
     */
    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }

    /**
     * 获取session
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    public static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param string   待渲染的字符串
     */
    public static void renderString(HttpServletResponse response, String string) {
        try {
            response.setStatus(HttpStatus.HTTP_OK);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
            response.getWriter().print(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否是Ajax异步请求
     *
     * @param request
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {

        String accept = request.getHeader("accept");
        if (accept != null && accept.contains(MediaType.APPLICATION_JSON_VALUE)) {
            return true;
        }

        String xRequestedWith = request.getHeader("X-Requested-With");
        if (xRequestedWith != null && xRequestedWith.contains("XMLHttpRequest")) {
            return true;
        }

        String uri = request.getRequestURI();
        if (StrUtil.equalsAnyIgnoreCase(uri, ".json", ".xml")) {
            return true;
        }

        String ajax = request.getParameter("__ajax");
        return StrUtil.equalsAnyIgnoreCase(ajax, "json", "xml");
    }

    public static String getClientIP() {
        return getClientIP(getRequest());
    }

    /**
     * 内容编码
     *
     * @param str 内容
     * @return 编码后的内容
     */
    public static String urlEncode(String str) {
        return URLEncoder.encode(str, StandardCharsets.UTF_8);
    }

    /**
     * 内容解码
     *
     * @param str 内容
     * @return 解码后的内容
     */
    public static String urlDecode(String str) {
        return URLDecoder.decode(str, StandardCharsets.UTF_8);
    }

    /**
     * 根据ip获取地址信息
     *
     * @return 地址信息
     */
    public static String getLocation() {
        String clientIP = getClientIP();
        try {
            HttpResponse resp = HttpRequest.get(StrUtil.format("https://ip.useragentinfo.com/json?ip={}", clientIP)).execute();
            JSONObject body = JSONUtil.parseObj(resp.body());
            return StrUtil.format("{}{}{}{}",
                    body.get("country"),
                    body.get("province"),
                    body.get("city"),
                    body.get("area")
            );
        } catch (Exception e) {
            return CommonConstant.UNKNOWN_ZH;
        }
    }

    /**
     * 获取UA信息
     *
     * @return {@link UserAgent}
     */
    public static UserAgent getUserAgent() {
        return UserAgentUtil.parse(getHeader(getRequest(), Header.USER_AGENT.getValue(), StandardCharsets.UTF_8));
    }

    /**
     * 获取当前请求cookie字符串
     *
     * @return a=xxx; b=yyy
     */
    public static String getCookieStr() {
        return Arrays.stream(getRequest().getCookies())
                .map(e -> StrUtil.format("{}={}", e.getName(), e.getValue()))
                .collect(Collectors.joining("; "));
    }

    /**
     * 获取当前请求cookie指定key的值
     *
     * @param key key
     * @return cookie值
     */
    public static String getCookieValue(String key) {
        return Arrays.stream(getRequest().getCookies()).filter(e -> StrUtil.equals(key, e.getName())).map(e -> e.getValue()).findFirst().orElse("");
    }

    /**
     * cookie字符串转map
     *
     * @param cookie cookie字符串
     * @return cookie map
     */
    public static Map<String, String> getCookieMap(String cookie) {
        if (StrUtil.isBlank(cookie)) {
            return Collections.emptyMap();
        }
        return Arrays.stream(cookie.split(";"))
                .filter(e -> StrUtil.isNotBlank(e) && e.contains("="))
                .collect(Collectors.toMap(
                        e -> StrUtil.split(e, "=", true, false).get(0),
                        e -> StrUtil.split(e, "=", true, false).get(1),
                        (e1, e2) -> e2
                ));
    }

    /**
     * 获取cookie的值
     *
     * @param cookie cookie
     * @param key    key
     * @return value
     */
    public static String getCookieValue(String cookie, String key) {
        return getCookieMap(cookie).getOrDefault(key, "");
    }

}
