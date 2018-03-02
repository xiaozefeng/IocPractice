package org.gz.ioc;

import java.lang.annotation.*;

/**
 * @author xiaozefeng
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Resource {
}
