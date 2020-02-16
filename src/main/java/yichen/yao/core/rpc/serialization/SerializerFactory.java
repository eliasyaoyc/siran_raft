package yichen.yao.core.rpc.serialization;

import yichen.yao.core.rpc.serialization.fastjson.FastJsonSerializer;

/**
 * @Author: siran.yao
 * @time: 2020/2/14:上午10:06
 */
public class SerializerFactory {
    private Serializer serializer;

    //默认使用fastJson
    public SerializerFactory() {
        serializer = new FastJsonSerializer();
    }

    public SerializerFactory(Serializer serializer){
        this.serializer = serializer;
    }

    public Serializer getSerializer(){
        return serializer;
    }
}
