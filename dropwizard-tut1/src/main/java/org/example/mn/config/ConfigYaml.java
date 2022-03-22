package org.example.mn.config;

import org.example.mn.AppConstants;

import java.util.HashMap;
import java.util.Map;

public class ConfigYaml {
    private static final Map<String, String> configYml = new HashMap<>();
    static {
        configYml.put("dev", "helloworld_dev.yaml");
        configYml.put("test", "helloworld_test.yml");
        configYml.put("default", "helloworld.yml");
    }

    public static String getconfigYml(){
        String appEnv = System.getenv(AppConstants.APP_ENV) != null ? System.getenv(AppConstants.APP_ENV) : "default";
        return configYml.get(appEnv);
    }
}
