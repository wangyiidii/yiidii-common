package cn.yiidii.boot.util;

import cn.hutool.extra.spring.SpringUtil;
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
     * @see {@link cn.yiidii.boot.config.ThreadPoolConfig}
     */
    private final static ThreadPoolTaskExecutor THREAD_POOL_TASK_EXECUTOR = SpringUtil.getBean("threadPoolTaskExecutor", ThreadPoolTaskExecutor.class);

    /**
     * 异步执行任务
     *
     * @param runnable runnable
     */
    public static void execute(Runnable runnable) {
        THREAD_POOL_TASK_EXECUTOR.execute(runnable);
    }

}
