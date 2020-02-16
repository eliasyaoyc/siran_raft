package yichen.yao.core.consistency;

import yichen.yao.core.entity.LogEntry;

/**
 * @Author: siran.yao
 * @time: 2020/2/14:下午8:08
 */
public interface LogManager {

    /**
     * 写日志
     * @param logEntry
     */
    void write(LogEntry logEntry);

    /**
     * 读日志
     * @param index
     * @return
     */
    LogEntry read(Long index);

    void removeIndex(Long index);

    LogEntry getLast();

    Long getLastIndex();
}
