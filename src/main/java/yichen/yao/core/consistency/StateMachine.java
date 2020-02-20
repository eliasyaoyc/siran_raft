package yichen.yao.core.consistency;

import yichen.yao.core.entity.LogEntry;

/**
 * @Author: siran.yao
 * @time: 2020/2/15:下午12:24
 */
public interface StateMachine {

    /**
     * 把已经提交的日志 运行到状态机上(插入数据库)
     * @param logEntry
     */
    void apply(LogEntry logEntry);

    LogEntry get(String key);

    String getString(String key);

    void setString(String key, String value);

    void delString(String... key);

}
