package com.danqing.rpc.registry;

import java.net.InetSocketAddress;

/**
 * 将一个服务注册进注册表
 *
 * @author danqing
 */
public interface ServiceRegistry {
    void register(String serviceName, InetSocketAddress inetSocketAddress);
}
