package com.danqing.test;

import com.danqing.rpc.api.ByeService;
import com.danqing.rpc.api.HelloObject;
import com.danqing.rpc.api.HelloService;
import com.danqing.rpc.serializer.CommonSerializer;
import com.danqing.rpc.serializer.HessianSerializer;
import com.danqing.rpc.transport.RpcClientProxy;
import com.danqing.rpc.transport.socket.client.SocketClient;

/**
 * 客户端测试
 *
 * @author danqing
 */
public class TestClient {
    public static void main(String[] args) {
        SocketClient client = new SocketClient(CommonSerializer.KRYO_SERIALIZER);
        RpcClientProxy clientProxy = new RpcClientProxy(client);
        HelloService helloService = clientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12,"This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
        ByeService byeService = clientProxy.getProxy(ByeService.class);
        System.out.println(byeService.bye("Socket"));
    }
}
