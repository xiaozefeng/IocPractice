package org.gz;

import org.gz.ioc.Component;
import org.gz.mvc.annotations.Param;
import org.gz.mvc.annotations.RequestMapping;

/**
 * @author xiaozefeng
 */
@Component
@RequestMapping("/person")
public class Hello {

    @RequestMapping("/info")
    public String hello(@Param("name") String name, @Param("age") String age) {
        return "hello " + name + ", your age is " + Integer.valueOf(age);
    }

}
