package org.gz;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaozefeng
 */
public class Beans {

    private static final Beans BEANS = new Beans();

    public static Beans getInstance() {
        return BEANS;
    }

    private Map<Class, Object> containers = new HashMap<>();

    public Object getObject(Class clazz) {
        return containers.get(clazz);
    }

    public void setObject(Class clazz, Object object) {
        containers.put(clazz, object);
    }
}
