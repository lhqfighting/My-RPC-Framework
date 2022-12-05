package com.danqing.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * 基于轮询的负载均衡算法
 *
 * @author danqing
 */
public class RoundRobinLoadBalancer implements LoadBalancer{

    private volatile int index = 0;

    @Override
    public Instance select(List<Instance> instances) {
        if (index >= instances.size()) {
            index %= instances.size();
        }
        return instances.get(index++);
    }
}
