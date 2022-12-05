package com.danqing.test;

import com.danqing.rpc.annotation.ServiceScan;
import com.danqing.rpc.serializer.CommonSerializer;
import com.danqing.rpc.transport.netty.server.NettyServer;


/**
 * Netty服务端测试
 *
 * @author danqing
 */
@ServiceScan
public class NettyTestServer {
    public static void main(String[] args) {
        NettyServer server = new NettyServer("127.0.0.1", 9999, CommonSerializer.PROTOBUF_SERIALIZER);
        server.start();
    }
}
