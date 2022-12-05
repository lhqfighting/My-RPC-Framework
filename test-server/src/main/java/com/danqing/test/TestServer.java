package com.danqing.test;

import com.danqing.rpc.annotation.ServiceScan;
import com.danqing.rpc.serializer.CommonSerializer;
import com.danqing.rpc.transport.socket.server.SocketServer;

/**
 * 服务端测试
 *
 * @author danqing
 */
@ServiceScan
public class TestServer {
    public static void main(String[] args) {
        SocketServer socketServer = new SocketServer("127.0.0.1", 9998, CommonSerializer.HESSIAN_SERIALIZER);
        socketServer.start();
    }
}
