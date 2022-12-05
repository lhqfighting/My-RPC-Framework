package com.danqing.rpc.transport.netty.client;

import com.danqing.rpc.entity.RpcRequest;
import com.danqing.rpc.entity.RpcResponse;
import com.danqing.rpc.enumeration.RpcError;
import com.danqing.rpc.exception.RpcException;
import com.danqing.rpc.factory.SingletonFactory;
import com.danqing.rpc.loadbalancer.LoadBalancer;
import com.danqing.rpc.loadbalancer.RandomLoadBalancer;
import com.danqing.rpc.registry.NacosServiceDiscovery;
import com.danqing.rpc.registry.ServiceDiscovery;
import com.danqing.rpc.serializer.CommonSerializer;
import com.danqing.rpc.transport.RpcClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * NIO方式消费侧客户端
 *
 * @author danqing
 */
public class NettyClient implements RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    private static final Bootstrap bootstrap;
    private static final EventLoopGroup group;

    static {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class);
    }

    private final ServiceDiscovery serviceDiscovery;
    private final CommonSerializer serializer;
    private final UnprocessedRequests unprocessedRequests;

    public NettyClient(){
        this(DEFAULT_SERIALIZER,new RandomLoadBalancer());
    }
    public NettyClient(LoadBalancer loadBalancer) {
        this(DEFAULT_SERIALIZER, loadBalancer);
    }
    public NettyClient(Integer serializer) {
        this(serializer, new RandomLoadBalancer());
    }
    public NettyClient(Integer serializer, LoadBalancer loadBalancer){
        this.serializer = CommonSerializer.getByCode(serializer);
        this.serviceDiscovery = new NacosServiceDiscovery(loadBalancer);
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
    }
    @Override
    public CompletableFuture<RpcResponse> sendRequest(RpcRequest rpcRequest) {
        if (serializer == null) {
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        CompletableFuture<RpcResponse> resultFuture = new CompletableFuture<>();
        try {
            InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
            Channel channel = ChannelProvider.get(inetSocketAddress, serializer);
            if (!channel.isActive()){
                group.shutdownGracefully();
                return null;
            }
            unprocessedRequests.put(rpcRequest.getRequestId(),resultFuture);
            channel.writeAndFlush(rpcRequest).addListener((ChannelFutureListener) future1 -> {
                if (future1.isSuccess()) {
                    logger.info(String.format("客户端发送消息: %s", rpcRequest.toString()));
                } else {
                    future1.channel().close();
                    resultFuture.completeExceptionally(future1.cause());
                    logger.error("发送消息时有错误发生: ", future1.cause());
                }
            });
        } catch (IllegalStateException | InterruptedException e) {
            unprocessedRequests.remove(rpcRequest.getRequestId());
            logger.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
        return resultFuture;
    }
}
