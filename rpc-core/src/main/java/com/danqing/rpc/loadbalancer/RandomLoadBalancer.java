package com.danqing.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;
import javafx.beans.property.ReadOnlyMapProperty;

import java.util.List;
import java.util.Random;

/**
 * 基于随机策略的负载均衡算法
 *
 * @author danqing
 */
public class RandomLoadBalancer implements LoadBalancer{
    @Override
    public Instance select(List<Instance> instances) {
        return instances.get(new Random().nextInt(instances.size()));
    }
}
