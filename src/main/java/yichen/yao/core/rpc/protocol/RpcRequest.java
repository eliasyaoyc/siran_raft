package yichen.yao.core.rpc.protocol;

import lombok.Data;

/**
 * @Author: siran.yao
 * @time: 2020/2/13:下午5:53
 */
@Data
public class RpcRequest {

    /**
     * 请求类型
     */
    private byte requestType;
}
