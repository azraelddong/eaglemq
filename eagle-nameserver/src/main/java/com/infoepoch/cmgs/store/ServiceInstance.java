package com.infoepoch.cmgs.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceInstance {

    private String brokerIp;

    private int brokerPort;

    private long firstRegistrationTime;

    private long lastRegistrationTime;

    private Map<String, String> attrs = new HashMap<>();
}
