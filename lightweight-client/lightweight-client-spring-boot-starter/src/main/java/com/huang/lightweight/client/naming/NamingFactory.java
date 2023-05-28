package com.huang.lightweight.client.naming;

import com.huang.lightweight.common.exception.LightweightException;
import com.huang.lightweight.common.model.v1.ErrorCode;

import java.util.Properties;

public class NamingFactory {

    private NamingFactory() {
        // private constructor to prevent instantiation
    }

    public static NamingService createNamingService(Properties properties) throws LightweightException {
        try {
            return new LightweightNamingService(properties);
        } catch (Exception e) {
            throw new LightweightException(ErrorCode.PARAMETER_ERROR, e);
        }
    }
}
