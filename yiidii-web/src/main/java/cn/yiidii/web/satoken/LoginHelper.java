package cn.yiidii.web.satoken;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.yiidii.web.constant.CommonConstant;
import cn.yiidii.web.support.LoginUser;

import java.util.List;

/**
 * LoginHelper
 *
 * @author ed w
 * @since 1.0
 */
public class LoginHelper {

    public static void login(LoginUser loginUser) {
        SaHolder.getStorage().set(CommonConstant.SA_LOGIN_USER_KEY, loginUser);
        StpUtil.login(loginUser.getUserId(),
                SaLoginConfig
                        .setExtra("id", loginUser.getUserId())
                        .setExtra("username", loginUser.getUsername())
                        .setExtra("nickname", loginUser.getNickname())
        );
        loginUser.setToken(StpUtil.getTokenInfo().getTokenValue());
        setLoginUser(loginUser);
    }

    public static void setLoginUser(LoginUser loginUser) {
        StpUtil.getTokenSession().set(CommonConstant.SA_LOGIN_USER_KEY, loginUser);
    }

    /**
     * 获取用户
     */
    public static LoginUser getLoginUser() {
        LoginUser loginUser = (LoginUser) SaHolder.getStorage().get(CommonConstant.SA_LOGIN_USER_KEY);
        if (loginUser != null) {
            return loginUser;
        }
        loginUser = (LoginUser) StpUtil.getTokenSession().get(CommonConstant.SA_LOGIN_USER_KEY);
        SaHolder.getStorage().set(CommonConstant.SA_LOGIN_USER_KEY, loginUser);
        return loginUser;
    }

    public static boolean isAdmin() {
        LoginUser loginUser = getLoginUser();
        return loginUser.getUserId().equals(1L);
    }

    public static boolean isAdmin(Long id) {
        return id.equals(1L);
    }
}
