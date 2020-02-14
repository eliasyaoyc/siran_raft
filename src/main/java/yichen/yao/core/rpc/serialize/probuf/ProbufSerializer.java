package yichen.yao.core.rpc.serialize.probuf;

import yichen.yao.core.common.constants.SerializerType;
import yichen.yao.core.rpc.serialize.Serializer;

/**
 * @Author: siran.yao
 * @time: 2020/2/14:上午11:12
 */
public class ProbufSerializer implements Serializer {
    @Override
    public byte getSerializerAlgorithm() {
        return SerializerType.PROTO_BUF;
    }

    @Override
    public byte[] serialize(Object object) {
        return new byte[0];
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return null;
    }
}
