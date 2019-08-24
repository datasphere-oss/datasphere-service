package com.datasphere.core.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

public class PropertyUtil {
    private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);
    private static Properties props;
    static {
        loadProps();
    }

    synchronized static private void loadProps(){
        logger.info("start loading properties.......");
        props = new Properties();
        String propertiesPath = System.getProperty("user.dir")+ File.separator+"dwresources"+File.separator+ "application.properties";
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(propertiesPath));
            props.load(in);
        } catch (FileNotFoundException e) {
            logger.error("application.properties is not found!");
        } catch (IOException e) {
            logger.error("an IOException is encountered");
        } finally {
            try {
                if(null != in) in.close();
            } catch (IOException e) {
                logger.error("the inputstream of application.properties file is closed.");
            }
        }
        logger.info("loading properties is already completed.");
    }

    public static String getProperty(String key){
        if(null == props) {
            loadProps();
        }
        return props.getProperty(key);
    }
}
