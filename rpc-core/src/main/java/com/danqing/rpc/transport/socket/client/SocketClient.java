package com.danqing.rpc.transport.socket.client;

import com.danqing.rpc.entity.RpcRequest;
import com.danqing.rpc.entity.RpcResponse;
import com.danqing.rpc.enumeration.ResponseCode;
import com.danqing.rpc.enumeration.RpcError;
import com.danqing.rpc.exception.RpcException;
import com.danqing.rpc.loadbalancer.LoadBalancer;
import com.danqing.rpc.loadbalancer.RandomLoadBalancer;
import com.danqing.rpc.registry.NacosServiceDiscovery;
import com.danqing.rpc.registry.ServiceDiscovery;
import com.danqing.rpc.serializer.CommonSerializer;
import com.danqing.rpc.transport.RpcClient;
import com.danqing.rpc.transport.socket.util.ObjectReader;
import com.danqing.rpc.transport.socket.util.ObjectWriter;
import com.danqing.rpc.util.RpcMessageChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * socket客户端
 *
 * @author danqing
 */
public class SocketClient implements RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);

    private final ServiceDiscovery serviceDiscovery;

    private CommonSerializer serializer;

    public SocketClient(){
        this(DEFAULT_SERIALIZER,new RandomLoadBalancer());
    }
    public SocketClient(Integer serializer){
        this(serializer,new RandomLoadBalancer());
    }
    public SocketClient(LoadBalancer loadBalancer) {
        this(DEFAULT_SERIALIZER,loadBalancer);
    }

    public SocketClient(Integer serializer, LoadBalancer loadBalancer){
        this.serializer = CommonSerializer.getByCode(serializer);
        this.serviceDiscovery = new NacosServiceDiscovery(loadBalancer);
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        if(serializer == null) {
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
        try (Socket socket = new Socket()){
            socket.connect(inetSocketAddress);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            ObjectWriter.writeObject(outputStream,rpcRequest,serializer);
            Object obj = ObjectReader.readObject(inputStream);
            RpcResponse rpcResponse = (RpcResponse) obj;
            if (rpcResponse == null) {
                logger.error("服务调用失败，service：{}", rpcRequest.getInterfaceName());
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, " service:" + rpcRequest.getInterfaceName());
            }
            if (rpcResponse.getStatusCode() == null || rpcResponse.getStatusCode() != ResponseCode.SUCCESS.getCode()) {
                logger.error("调用服务失败, service: {}, response:{}", rpcRequest.getInterfaceName(), rpcResponse);
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, " service:" + rpcRequest.getInterfaceName());
            }
            RpcMessageChecker.check(rpcRequest,rpcResponse);
            return rpcResponse;
        } catch (IOException e) {
            logger.error("调用时有错误发生：", e);
            throw new RpcException("服务调用失败: ", e);
        }
    }
}
