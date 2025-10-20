package com.infoepoch.cmgs.common;

import com.infoepoch.cmgs.core.PropertiesLoader;
import com.infoepoch.cmgs.store.ServiceInstanceManager;

public class CommonCache {

    private static PropertiesLoader propertiesLoader = new PropertiesLoader();

    private static ServiceInstanceManager serviceInstanceManager = new ServiceInstanceManager();

    public static ServiceInstanceManager getServiceInstanceManager() {
        return serviceInstanceManager;
    }

    public static void setServiceInstanceManager(ServiceInstanceManager serviceInstanceManager) {
        CommonCache.serviceInstanceManager = serviceInstanceManager;
    }

    public static PropertiesLoader getPropertiesLoader() {
        return propertiesLoader;
    }

    public static void setPropertiesLoader(PropertiesLoader propertiesLoader) {
        CommonCache.propertiesLoader = propertiesLoader;
    }
}
