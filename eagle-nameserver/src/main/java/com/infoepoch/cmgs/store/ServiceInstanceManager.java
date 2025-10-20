package com.infoepoch.cmgs.store;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceInstanceManager {

    private Map<String, ServiceInstance> map = new ConcurrentHashMap<>();

    public void put(ServiceInstance serviceInstance) {
        map.put(serviceInstance.getBrokerIp() + ":" + serviceInstance.getBrokerPort(), serviceInstance);
    }

    public ServiceInstance get(String brokerIp, int brokerPort) {
        return map.get(brokerIp + ":" + brokerPort);
    }
}
