package com.danqing.rpc.transport.socket.server;

import com.danqing.rpc.handler.RequestHandler;
import com.danqing.rpc.registry.NacosServiceRegistry;
import com.danqing.rpc.serializer.CommonSerializer;
import com.danqing.rpc.transport.AbstractRpcServer;
import com.danqing.rpc.provider.ServiceProviderImpl;
import com.danqing.rpc.factory.ThreadPoolFactory;
import com.danqing.rpc.hook.ShutdownHook;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * Socket方式远程方法调用的提供者（服务端）
 *
 * @author danqing
 */
public class SocketServer extends AbstractRpcServer {

    private CommonSerializer serializer;
    private final ExecutorService threadPool;
    private RequestHandler requestHandler = new RequestHandler();

    public SocketServer(String host, int port) {
        this(host, port,DEFAULT_SERIALIZER);
    }
    public SocketServer(String host, int port,Integer serializer) {
        this.host = host;
        this.port = port;
        threadPool = ThreadPoolFactory.createDefaultThreadPool("socket-rpc-server");
        this.serviceRegistry = new NacosServiceRegistry();
        this.serviceProvider = new ServiceProviderImpl();
        this.serializer = CommonSerializer.getByCode(serializer);
        scanServices();
    }
    @Override
    public void start() {
        try(ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress(host,port));
            logger.info("服务器启动");
            ShutdownHook.getShutdownHook().addClearAllHook();
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                logger.info("消费者连接: {}:{}",socket.getInetAddress(),socket.getPort());
                threadPool.execute(new RequestHandlerThread(socket,requestHandler,serializer));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            logger.error("服务器启动时有错误发生:", e);
        }
    }
}
