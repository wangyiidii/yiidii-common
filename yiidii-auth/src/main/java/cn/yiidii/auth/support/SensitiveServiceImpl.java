package cn.yiidii.auth.support;

import cn.dev33.satoken.strategy.SaStrategy;
import cn.yiidii.auth.consts.PermissionConstant;
import cn.yiidii.auth.satoken.LoginHelper;
import cn.yiidii.base.core.service.SensitiveService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * SensitiveServiceImpl
 *
 * @author ed w
 * @since 1.0
 */
@Component
public class SensitiveServiceImpl implements SensitiveService {
    @Override
    public boolean isSensitive() {
        List<String> permissions = LoginHelper.getLoginUser().getPermission();
        return !SaStrategy.me.hasElement.apply(permissions, PermissionConstant.IS_SENSITIVE);
    }

}
