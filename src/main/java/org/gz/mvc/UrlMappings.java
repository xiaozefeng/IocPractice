package org.gz.mvc;

import org.gz.mvc.enums.RequestMethod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaozefeng
 */
public class UrlMappings {
    private static UrlMappings urlMappings = new UrlMappings();

    private UrlMappings() {
    }

    public static UrlMappings getInstance() {
        return urlMappings;
    }

    List<MethodDetail> methodDetails = new ArrayList<>();

    public MethodDetail getMap(String url, RequestMethod requestMethod) {
        return this.methodDetails.stream()
                .filter(md -> md.getUrl().equals(url) && md.getRequestMethod() == requestMethod)
                .findFirst()
                .orElse(null);
    }

    public void setMap(String url, Class clazz, Method method, RequestMethod requestMethod) {
        MethodDetail methodDetail = MethodDetail.builder()
                .url(url)
                .clazz(clazz)
                .method(method)
                .requestMethod(requestMethod)
                .build();

        this.methodDetails.add(methodDetail);
    }

    public void setMap(String url, MethodDetail methodDetail) {
        this.methodDetails.add(methodDetail);
    }

}
