package org.gz.mvc;

import lombok.extern.slf4j.Slf4j;
import org.gz.mvc.annotations.RequestMapping;
import org.gz.mvc.enums.RequestMethod;
import org.gz.utils.PackageUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author xiaozefeng
 */
@Slf4j
public class RequestScanner {

    public static void initMapping() {
        List<Class> classes = PackageUtils.getAllClass();
        AtomicReference<String> classUrl = new AtomicReference<>("");
        classes.forEach(clazz -> {
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
                classUrl.set(requestMapping.value());
            }

            Method[] methods = clazz.getMethods();
            Arrays.stream(methods)
                    .filter(m -> m.isAnnotationPresent(RequestMapping.class))
                    .forEach(m->{
                        RequestMapping requestMapping= m.getAnnotation(RequestMapping.class);
                        String methodUrl = requestMapping.value();
                        RequestMethod method = requestMapping.method();
                        boolean isNull = classUrl.get() == null || "".equals(classUrl.get());
                        methodUrl = isNull ? methodUrl : classUrl.get() + methodUrl;
                        log.info("Mapped URL [{} {}] onto handler of type [{}]", method.getValue(), methodUrl, method);
                        UrlMappings.getInstance().setMap(methodUrl, clazz, m, method);
                    });
        });


    }
}
