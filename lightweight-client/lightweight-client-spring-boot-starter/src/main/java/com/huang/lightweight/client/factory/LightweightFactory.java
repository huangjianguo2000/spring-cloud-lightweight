package com.huang.lightweight.client.factory;

import com.huang.lightweight.client.naming.NamingFactory;
import com.huang.lightweight.client.naming.NamingService;
import com.huang.lightweight.common.exception.LightweightException;

import java.util.Properties;

public class LightweightFactory {

    private LightweightFactory() {
        // private constructor to prevent instantiation
    }

    /**
     * Create naming service
     *
     * @param properties init param
     * @return Naming
     * @throws LightweightException Exception
     */
    public static NamingService createNamingService(Properties properties) throws LightweightException {
        return NamingFactory.createNamingService(properties);
    }


}
