package org.miumum.spring.placeholder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * This property place holder configurer uses {@link PropertiesEnricher} to enrich the loaded properties of some new properties
 *
 * @author Roman Valina
 */
public class EnrichingPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private List<PropertiesEnricher> propertiesEnrichers;

    @Override
    protected void loadProperties(Properties props) throws IOException {
        super.loadProperties(props);
        if (propertiesEnrichers != null) {
            for (PropertiesEnricher propertiesEnricher : propertiesEnrichers) {
                logger.info("Enriching property using enricher '{}'", propertiesEnricher.getClass().getSimpleName());
                propertiesEnricher.enrichProperties(props);
            }
        }
    }

    public List<PropertiesEnricher> getPropertiesEnrichers() {
        return propertiesEnrichers;
    }

    public void setPropertiesEnrichers(List<PropertiesEnricher> propertiesEnrichers) {
        this.propertiesEnrichers = propertiesEnrichers;
    }

}
