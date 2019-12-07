package io.snackbase.common;

import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * read global configuration[resources/meteroute.properties]
 *
 * @author maxuefeng
 * @since 2019/12/6
 */
public class Configuration {

    private static Logger logger = LoggerFactory.getLogger(Configuration.class);

    private static ImmutableMap config = null;

    /*
     * load configuration
     */
    static {
        try {

            InputStream is = Configuration.class.getClassLoader().getResourceAsStream("meteroute.properties");
            Properties prop = new Properties();
            prop.load(is);

            HashMap<String, Object> tempConfig = new HashMap<>();
            prop.forEach((key, value) -> tempConfig.put(key.toString(), value));
            config = ImmutableMap.copyOf(tempConfig);

        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    public static Object get(String key) {
        return config.get(key);
    }
}
