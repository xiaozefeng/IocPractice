package org.gz.mvc.enums;

import java.util.Arrays;

/**
 * @author xiaozefeng
 */

public enum RequestMethod {
    GET("GET"),

    POST("POST"),

    DELETE("DELETE"),

    PUT("PUT"),

    HEAD("HEAD");

    private String value;

    RequestMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static RequestMethod getEnum(String value) {
        return Arrays.stream(RequestMethod.values())
                .filter(r -> r.value.equals(value.toUpperCase()))
                .findFirst()
                // 默认 GET
                .orElse(GET);
    }

}
