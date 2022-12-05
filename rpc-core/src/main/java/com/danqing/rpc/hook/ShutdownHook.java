package com.danqing.rpc.hook;

import com.danqing.rpc.factory.ThreadPoolFactory;
import com.danqing.rpc.util.NacosUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 注销服务的方法
 *
 * @author danqing
 */
public class ShutdownHook {

    private static final Logger logger = LoggerFactory.getLogger(ShutdownHook.class);

    private static final ShutdownHook shutdownHook = new ShutdownHook();

    public static ShutdownHook getShutdownHook() {
        return shutdownHook;
    }

    public void addClearAllHook() {
        logger.info("关闭后将自动注销所有服务");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            NacosUtil.clearRegistry();
            ThreadPoolFactory.shutDownAll();
        }));
    }
}
