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

package com.datasphere.datalayer.resource.manager.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

public class SqlUtil {

    public static String encodeURI(
            String provider, String endpoint, String database,
            String username, String password) {

        // pack connection details into URL
        // mimic RFC 1738
        // <provider>://<user>:<password>@<host>:<port>/<resource>
        StringBuilder sb = new StringBuilder();
        sb.append(provider).append("://");
        try {
            sb.append(URLEncoder.encode(username, "UTF-8"));
            sb.append(":");
            sb.append(URLEncoder.encode(password, "UTF-8"));
            sb.append("@");
            // endpoint should be host:port from caller
            sb.append(endpoint);
            sb.append("/");
            sb.append(URLEncoder.encode(database, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
        }

        return sb.toString();
    }

    public static String encodeURI(
            String provider, String endpoint, String database) {

        // pack connection details into URL
        // mimic RFC 1738
        // <provider>://<host>:<port>/<resource>
        StringBuilder sb = new StringBuilder();
        sb.append(provider).append("://");
        try {
            // endpoint should be host:port from caller
            sb.append(endpoint);
            sb.append("/");
            sb.append(URLEncoder.encode(database, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
        }

        return sb.toString();
    }

    public static String getUsername(String uri) {
        try {
            URI u = new URI(uri);
            String userInfo = u.getUserInfo();
            if (userInfo == null) {
                return "";
            }

            if (userInfo.contains(":")) {
                return userInfo.split(":")[0];
            } else {
                return userInfo;
            }

        } catch (Exception e) {
            return "";
        }
    }

    public static String getPassword(String uri) {
        try {
            URI u = new URI(uri);
            String userInfo = u.getUserInfo();
            if (userInfo == null) {
                return "";
            }

            if (userInfo.contains(":")) {
                return userInfo.split(":")[1];
            } else {
                return "";
            }

        } catch (Exception e) {
            return "";
        }
    }

    public static String getProvider(String uri) {
        try {
            URI u = new URI(uri);
            return u.getScheme();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getEndpoint(String uri) {
        try {
            URI u = new URI(uri);
            return u.getAuthority();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getHost(String uri) {
        try {
            URI u = new URI(uri);
            return u.getHost();
        } catch (Exception e) {
            return "";
        }
    }

    public static int getPort(String uri) {
        try {
            URI u = new URI(uri);
            return u.getPort();
        } catch (Exception e) {
            return -1;
        }
    }

    public static String getDatabase(String uri) {
        try {
            URI u = new URI(uri);
            String path = u.getPath();
            if (path.startsWith("/")) {
                return path.substring(1);
            } else {
                return path;
            }
        } catch (Exception e) {
            return "";
        }
    }

}
