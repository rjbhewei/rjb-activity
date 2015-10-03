package com.hewei.connector;

import com.hewei.enums.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/25  13:54
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class LogInit {

    private static final Logger logger = LoggerFactory.getLogger(LogInit.class);

    private static final String fileProPath = "/logPro.properties";

    private static final String fileDevPath = "/logDev.properties";

    private static Properties properties = new Properties();

    static {
        String hostName = "";
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        Server server = Server.checkServer(hostName);
        init(server);
    }

    public static void init(Server server) {
        try (InputStream in = LogInit.class.getResourceAsStream(server == Server.produce ? fileProPath : fileDevPath)) {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("LogInit properties:" + properties);
    }

    public static String getString(String key) {
        return properties.getProperty(key);
    }

    public static int getInt(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

}