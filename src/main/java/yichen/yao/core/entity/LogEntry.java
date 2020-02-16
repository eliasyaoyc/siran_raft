package yichen.yao.core.entity;

import lombok.Data;

/**
 * @Author: siran.yao
 * @time: 2020/2/14:下午8:09
 */
@Data
public class LogEntry {
    private String command;
    private long logIndex;
    private int logTerm;
}
