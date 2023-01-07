package cn.yiidii.auth.support;

import cn.yiidii.base.annotation.Sensitive;
import cn.yiidii.base.domain.enums.SensitiveStrategy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 登录用户身份权限
 *
 * @author Lion Li
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 用户唯一标识
     */
    private String token;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;

    /**
     * 登录IP地址
     */
    @Sensitive(strategy = SensitiveStrategy.IPADDR)
    private String ipaddr;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 菜单权限
     */
    private List<String> menus;

    /**
     * 角色权限
     */
    private List<String> permission;

    /**
     * 数据权限 当前角色ID
     */
    private List<String> roles;

}
