package org.gz;

import lombok.extern.slf4j.Slf4j;
import org.gz.ioc.Bean;
import org.gz.ioc.Component;
import org.gz.ioc.Configuration;
import org.gz.ioc.Resource;
import org.gz.utils.PackageUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * @author xiaozefeng
 */
@Slf4j
public class ApplicationContext {


    public static void init() {
        // 获取类路径下的所用classes
        List<Class> classes = PackageUtils.getAllClass();

        // 扫描@Component 和 @Configuration 注解并加入容器
        assert classes != null;
        classes.stream()
                .filter(c -> c.isAnnotationPresent(Component.class) || c.isAnnotationPresent(Configuration.class))
                .forEach(c -> {
                    try {
                        log.info("Created bean for [{}]", c);
                        Beans.getInstance().setObject(c, c.newInstance());

                    } catch (InstantiationException | IllegalAccessException e) {
                        log.error(e.getMessage(), e);
                    }
                });

        // 扫描@Configuration 找到里面的@Bean 并加入IOC 容器
        classes.stream()
                .filter(c -> c.isAnnotationPresent(Configuration.class))
                .forEach(c -> {
                    Method[] methods = c.getMethods();
                    Arrays.stream(methods)
                            .filter(m -> m.isAnnotationPresent(Bean.class))
                            .forEach(m -> {
                                Object classObject = Beans.getInstance().getObject(c);
                                try {
                                    Object o = m.invoke(classObject);
                                    log.info("Created bean for [{}] by method [{}]", o.getClass(), m);
                                    Beans.getInstance().setObject(o.getClass(), o);
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    log.error(e.getMessage(), e);
                                }

                            });
                });

        // 扫描 @Resource 并注入到field中
        classes.stream()
                .forEach(c -> {
                    Field[] declaredFields = c.getDeclaredFields();
                    Arrays.stream(declaredFields)
                            .filter(f -> f.isAnnotationPresent(Resource.class))
                            .forEach(f -> {
                                Object classObject = Beans.getInstance().getObject(c);
                                Object fieldObject = Beans.getInstance().getObject(f.getType());
                                f.setAccessible(true);
                                try {
                                    f.set(classObject, fieldObject);
                                    log.info("Inject bean [{}] type [{}] into [{}]", fieldObject, f.getType(), c);
                                } catch (IllegalAccessException e) {
                                    log.error(e.getMessage(), e);
                                }

                            });
                });


    }

    public static Object getBean(Class clazz){
        return Beans.getInstance().getObject(clazz);
    }
}
