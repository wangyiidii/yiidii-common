package cn.yiidii.base.core.service;

/**
 * 配置service
 *
 * @author ed w
 * @since 1.0
 */
public interface ConfigService {

    /**
     * 获取配置
     *
     * @param key key
     * @param dft 默认值
     * @return value
     */
    String get(String key, String dft);
}
