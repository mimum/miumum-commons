package org.miumum.spring.placeholder.reloadable

import org.junit.Assert
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests

/**
 *
 *
 * @author Roman Valina
 */
@ContextConfiguration(locations = [
        "classpath:org/miumum/spring/placeholder/reloadable/test-applicationContext.xml"
])
class ReloadablePropertyPlaceholderConfigurerTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private BeanWithProperty bean;

    @Test
    public void xxx() {
        Assert.assertEquals("123", bean.resolvedValue)
    }
}
