package com.danqing.test;

import com.danqing.rpc.annotation.Service;
import org.slf4j.Logger;
import com.danqing.rpc.api.HelloObject;
import com.danqing.rpc.api.HelloService;
import org.slf4j.LoggerFactory;

@Service
public class HelloServiceImpl implements HelloService {
    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String hello(HelloObject helloObject) {
        logger.info("接受消息:{}", helloObject.getMessage());
        return "这是Impl1方法" + "---"+"调用的返回值id=" + helloObject.getId();
    }
}
