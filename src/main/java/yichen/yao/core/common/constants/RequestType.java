package yichen.yao.core.common.constants;

/**
 * @Author: siran.yao
 * @time: 2020/2/14:上午9:27
 */
public class RequestType {

    public static final Byte VOTE_REQUEST = 1;
    public static final Byte APPEND_ENTRIES_REQUEST = 2;
    public static final Byte INSTALL_SNAPSHOT_REQUEST = 3;
    public static final Byte VOTE_RESPONSE = 4;
    public static final Byte APPEND_ENTRIES_RESPONSE = 5;
    public static final Byte INSTALL_SNAPSHOT_RESPONSE = 6;
    public static final Byte CLIENT_REQUEST = 7;
    public static final Byte CLIENT_RESPONSE = 8;
}
