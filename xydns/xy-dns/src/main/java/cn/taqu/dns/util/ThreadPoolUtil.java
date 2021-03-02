package cn.taqu.dns.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * @Description:
 * @Author linzt
 * @Date 2021年2月20日 0020
 */
public class ThreadPoolUtil {

    private ThreadPoolUtil(){}

    private final static ThreadFactory THREAD_FACTORY = new ThreadFactoryBuilder().setNameFormat("myThreadPool").build();
    private volatile static ExecutorService executorThreadService;
    private volatile static ScheduledExecutorService executorScheduledThreadService;


    public static ExecutorService getExecutorThreadService() {
        if (executorThreadService == null) {
            synchronized (ThreadPoolUtil.class) {
                /**
                 * 使用谷歌的guava框架
                 * ThreadPoolExecutor参数解释
                 *   1.corePoolSize 核心线程池大小
                 *   2.maximumPoolSize 线程池最大容量大小
                 *   3.keepAliveTime 线程池空闲时，线程存活的时间
                 *   4.TimeUnit 时间单位
                 *   5.ThreadFactory 线程工厂
                 *   6.BlockingQueue任务队列
                 *   7.RejectedExecutionHandler 线程拒绝策略
                 */
                executorThreadService = new ThreadPoolExecutor(10, 20,
                        0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>(1024), THREAD_FACTORY, new ThreadPoolExecutor.AbortPolicy());
            }
        }
        return executorThreadService;
    }

    public static ScheduledExecutorService getExecutorScheduledThreadService() {
        if (executorScheduledThreadService == null) {
            synchronized (ThreadPoolUtil.class) {
                executorScheduledThreadService = new ScheduledThreadPoolExecutor(5,THREAD_FACTORY);
            }
        }
        return executorScheduledThreadService;
    }
}
