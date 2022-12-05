package com.danqing.rpc.exception;

/**
 * 序列化异常
 *
 * @author danqing
 */
public class SerializeException extends RuntimeException {
    public SerializeException(String msg) {
        super(msg);
    }
}
