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

package com.datasphere.engine.manager.resource.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class OdbcUtil {

    public static String encodeURI(
            String provider, String driver,
            String host, int port,
            String username, String password,
            String database, String schema, String table,
            String properties) throws UnsupportedEncodingException {

        // validate inputs
        validate(provider);
        validate(driver);
        validate(username);
        validate(password);
        validate(database);
        validate(table);

        // pack connection details into String
        // <provider>::DRIVER=<driver>;...
        StringBuilder sb = new StringBuilder();
        sb.append(provider).append("::");

        sb.append("DRIVER={").append(driver).append("};");
        sb.append("HOST=").append(host).append(";");
        sb.append("PORT=").append(Integer.toString(port)).append(";");
        // also build single server string
        sb.append("SERVER=").append(host).append(",").append(Integer.toString(port)).append(";");

        sb.append("USER={").append(URLEncoder.encode(username, "UTF-8")).append("};");
        sb.append("PASS={").append(URLEncoder.encode(password, "UTF-8")).append("};");

        sb.append("DATABASE={").append(database).append("};");
        sb.append("SCHEMA={").append(URLEncoder.encode(schema, "UTF-8")).append("};");
        sb.append("TABLE={").append(URLEncoder.encode(table, "UTF-8")).append("};");

        sb.append(properties);

        return sb.toString();
    }

    public static String getDriver(String uri) {
        Map<String, String> values = getValues(uri);
        if (values.containsKey("DRIVER")) {
            return values.get("DRIVER").replaceAll("[{}]+", "");
        } else {
            return "";
        }
    }

    public static String getHost(String uri) {
        Map<String, String> values = getValues(uri);
        if (values.containsKey("HOST")) {
            return values.get("HOST");
        } else {
            return "";
        }
    }

    public static int getPort(String uri) {
        Map<String, String> values = getValues(uri);
        if (values.containsKey("PORT")) {
            return Integer.parseInt(values.get("PORT"));
        } else {
            return -1;
        }
    }

    public static String getUsername(String uri) {
        Map<String, String> values = getValues(uri);
        if (values.containsKey("USER")) {
            try {
                return URLDecoder.decode(values.get("USER"), "UTF-8").replaceAll("[{}]+", "");
            } catch (UnsupportedEncodingException e) {
                return "";
            }
        } else {
            return "";
        }
    }

    public static String getPassword(String uri) {
        Map<String, String> values = getValues(uri);
        if (values.containsKey("PASS")) {
            try {
                return URLDecoder.decode(values.get("PASS"), "UTF-8").replaceAll("[{}]+", "");
            } catch (UnsupportedEncodingException e) {
                return "";
            }
        } else {
            return "";
        }
    }

    public static String getDatabase(String uri) {
        Map<String, String> values = getValues(uri);
        if (values.containsKey("DATABASE")) {
            return values.get("DATABASE").replaceAll("[{}]+", "");
        } else {
            return "";
        }
    }

    public static String getSchema(String uri) {
        Map<String, String> values = getValues(uri);
        if (values.containsKey("SCHEMA")) {
            try {
                return URLDecoder.decode(values.get("SCHEMA"), "UTF-8").replaceAll("[{}]+", "");
            } catch (UnsupportedEncodingException e) {
                return "";
            }
        } else {
            return "";
        }
    }

    public static String getTable(String uri) {
        Map<String, String> values = getValues(uri);
        if (values.containsKey("TABLE")) {
            try {
                return URLDecoder.decode(values.get("TABLE"), "UTF-8").replaceAll("[{}]+", "");
            } catch (UnsupportedEncodingException e) {
                return "";
            }
        } else {
            return "";
        }
    }

    public static String getProvider(String uri) {
        return uri.substring(0, uri.indexOf("::"));
    }

    /*
     * Helpers
     */
    public static final String VALID_CHARS = "[a-zA-Z0-9 -_@\\[\\]\\.]+";

    public static Map<String, String> getValues(String uri) {
        Map<String, String> values = new HashMap<>();

        String data = uri.substring(uri.indexOf("::") + 2);
        String[] arr = data.split(";");
        for (String s : arr) {
            try {
                String[] kv = s.split("=");
                if (kv.length == 2) {
                    values.put(kv[0], kv[1]);
                }
            } catch (Exception e) {
                // skip
            }
        }

        return values;
    }

    private static void validate(String input) throws UnsupportedEncodingException {
        if (!input.matches(VALID_CHARS)) {
            throw new UnsupportedEncodingException();
        }
    }
}
