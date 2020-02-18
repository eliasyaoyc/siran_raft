package yichen.yao.core.common.threadpool;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: siran.yao
 * @time: 2020/2/15:下午4:50
 */
public class RaftThreadFactory implements ThreadFactory {
    private final ThreadGroup tg;
    private final AtomicInteger poolNum = new AtomicInteger(1);
    private final AtomicInteger threadNum = new AtomicInteger(1);
    private static String namePrefix;

    public RaftThreadFactory() {
        tg = Thread.currentThread().getThreadGroup();
        namePrefix = "raft"+ "-" + poolNum.getAndIncrement() + "thread-";
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(tg,runnable,namePrefix+threadNum.getAndIncrement());
        if(thread.isDaemon())
            thread.setDaemon(true);
        if(thread.getPriority() == Thread.MAX_PRIORITY)
            thread.setPriority(Thread.NORM_PRIORITY);
        return thread;
    }

}
