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

    private Integer brokerPort;

    private Long firstRegistrationTime;

    private Long lastRegistrationTime;

    private Map<String, String> attrs = new HashMap<>();
}
