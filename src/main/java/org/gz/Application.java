package org.gz;

import org.gz.mvc.RequestScanner;
import org.gz.mvc.server.jetty.JettyServer;
import org.gz.utils.ConfigUtils;

import java.util.Map;

/**
 * @author xiaozefeng
 */
public class Application {
    public static void run() {
        // 依赖注入
        ApplicationContext.init();

        // 初始化Http请求映射
        RequestScanner.initMapping();

        Map<String, Object> config = ConfigUtils.getConfig();
        String server = (String) config.get("server");
        Integer port = (Integer) config.get("port");
        if (server.equals("jetty")) {
            JettyServer.start(port);
        }
    }

    public static void main(String[] args) {
        Application.run();
    }
}
