package com.danqing.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据包类型（请求包 / 响应包）
 *
 * @author danqing
 */
@AllArgsConstructor
@Getter
public enum PackageType {

    REQUEST_PACK(0),
    RESPONSE_PACK(1);

    private final int code;

}
