package cn.yiidii.boot.util;

import cn.yiidii.boot.config.ThreadPoolConfig;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 线程工具类
 *
 * @author ed w
 * @since 1.0
 */
public class ThreadUtil {

    /**
     * 线程池
     *
     * @see ThreadPoolConfig
     */
    private final static ThreadPoolTaskExecutor GLOBAL_ASYNC_EXECUTOR = SpringUtil.getBean(ThreadPoolConfig.GLOBAL_ASYNC_EXECUTOR, ThreadPoolTaskExecutor.class);

    /**
     * 异步执行任务
     *
     * @param runnable runnable
     */
    public static void execute(Runnable runnable) {
        GLOBAL_ASYNC_EXECUTOR.execute(runnable);
    }

}
