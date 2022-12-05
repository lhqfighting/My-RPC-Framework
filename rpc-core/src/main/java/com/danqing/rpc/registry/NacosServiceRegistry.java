package com.danqing.rpc.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.danqing.rpc.enumeration.RpcError;
import com.danqing.rpc.exception.RpcException;
import com.danqing.rpc.util.NacosUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;


/**
 * Nacos服务注册中心
 *
 * @author danqing
 */
public class NacosServiceRegistry implements ServiceRegistry{

    private static final Logger logger = LoggerFactory.getLogger(NacosServiceRegistry.class);
    /**
     * 将服务的名称和地址注册进服务注册中心
     *
     * @param serviceName
     * @param inetSocketAddress
     */
    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            NacosUtil.registerService(serviceName, inetSocketAddress);
        } catch (NacosException e) {
            logger.error("注册服务时有错误发生:", e);
            throw new RpcException(RpcError.REGISTER_SERVICE_FAILED);
        }
    }

}
