package com.danqing.rpc.transport;

import com.danqing.rpc.entity.RpcRequest;
import com.danqing.rpc.serializer.CommonSerializer;

/**
 * 客户端类通用接口
 *
 * @author danqing
 */
public interface RpcClient {

    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;

    Object sendRequest(RpcRequest rpcRequest);
}
