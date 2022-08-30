package cn.yiidii.base.util;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @author ed w
 * @since 1.0
 */
public class DesensitizedUtil extends cn.hutool.core.util.DesensitizedUtil {

    /**
     * ip地址脱敏
     *
     * @param ipaddr    ip地址
     * @param pos       只支持1,2,3,4
     * @return
     */
    public static String ipaddr(String ipaddr, int... pos) {
        if (StrUtil.isBlank(ipaddr)) {
            return "";
        } else {
            if (pos.length == 0) {
                return ipaddr;
            } else {
                if (pos.length > 4) {
                    pos = ArrayUtil.sub(pos, 0, 4);
                }
                String[] split = ipaddr.split("\\.");
                for (int i = 1; i <= split.length; i++) {
                    if (ArrayUtil.contains(pos, i)) {
                        split[i - 1] = "*";
                    }
                }
                return ArrayUtil.join(split, StrPool.DOT);
            }
        }
    }

}
