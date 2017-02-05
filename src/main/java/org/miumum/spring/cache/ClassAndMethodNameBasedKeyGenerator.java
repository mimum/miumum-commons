package org.miumum.spring.cache;

import org.springframework.cache.interceptor.SimpleKeyGenerator;

import java.lang.reflect.Method;

/**
 * This custom implementation generates key of cached entity not only by parameters of methods,
 * but also by name of target class and name of invoked method.
 *
 * @see org.springframework.cache.interceptor.SimpleKeyGenerator
 *
 * @author Roman Valina
 */
public class ClassAndMethodNameBasedKeyGenerator extends SimpleKeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        return target.getClass().getName()+"#"+ method.getName()+":" + super.generate(target, method, params).hashCode();
    }

}
