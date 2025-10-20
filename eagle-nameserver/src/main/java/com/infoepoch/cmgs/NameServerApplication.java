package com.infoepoch.cmgs;

import com.infoepoch.cmgs.core.NameServerStarter;

public class NameServerApplication {
    public static void main(String[] args) throws InterruptedException {
        NameServerStarter nameServerStarter = new NameServerStarter(8020);
        nameServerStarter.start();
    }
}
