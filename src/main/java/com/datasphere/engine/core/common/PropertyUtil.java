/*
 * Copyright 2019, Huahuidata, Inc.
 * DataSphere is licensed under the Mulan PSL v1.
 * You can use this software according to the terms and conditions of the Mulan PSL v1.
 * You may obtain a copy of Mulan PSL v1 at:
 * http://license.coscl.org.cn/MulanPSL
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v1 for more details.
 */

package com.datasphere.engine.core.common;

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
