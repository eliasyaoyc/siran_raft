package yichen.yao.core.entity;

import lombok.Data;

/**
 * @Author: siran.yao
 * @time: 2020/2/14:下午8:09
 */
@Data
public class LogEntry {
    private String command;
    private Long logIndex;
    private int logTerm;

    private LogEntry(String command, long logIndex, int logTerm) {
        this.command = command;
        this.logIndex = logIndex;
        this.logTerm = logTerm;
    }

    public static LogEntryBuilder builder(){
        return new LogEntryBuilder();
    }

    public static class LogEntryBuilder{
        private String command;
        private Long logIndex;
        private int logTerm;

        public LogEntryBuilder command(String command) {
            this.command = command;
            return this;
        }

        public LogEntryBuilder logIndex(Long logIndex) {
            this.logIndex = logIndex;
            return this;
        }

        public LogEntryBuilder logTerm(int logTerm) {
            this.logTerm = logTerm;
            return this;
        }

        public LogEntry build(){
            return new LogEntry(command,logIndex,logTerm);
        }
    }
}
