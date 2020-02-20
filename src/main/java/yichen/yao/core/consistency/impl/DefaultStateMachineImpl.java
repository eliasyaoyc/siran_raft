package yichen.yao.core.consistency.impl;

import com.alibaba.fastjson.JSON;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yichen.yao.core.consistency.StateMachine;
import yichen.yao.core.entity.LogEntry;

import java.io.File;

/**
 * @Author: siran.yao
 * @time: 2020/2/20:下午4:38
 */
public class DefaultStateMachineImpl implements StateMachine {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultStateMachineImpl.class);

    public static String dbDir;
    public static String stateMachineDir;

    public static RocksDB machineDb;

    static {
        if (dbDir == null) {
            dbDir = "./rocksDB-raft/" + System.getProperty("serverPort");
        }
        if (stateMachineDir == null) {
            stateMachineDir =dbDir + "/stateMachine";
        }
        RocksDB.loadLibrary();
    }


    private DefaultStateMachineImpl() {
        synchronized (this) {
            try {
                File file = new File(stateMachineDir);
                boolean success = false;
                if (!file.exists()) {
                    success = file.mkdirs();
                }
                if (success) {
                    LOGGER.warn("make a new dir : " + stateMachineDir);
                }
                Options options = new Options();
                options.setCreateIfMissing(true);
                machineDb = RocksDB.open(options, stateMachineDir);

            } catch (RocksDBException e) {
                LOGGER.info(e.getMessage());
            }
        }
    }

    public static DefaultStateMachineImpl getInstance() {
        return DefaultStateMachineImplLazyHolder.INSTANCE;
    }

    private static class DefaultStateMachineImplLazyHolder {

        private static final DefaultStateMachineImpl INSTANCE = new DefaultStateMachineImpl();
    }

    @Override
    public LogEntry get(String key) {
        try {
            byte[] result = machineDb.get(key.getBytes());
            if (result == null) {
                return null;
            }
            return JSON.parseObject(result, LogEntry.class);
        } catch (RocksDBException e) {
            LOGGER.info(e.getMessage());
        }
        return null;
    }

    @Override
    public String getString(String key) {
        try {
            byte[] bytes = machineDb.get(key.getBytes());
            if (bytes != null) {
                return new String(bytes);
            }
        } catch (RocksDBException e) {
            LOGGER.info(e.getMessage());
        }
        return "";
    }

    @Override
    public void setString(String key, String value) {
        try {
            machineDb.put(key.getBytes(), value.getBytes());
        } catch (RocksDBException e) {
            LOGGER.info(e.getMessage());
        }
    }

    @Override
    public void delString(String... key) {
        try {
            for (String s : key) {
                machineDb.delete(s.getBytes());
            }
        } catch (RocksDBException e) {
            LOGGER.info(e.getMessage());
        }
    }

    @Override
    public synchronized void apply(LogEntry logEntry) {

        try {
            String command = logEntry.getCommand();

            if (command == null) {
                throw new IllegalArgumentException("command can not be null, logEntry : " + logEntry.toString());
            }
            machineDb.put(command.getBytes(), JSON.toJSONBytes(logEntry));
        } catch (RocksDBException e) {
            LOGGER.info(e.getMessage());
        }
    }

}
