package cn.yiidii.web.support;

import cn.dev33.satoken.strategy.SaStrategy;
import cn.yiidii.base.core.service.SensitiveService;
import cn.yiidii.web.constant.PermissionConstant;
import cn.yiidii.web.satoken.LoginHelper;
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
