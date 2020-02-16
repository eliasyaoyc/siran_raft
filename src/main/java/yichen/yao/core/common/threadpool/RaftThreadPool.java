package yichen.yao.core.common.threadpool;

import java.util.concurrent.*;

/**
 * @Author: siran.yao
 * @time: 2020/2/15:下午4:48
 */
public class RaftThreadPool extends RaftThreadPoolExecutor{
    public static final RaftThreadPool INSTANCE = new RaftThreadPool();

    private static ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    private static int corePoolSize = Runtime.getRuntime().availableProcessors();
    private static int maximumPoolSize = corePoolSize * 2;
    private static final long keepAliveTime = 1000 * 60;
    private static final int queueSize = 1024;

    public RaftThreadPool() {
        super(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(queueSize),
                new RaftThreadFactory());
        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(corePoolSize,new RaftThreadFactory());
    }

    public static void scheduleAtFixedRate(Runnable runnable,int initDelay,int delay){
        scheduledThreadPoolExecutor.scheduleAtFixedRate(runnable,initDelay,delay,TimeUnit.MILLISECONDS);

    }
    public static void scheduleWithFixedDelay(Runnable runnable,int delay){
        scheduledThreadPoolExecutor.scheduleWithFixedDelay(runnable,0,delay,TimeUnit.MILLISECONDS);
    }
}
