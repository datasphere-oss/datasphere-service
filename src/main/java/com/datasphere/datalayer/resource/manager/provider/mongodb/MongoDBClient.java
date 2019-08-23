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

package com.datasphere.datalayer.resource.manager.provider.mongodb;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.MongoClient;

public class MongoDBClient {

    private final String HOST;
    private final int PORT;
    private final String USERNAME;
    private final String PASSWORD;
    private final boolean SSL;

    // mongodb admin database for users
    private final static String DB = "admin";

    public MongoDBClient(String host, int port, boolean ssl, String username, String password) {
        super();
        HOST = host;
        PORT = port;
        USERNAME = username;
        PASSWORD = password;
        SSL = ssl;
    }

    private MongoClient connect() {
//        // create credentials - expect access to "admin" db
//        MongoCredential credential = MongoCredential.createCredential(USERNAME, DB, PASSWORD.toCharArray());

        // build ConnectionString
        String url = "mongodb://" + USERNAME + ":" + PASSWORD + "@" + HOST + ":" + String.valueOf(PORT)
                + "/?authSource=" + DB;

        if (SSL) {
            url += "&ssl=true";
        }

        MongoClient client = MongoClients.create(url);

        return client;
    }

    public boolean hasDatabase(String name) {
        MongoClient client = connect();
        boolean exists = false;


        List<String> databases = (List<String>) client.listDatabaseNames().into(new ArrayList<String>());

        for (String db : databases) {
            if (db.equals(name)) {
                exists = true;
                break;
            }
        }

        client.close();

        return exists;
    }

    public void createDatabase(String name) {
        // create a collection "rm" to init db
        // TODO set as config or rework
        MongoClient client = connect();
        MongoDatabase db = client.getDatabase(name);
        MongoCollection<Document> collection = db.getCollection("rm");
        Document doc = new Document("owner", "rm")
                .append("type", "database")
                .append("name", name);
        collection.insertOne(doc);

        client.close();

    }

    public void deleteDatabase(String name) {
        MongoClient client = connect();
        MongoDatabase db = client.getDatabase(name);

        // call drop
        // TODO drop users before?
        db.drop();

        client.close();

    }

    public void createUser(String database, String username, String password, String role) {
        MongoClient client = connect();
        MongoDatabase db = client.getDatabase(database);

        // build custom command
        BasicDBObject command = new BasicDBObject("createUser", username)
                .append("pwd", password)
                .append("roles", Collections.singletonList(
                        new BasicDBObject("role", role).append("db", database)));

        db.runCommand(command);

        client.close();

    }

    public void deleteUser(String database, String username) {
        MongoClient client = connect();
        MongoDatabase db = client.getDatabase(database);

        // build custom command
        BasicDBObject command = new BasicDBObject("dropUser", username);
        db.runCommand(command);

        client.close();

    }

    public static final String ROLE_OWNER = "dbOwner"; // admin+userAdmin+readWrite
    public static final String ROLE_ADMIN = "dbAdmin"; // admin has stats, info etc
    public static final String ROLE_RW = "readWrite"; // rw access to collections
    public static final String ROLE_READ = "read"; // read only

}
