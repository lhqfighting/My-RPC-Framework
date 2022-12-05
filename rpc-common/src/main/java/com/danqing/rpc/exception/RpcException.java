package com.danqing.rpc.exception;

import com.danqing.rpc.enumeration.RpcError;

/**
 * RPC异常
 *
 * @author danqing
 */
public class RpcException extends RuntimeException{

    public RpcException(RpcError error, String detail) {
        super(error.getMessage() + ": " + detail);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(RpcError error) {
        super(error.getMessage());
    }
}
