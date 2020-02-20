package yichen.yao.core.common.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: siran.yao
 * @time: 2020/2/19:下午2:24
 */
public class RaftThread extends Thread {
    private static final Logger LOGGER = LoggerFactory.getLogger(RaftThread.class);
    private static final UncaughtExceptionHandler uncaughtExceptionHandler = (t, e)
            -> LOGGER.warn("Exception occurred from thread {}", t.getName(), e);

    public RaftThread(ThreadGroup group,Runnable runnable, String name) {
        super(group,runnable,name);
        setUncaughtExceptionHandler(uncaughtExceptionHandler);
    }
}
