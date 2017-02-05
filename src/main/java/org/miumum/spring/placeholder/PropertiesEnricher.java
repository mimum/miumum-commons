package org.miumum.spring.placeholder;

import java.util.Properties;

/**
 * The enricher is used to insert some more properties to the proprety placeholder configurer
 *
 * @see EnrichingPropertyPlaceholderConfigurer
 *
 * @author Roman Valina
 */
public interface PropertiesEnricher {

    /**
     * Add some new or modify current properties to the current properties in {@link EnrichingPropertyPlaceholderConfigurer}
     *
     * @param properties current loaded properties
     */
    public void enrichProperties(Properties properties);

}
