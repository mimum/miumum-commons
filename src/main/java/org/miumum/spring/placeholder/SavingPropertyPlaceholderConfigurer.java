package org.miumum.spring.placeholder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * This custom implementation saves resolved properties in to map
 *
 * @author Roman Valina
 */
public class SavingPropertyPlaceholderConfigurer extends EnrichingPropertyPlaceholderConfigurer {

    protected Map<String, String> propertiesMap;
    // Default as in PropertyPlaceholderConfigurer
    protected int springSystemPropertiesMode = SYSTEM_PROPERTIES_MODE_FALLBACK;

    @Override
    public void setSystemPropertiesMode(int systemPropertiesMode) {
        super.setSystemPropertiesMode(systemPropertiesMode);
        springSystemPropertiesMode = systemPropertiesMode;
    }

    @Override
    protected void loadProperties(Properties props) throws IOException {
        super.loadProperties( props);
        saveResolvedProperties(props);
    }

    protected void saveResolvedProperties(Properties props) {
        propertiesMap = new HashMap<String, String>();
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            String valueStr = resolvePlaceholder(keyStr, props, springSystemPropertiesMode);
            propertiesMap.put(keyStr, valueStr);
        }
        // save the system properties as well
        Properties properties = System.getProperties();
        for (Object key : properties.keySet()) {
            String keyStr = key.toString();
            String valueStr = resolvePlaceholder(keyStr, props, springSystemPropertiesMode);
            propertiesMap.put(keyStr, valueStr);
        }
    }

    public String getProperty(String name) {
        return propertiesMap.get(name);
    }

}
