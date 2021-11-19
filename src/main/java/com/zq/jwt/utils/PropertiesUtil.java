package com.zq.jwt.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author laozhou
 * 最新使用：PropertySourcesPlaceholderConfigurer
 */
//@Component
public class PropertiesUtil extends PropertyPlaceholderConfigurer implements Map<String, String> {

    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    /**
     * properties键值对容器
     */
    private static Map<String, String> ctxPropertiesMap;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess,
                                     Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
        if (ctxPropertiesMap != null) {
            logger.warn("The property map will be override!");
        }
        ctxPropertiesMap = new HashMap<>();
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            String value = props.getProperty(keyStr);
            ctxPropertiesMap.put(keyStr, value);
        }
    }

    @Override
    public int size() {
        return ctxPropertiesMap.size();
    }

    @Override
    public boolean isEmpty() {
        return ctxPropertiesMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return ctxPropertiesMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return ctxPropertiesMap.containsValue(value);
    }

    @Override
    public String get(Object key) {
        return ctxPropertiesMap.get(key);
    }

    @Override
    public String put(String key, String value) {
        return ctxPropertiesMap.put(key, value);
    }

    @Override
    public String remove(Object key) {
        return ctxPropertiesMap.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> m) {
        if (null != m) {
            m.forEach((k, v) -> ctxPropertiesMap.put(k, v));
        }
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<String> values() {
        return ctxPropertiesMap.values();
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}