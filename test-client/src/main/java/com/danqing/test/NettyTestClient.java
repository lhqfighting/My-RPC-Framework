package com.danqing.test;

import com.danqing.rpc.api.ByeService;
import com.danqing.rpc.api.HelloObject;
import com.danqing.rpc.api.HelloService;
import com.danqing.rpc.serializer.CommonSerializer;
import com.danqing.rpc.transport.RpcClient;
import com.danqing.rpc.transport.RpcClientProxy;
import com.danqing.rpc.transport.netty.client.NettyClient;

/**
 * Netty客户端测试
 *
 * @author danqing
 */
public class NettyTestClient {
    public static void main(String[] args) {
        RpcClient client = new NettyClient(CommonSerializer.PROTOBUF_SERIALIZER);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12,"This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
        ByeService byeService = rpcClientProxy.getProxy(ByeService.class);
        System.out.println(byeService.bye("Netty"));
    }
}
