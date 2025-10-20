package com.infoepoch.cmgs.core;

import com.infoepoch.cmgs.constants.BrokerConstants;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

public class PropertiesLoader {
    private Properties properties;

    public void loadProperties() throws IOException {
        String eagleHome = System.getenv(BrokerConstants.EAGLE_MQ_HOME);
        properties.load(Files.newInputStream(new File(eagleHome + "/config/nameserver.properties").toPath()));
    }

    public String getProperties(String key) {
        return properties.getProperty(key);
    }
}
