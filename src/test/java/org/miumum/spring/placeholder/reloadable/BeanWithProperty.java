package org.miumum.spring.placeholder.reloadable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Roman Valina
 */
@Component
public class BeanWithProperty {

    //@Value("${aaa.bbb}")
    private String resolvedValue;

    @Value("${aaa.bbb}")
    public void setResolvedValue(String resolvedValue) {
        this.resolvedValue = resolvedValue;
    }
}
