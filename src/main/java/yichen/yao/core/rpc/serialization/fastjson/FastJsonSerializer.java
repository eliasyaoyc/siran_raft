package yichen.yao.core.rpc.serialization.fastjson;

import com.alibaba.fastjson.JSON;
import yichen.yao.core.common.constants.SerializerType;
import yichen.yao.core.rpc.serialization.Serializer;

/**
 * @Author: siran.yao
 * @time: 2020/2/14:上午10:05
 */
public class FastJsonSerializer implements Serializer {
    @Override
    public byte getSerializerAlgorithm() {
        return SerializerType.FAST_JSON;
    }

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes,clazz);
    }
}
