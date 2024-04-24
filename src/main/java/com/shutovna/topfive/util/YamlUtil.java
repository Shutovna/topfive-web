package com.shutovna.topfive.util;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.PathResource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Properties;

public class YamlUtil {
    public static final String YAML_FILE = "application.yaml";
    private static Properties applicationProperties;

    public static String getPropertyValue(String property) {
        return  getProperties().getProperty(property);
    }

    public static Properties getProperties() {
        try {
            if (applicationProperties == null) {
                File file = ResourceUtils.getFile("classpath:" + YAML_FILE);
                YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
                factory.setResources(new PathResource(file.toPath()));
                factory.afterPropertiesSet();
                applicationProperties = factory.getObject();
            }

            return applicationProperties;
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Cannot load yaml file " + YAML_FILE, e);
        }
    }
}
