package com.danqing.rpc.transport;

import com.danqing.rpc.serializer.CommonSerializer;

/**
 * 服务器类
 *
 * @author danqing
 */
public interface RpcServer {
    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;

    void start();

    <T> void publishService(T service, String serviceName);
}
