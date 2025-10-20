package com.infoepoch.cmgs;

import com.infoepoch.cmgs.common.CommonCache;
import com.infoepoch.cmgs.core.NameServerStarter;

import java.io.IOException;

public class NameServerApplication {
    public static void main(String[] args) throws InterruptedException, IOException {
        NameServerStarter nameServerStarter = new NameServerStarter(8020);
        CommonCache.getPropertiesLoader().loadProperties();
        nameServerStarter.start();
    }
}
