package yichen.yao.core.rpc.serialize;

/**
 * @Author: siran.yao
 * @time: 2020/2/14:上午10:02
 */
public interface Serializer {
    /**
     * 序列化算法
     * @return
     */
    byte getSerializerAlgorithm();

    /**
     * java 对象转换成二进制
     */
    byte[] serialize(Object object);

    /**
     * 二进制转换成 java 对象
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);
}
