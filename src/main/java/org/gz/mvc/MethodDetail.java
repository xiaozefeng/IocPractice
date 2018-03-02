package org.gz.mvc;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.gz.mvc.enums.RequestMethod;

import java.lang.reflect.Method;

/**
 * @author xiaozefeng
 */
@Getter
@Setter
@Builder
public class MethodDetail {

    private String url;

    private RequestMethod requestMethod;

    private Class clazz;

    private Method method;
}
