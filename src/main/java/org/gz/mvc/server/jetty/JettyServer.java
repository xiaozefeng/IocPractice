package org.gz.mvc.server.jetty;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.gz.Beans;
import org.gz.mvc.MethodDetail;
import org.gz.mvc.UrlMappings;
import org.gz.mvc.annotations.Param;
import org.gz.mvc.enums.RequestMethod;
import org.gz.utils.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


/**
 * @author xiaozefeng
 */
@Slf4j
public class JettyServer {


    public static void start(int port) {
        Server server = new Server(port);
        server.setHandler(new AbstractHandler() {
            @Override
            public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse response) throws IOException, ServletException {
                response.setContentType("text/html;charset=utf-8");
                try {
                    doResponse(httpServletRequest, response);
                } catch (Exception e) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    response.setContentType("text/plain;charset=utf-8");
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().print(sw.toString());
                }
                request.setHandled(true);
            }
        });

        try {
            server.start();
            log.info("JettyServer is running on http://127.0.0.1:{}", port);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 作出响应
     *
     * @param request
     * @param response
     */
    private static void doResponse(HttpServletRequest request, HttpServletResponse response) throws IOException, InvocationTargetException, IllegalAccessException {
        String requestURL = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.getEnum(request.getMethod());
        log.info("{} {}", requestURL, requestMethod);
        MethodDetail methodDetail = UrlMappings.getInstance().getMap(requestURL, requestMethod);
        if (methodDetail == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().print(Constants.NOT_FOUND);
            return;
        }

        Class clazz = methodDetail.getClazz();
        Object object = Beans.getInstance().getObject(clazz);
        if (object == null) {
            throw new RuntimeException("can't found bean for " + clazz);
        }

        Map<String, String> requestParam = new HashMap<>();
        request.getParameterMap().forEach((k, v) -> {
            requestParam.put(k, v[0]);
        });
        List<String> params = new ArrayList<>();
        Method method = methodDetail.getMethod();

        // 获取方法上所有参数
        Parameter[] parameters = method.getParameters();
        Arrays.stream(parameters)
                .forEach(p -> {
                    AtomicReference<String> name = new AtomicReference<>("");
                    // 获取参数上所有的注解
                    Arrays.stream(p.getAnnotations())
                            .filter(a -> a.annotationType() == Param.class)
                            .forEach(a -> {
                                name.set(((Param) a).value());
                            });
                    // 如果请求参数中存在这个参数就把该值赋给方法参数，否则赋值null
                    params.add(requestParam.getOrDefault(name.get(), null));
                });

        // 调用方法
        Object result = method.invoke(object, params.toArray());
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print(result);
    }

}
