package com.infoepoch.cmgs.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalProperties {

    /**
     * 配置目录
     */
    private String eagleMqHome;

    private String nameServerIp;

    private Integer nameServerPort;

    private String nameServerUserName;

    private String nameServerPassword;
}
