package org.gz.utils;

import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaozefeng
 */
@Slf4j
public class ConfigUtils {

    private static String root = PackageUtils.class.getClassLoader().getResource("").getPath();

    private static String separator = File.separator;

    public static Map<String, Object> getConfig() {
        String fileName = root + separator + "application.yaml";
        File file = new File(fileName);
        if (!file.exists()) {
            Map<String, Object> result = new HashMap<>();
            result.put("server", "jetty");
            result.put("port", 8080);
            return result;
        }

        try {
            InputStream in = new FileInputStream(file);
            Yaml yaml = new Yaml();
            Map<String,Object> map = (Map<String, Object>) yaml.load(in);
            if (map == null)
                return new HashMap<>();
            if (!map.containsKey("server")) {
                map.put("server", "jetty");
            }
            if (!map.containsKey("port")) {
                map.put("port", 8080);
            }
            return map;
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
        }
        return new HashMap<>();
    }
}
