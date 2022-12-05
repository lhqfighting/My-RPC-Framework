package com.danqing.test;

import com.danqing.rpc.annotation.Service;
import com.danqing.rpc.api.ByeService;

/**
 *
 * @author danqing
 */
@Service
public class ByeServiceImpl implements ByeService {
    @Override
    public String bye(String name) {
        return "bye, " + name;
    }
}
