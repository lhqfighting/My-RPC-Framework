package com.danqing.rpc.transport.socket.server;

import com.danqing.rpc.entity.RpcRequest;
import com.danqing.rpc.entity.RpcResponse;
import com.danqing.rpc.handler.RequestHandler;
import com.danqing.rpc.serializer.CommonSerializer;
import com.danqing.rpc.transport.socket.util.ObjectReader;
import com.danqing.rpc.transport.socket.util.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

/**
 * 请求处理线程
 *
 * @author danqing
 */
public class RequestHandlerThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerThread.class);

    private Socket socket;
    private RequestHandler requestHandler;
    private CommonSerializer serializer;

    public RequestHandlerThread(Socket socket, RequestHandler requestHandler, CommonSerializer serializer) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serializer = serializer;
    }

    @Override
    public void run() {
        try(InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream()){
            RpcRequest rpcRequest = (RpcRequest) ObjectReader.readObject(inputStream);
            Object result = requestHandler.handle(rpcRequest);
            RpcResponse<Object> response = RpcResponse.success(result,rpcRequest.getRequestId());
            ObjectWriter.writeObject(outputStream,response,serializer);
        } catch (IOException e){
            logger.error("调用或发送时有错误发生：", e);
        }
    }
}
