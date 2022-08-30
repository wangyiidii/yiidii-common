package cn.yiidii.web.satoken;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.json.JSONUtil;
import cn.yiidii.web.support.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ed w
 * @since 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {

    @Override
    public List<String> getPermissionList(Object userId, String loginType) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        List<String> permissions = loginUser.getPermission();
        log.debug("用户: {}({}), 权限编码: {}", loginUser.getUsername(), loginUser.getUserId(), JSONUtil.toJsonStr(permissions));
        return permissions;
    }

    @Override
    public List<String> getRoleList(Object userId, String loginType) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        List<String> roles = loginUser.getRoles();
        log.debug("用户ID: {}({}), 角色编码: {}", loginUser.getUsername(), loginUser.getUserId(), JSONUtil.toJsonStr(roles));
        return roles;
    }
}
