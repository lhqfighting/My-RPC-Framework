package com.danqing.rpc.provider;

/**
 * 服务注册接口
 *
 * @author danqing
 */
public interface ServiceProvider {
    <T> void addServiceProvider(T service, String serviceName);

    Object getServiceProvider(String serviceName);
}
