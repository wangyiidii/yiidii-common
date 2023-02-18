package cn.yiidii.boot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 *
 * @author ed w
 * @since 1.0
 */
@Slf4j
@EnableAsync
@Configuration
public class ThreadPoolConfig {

    public static final String GLOBAL_SCHEDULED_EXECUTOR = "globalScheduledExecutor";
    public static final String GLOBAL_ASYNC_EXECUTOR = "globalAsyncExecutor";

    /**
     * CPU个数
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    /**
     * 核心线程数 = cpu 核心数 + 1
     */
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    /**
     * 最大线程数
     */
    private static final int MAX_POOL_SIZE = CPU_COUNT * 2 + 1;
    /**
     * 允许线程空闲时间（单位：默认为秒）
     */
    private static final int KEEP_ALIVE_TIME = 300;
    /**
     * 缓冲队列大小
     */
    private static final int QUEUE_CAPACITY = 128;
    /**
     * 线程池名前缀
     */
    private static final String SCHEDULED_EXECUTOR_NAME_PREFIX = "async-scheduledExecutor-%s";
    private static final String ASYNC_EXECUTOR_NAME_PREFIX = "async-executor-%s";

    /**
     * 定时任务线程池
     */
    @Bean(ThreadPoolConfig.GLOBAL_SCHEDULED_EXECUTOR)
    public ThreadPoolTaskExecutor scheduledExecutor() {
        return constructor(SCHEDULED_EXECUTOR_NAME_PREFIX);
    }

    /**
     * 通用线程池
     */
    @Bean(ThreadPoolConfig.GLOBAL_ASYNC_EXECUTOR)
    public ThreadPoolTaskExecutor asyncExecutor() {
        return constructor(ASYNC_EXECUTOR_NAME_PREFIX);
    }

    private ThreadPoolTaskExecutor constructor(String scheduledExecutorNamePrefix) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        executor.setThreadNamePrefix(scheduledExecutorNamePrefix);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

}
