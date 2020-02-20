package yichen.yao.core.consistency.impl;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yichen.yao.core.consistency.LogManager;
import yichen.yao.core.entity.LogEntry;

import java.io.File;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @Author: siran.yao
 * @time: 2020/2/19:下午7:27
 */
@Data
public class DefaultLogImpl implements LogManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultNodeImpl.class);

    public static String dbDir;
    public static String logsDir;
    private static RocksDB rocksDB;

    public final static byte[] LAST_INDEX_KEY = "LAST_INDEX_KEY".getBytes();

    private ReentrantLock lock = new ReentrantLock();

    static {
        if(dbDir == null)
            dbDir = "./rocksDB-raft" + System.getProperty("serverPort");
        if(logsDir == null)
            logsDir = dbDir + "/logModule";
        RocksDB.loadLibrary();
    }

    public DefaultLogImpl() {
        Options options = new Options();
        options.setCreateIfMissing(true);

        File file = new File(logsDir);
        boolean success = false;
        if (!file.exists())
            success = file.mkdir();
        if (success)
            LOGGER.info("make a new dir : {}",logsDir);
        try {
            rocksDB = RocksDB.open(options,logsDir);
        } catch (RocksDBException e) {
            LOGGER.error("exception {}",e);
        }
    }

    public static DefaultLogImpl getInstance(){
        return DefaultLogLazyHolder.INSTANCE;
    }

    private static class DefaultLogLazyHolder{
        private static final DefaultLogImpl INSTANCE = new DefaultLogImpl();
    }
    @Override
    public void write(List<LogEntry> logEntries) {
        for(LogEntry entry : logEntries)
            write(entry);
    }

    @Override
    public void write(LogEntry logEntry) {
        boolean success = false;
        try {
            lock.tryLock(3000, MILLISECONDS);
            logEntry.setLogIndex(getLastIndex() + 1);
            rocksDB.put(logEntry.getLogIndex().toString().getBytes(), JSON.toJSONBytes(logEntry));
            success = true;
            LOGGER.info("DefaultLogModule write rocksDB success, logEntry info : [{}]", logEntry);
        } catch (RocksDBException | InterruptedException e) {
            LOGGER.warn(e.getMessage());
        } finally {
            if (success) {
                updateLastIndex(logEntry.getLogIndex());
            }
            lock.unlock();
        }
    }

    @Override
    public LogEntry read(Long index) {
        try {
            byte[] result = rocksDB.get(convert(index));
            if (result == null) {
                return null;
            }
            return JSON.parseObject(result, LogEntry.class);
        } catch (RocksDBException e) {
            LOGGER.warn(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void removeIndex(Long index) {
        boolean success = false;
        int count = 0;
        try {
            lock.tryLock(3000, MILLISECONDS);
            for (long i = index; i <= getLastIndex(); i++) {
                rocksDB.delete(String.valueOf(i).getBytes());
                ++count;
            }
            success = true;
            LOGGER.warn("rocksDB removeOnStartIndex success, count={} startIndex={}, lastIndex={}", count, index, getLastIndex());
        } catch (InterruptedException | RocksDBException e) {
            LOGGER.warn(e.getMessage());
        } finally {
            if (success) {
                updateLastIndex(getLastIndex() - count);
            }
            lock.unlock();
        }
    }

    @Override
    public LogEntry getLast() {
        try {
            byte[] result = rocksDB.get(convert(getLastIndex()));
            if (result == null) {
                return null;
            }
            return JSON.parseObject(result, LogEntry.class);
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Long getLastIndex() {
        byte[] lastIndex = "-1".getBytes();
        try {
            lastIndex = rocksDB.get(LAST_INDEX_KEY);
            if (lastIndex == null) {
                lastIndex = "-1".getBytes();
            }
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
        return Long.valueOf(new String(lastIndex));
    }



    private byte[] convert(Long key) {
        return key.toString().getBytes();
    }

    // on lock
    private void updateLastIndex(Long index) {
        try {
            // overWrite
            rocksDB.put(LAST_INDEX_KEY, index.toString().getBytes());
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
    }
}
