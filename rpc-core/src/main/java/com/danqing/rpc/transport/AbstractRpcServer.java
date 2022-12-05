package com.danqing.rpc.transport;

import com.danqing.rpc.annotation.Service;
import com.danqing.rpc.annotation.ServiceScan;
import com.danqing.rpc.enumeration.RpcError;
import com.danqing.rpc.exception.RpcException;
import com.danqing.rpc.registry.ServiceRegistry;
import com.danqing.rpc.provider.ServiceProvider;
import com.danqing.rpc.util.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Set;

/**
 *
 * @author danqing
 */
public abstract class AbstractRpcServer implements RpcServer {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractRpcServer.class);

    protected String host;
    protected int port;

    protected ServiceRegistry serviceRegistry;
    protected ServiceProvider serviceProvider;

    public void scanServices() {
        String mainClassName = ReflectUtil.getStackTrace();
        Class<?> startClass;
        try {
            startClass = Class.forName(mainClassName);
            if (!startClass.isAnnotationPresent(ServiceScan.class)){
                logger.error("启动类缺少 @ServiceScan 注解");
                throw new RpcException(RpcError.SERVICE_SCAN_PACKAGE_NOT_FOUND);
            }
        }catch (ClassNotFoundException e){
            logger.error("出现未知错误");
            throw new RpcException(RpcError.UNKNOWN_ERROR);
        }
        String basePackage = startClass.getAnnotation(ServiceScan.class).value();
        if ("".equals(basePackage)){
            basePackage = mainClassName.substring(0,mainClassName.lastIndexOf("."));
        }
        Set<Class<?>> classSet = ReflectUtil.getClasses(basePackage);
        for (Class<?> clazz : classSet) {
            if (clazz.isAnnotationPresent(Service.class)) {
                String serviceName = clazz.getAnnotation(Service.class).name();
                Object obj;
                try {
                    obj = clazz.newInstance();
                } catch (IllegalAccessException | InstantiationException e) {
                    logger.error("创建 " + clazz + " 时有错误发生");
                    continue;
                }
                if ("".equals(serviceName)){
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> oneInterface : interfaces) {
                        publishService(obj,oneInterface.getCanonicalName());
                    }
                } else {
                    publishService(obj,serviceName);
                }
            }
        }
    }

    @Override
    public <T> void publishService(T service, String serviceName) {
        serviceProvider.addServiceProvider(service,serviceName);
        serviceRegistry.register(serviceName,new InetSocketAddress(host,port));
    }
}
