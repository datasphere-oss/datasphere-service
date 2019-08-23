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

package com.datasphere.datalayer.resource.manager.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.datasphere.datalayer.resource.manager.SystemKeys;
import com.datasphere.datalayer.resource.manager.common.ConsumerException;
import com.datasphere.datalayer.resource.manager.common.NoSuchConsumerException;
import com.datasphere.datalayer.resource.manager.common.NoSuchProviderException;
import com.datasphere.datalayer.resource.manager.common.NoSuchRegistrationException;
import com.datasphere.datalayer.resource.manager.common.NoSuchResourceException;
import com.datasphere.datalayer.resource.manager.common.ResourceProviderException;
import com.datasphere.datalayer.resource.manager.config.ConsumerConfiguration;
import com.datasphere.datalayer.resource.manager.model.Consumer;
import com.datasphere.datalayer.resource.manager.model.ConsumerBuilder;
import com.datasphere.datalayer.resource.manager.model.Registration;
import com.datasphere.datalayer.resource.manager.model.Resource;
import com.datasphere.datalayer.resource.manager.model.ResourceEvent;
import com.datasphere.datalayer.resource.manager.model.ResourceProvider;

@Component
public class ConsumerLocalService {
    private final static Logger _log = LoggerFactory.getLogger(ConsumerLocalService.class);

    private Map<String, List<Consumer>> _consumers;

    @Autowired
    private ConsumerConfiguration staticConfig;

    @Autowired
    private ResourceLocalService resourceLocalService;

    @Autowired
    private RegistrationLocalService registrationService;

    /*
     * Init
     */
    @PostConstruct
    private void init() {
        // init consumer in-memory map
        // TODO replace with better repo
        _consumers = new HashMap<>();
        // init types - TODO add configuration
        _consumers.put(SystemKeys.TYPE_SQL, new ArrayList<>());
        _consumers.put(SystemKeys.TYPE_NOSQL, new ArrayList<>());
        _consumers.put(SystemKeys.TYPE_FILE, new ArrayList<>());
        _consumers.put(SystemKeys.TYPE_OBJECT, new ArrayList<>());
        _consumers.put(SystemKeys.TYPE_ODBC, new ArrayList<>());

        // scan static consumers from config
        // TODO refactor
        for (String type : _consumers.keySet()) {

            for (String id : staticConfig.get(type)) {
                // create via builder
                try {
                    Consumer c = buildConsumer(id);
                    // add to map
                    _consumers.get(type).add(c);
                } catch (NoSuchConsumerException e) {
                    _log.error("no builder for " + id);
                } catch (ConsumerException e) {
                    _log.error("builder consumer error for " + id + " " + e.getMessage());
                }
            }
        }

        // read consumers from DB
        List<Registration> registrations = registrationService.list();
        for (Registration reg : registrations) {
            // build consumer with properties
            try {
                Consumer c = buildConsumer(reg);
                // add to map
                _consumers.get(reg.getType()).add(c);
            } catch (NoSuchConsumerException e) {
                _log.error("no builder for registration " + reg.getId());
            } catch (ConsumerException e) {
                _log.error("builder consumer error for registration " + reg.getId() + " " + e.getMessage());
            }
        }

        // check all resources
        checkResources();
    }

    public void checkResources() {
        // fetch all resources and require check for both producer and consumers
        List<Resource> resources = resourceLocalService.list();
        for (Resource r : resources) {
            try {
                resourceLocalService.check(r.getId());
            } catch (NoSuchResourceException | NoSuchProviderException e) {
                _log.error("error checking resource " + r.getId());
            } catch (ResourceProviderException e) {
                _log.error("provider error checking resource " + r.getId());
            }
        }
    }

    /*
     * Builders
     */

    @Autowired
    private Map<String, ConsumerBuilder> _builders;

    private Consumer buildConsumer(String id) throws NoSuchConsumerException, ConsumerException {
        // build without parameters
        return getBuilder(id).build();
    }

    private Consumer buildConsumer(Registration reg) throws NoSuchConsumerException, ConsumerException {

        // lookup builder
        String id = reg.getConsumer();
        // build based on registration
        return getBuilder(id).build(reg);
    }

    private Consumer buildConsumer(String id, Map<String, Serializable> properties)
            throws NoSuchConsumerException, ConsumerException {

        // build based on properties
        return getBuilder(id).build(properties);
    }

    public ConsumerBuilder getBuilder(String id) throws NoSuchConsumerException {

        // check if id ends with "Consumer"
        // spring registers beans with "className" as key
        // code expects consumer classes to end with *Consumer.java
        // builders should be named as [consumerId]Builder.java
        if (!id.endsWith("Consumer")) {
            id = id.concat("Consumer");
        }
        String builderClass = id.replace("Consumer", "Builder");

        _log.debug("get builder for " + builderClass);

//        if (!_builders.containsKey(builderClass)) {
//            throw new NoSuchConsumerException();
//        }
//
//        return _builders.get(builderClass);

        ConsumerBuilder builder = null;
        if (_builders.containsKey(builderClass)) {
            builder = _builders.get(builderClass);
        } else {
            // iterate to match
            for (String b : _builders.keySet()) {
                if (b.compareToIgnoreCase(builderClass) == 0) {
                    builder = _builders.get(b);
                    break;
                }
            }
        }

        if (builder == null) {
            _log.error("no builder for " + builderClass);
            _log.debug("available builders: " + _builders.keySet().toString());
            throw new NoSuchConsumerException();
        }

        return builder;
    }

    public boolean hasBuilder(String id) {
        // check if id ends with "Consumer"
        // spring registers beans with "className" as key
        // code expects consumer classes to end with *Consumer.java
        // builders should be named as [consumerId]Builder.java
//        if (!id.endsWith("Consumer")) {
//            id = id.concat("Consumer");
//        }
//        String builderClass = id.replace("Consumer", "Builder");
//
//        return _builders.containsKey(builderClass);

        try {
            ConsumerBuilder b = getBuilder(id);
            return true;
        } catch (NoSuchConsumerException e) {
            return false;
        }

    }

    public Map<String, List<ConsumerBuilder>> listBuilders() {
        Map<String, List<ConsumerBuilder>> map = new HashMap<>();
        // static init for all types
        map.put(SystemKeys.TYPE_SQL, new ArrayList<>());
        map.put(SystemKeys.TYPE_NOSQL, new ArrayList<>());
        map.put(SystemKeys.TYPE_FILE, new ArrayList<>());
        map.put(SystemKeys.TYPE_OBJECT, new ArrayList<>());
        map.put(SystemKeys.TYPE_ODBC, new ArrayList<>());

        for (ConsumerBuilder b : _builders.values()) {
            if (b.isAvailable()) {
                map.get(b.getType()).add(b);
            }
        }

        return map;
    }

    public List<ConsumerBuilder> listBuilders(String type) {
        return _builders.entrySet().stream()
                .map(entry -> entry.getValue())
                .filter(entry -> (entry.isAvailable() && entry.getType().equals(type)))
                .collect(Collectors.toList());
    }

    public List<String> listTypes() {
        // return only non empty types
        Set<String> types = new HashSet<>();
        for (ConsumerBuilder b : _builders.values()) {
            if (b.isAvailable()) {
                types.add(b.getType());
            }
        }
        return new ArrayList<>(types);
    }

    /*
     * Data
     */

    public Registration add(String scopeId, String userId, String type, String consumer,
            Map<String, Serializable> properties, List<String> tags)
            throws NoSuchConsumerException, ConsumerException {

        // check support
        if (!hasBuilder(consumer)) {
            throw new NoSuchConsumerException();
        }

        // build registration
        Registration reg = registrationService.add(scopeId, userId, type, consumer, properties, tags);

        // build consumer
        Consumer c = buildConsumer(reg);
        _consumers.get(type).add(c);

        // trigger create on new consumer for all existing resources
        List<Resource> resources = resourceLocalService.listByTypeAndScopeId(type, scopeId);

        for (Resource res : resources) {
            try {
                // fetch full resource definition (ie unencrypted)
                Resource r = resourceLocalService.get(res.getId());
//                c.addResource(scopeId, userId, r);
                // use UPDATE to create IF MISSING
                c.updateResource(scopeId, userId, r);
            } catch (Exception ex) {
                _log.error("error registering resource " + res.getId() + ": " + ex.getMessage());
            }
        }

        return reg;
    }

    public Registration update(long id, Map<String, Serializable> properties, List<String> tags)
            throws NoSuchConsumerException, ConsumerException {
        try {
            // fetch registration
            Registration reg = registrationService.get(id);
            String type = reg.getType();

            // lookup for consumer
            Consumer consumer = consumerByRegistrationId(type, reg.getId());

            if (consumer != null) {
                // delete consumer - no cleanup or shutdown
                _consumers.get(type).remove(consumer);
            }

            // update registration
            reg = registrationService.update(id, properties, tags);

            // re-create consumer as new
            Consumer c = buildConsumer(reg);
            _consumers.get(type).add(c);

            // trigger update on consumer for all existing resources
            // TODO rework
            List<Resource> resources = resourceLocalService.listByTypeAndScopeId(type, reg.getScopeId());

            for (Resource res : resources) {
                try {
                    // fetch full resource definition (ie unencrypted)
                    Resource r = resourceLocalService.get(res.getId());
                    // use UPDATE to create IF MISSING
                    c.updateResource(reg.getScopeId(), reg.getUserId(), r);
                } catch (Exception ex) {
                    _log.error("error registering resource " + res.getId() + ": " + ex.getMessage());
                }
            }

            return reg;
        } catch (NoSuchRegistrationException nrex) {
            throw new NoSuchConsumerException();
        }
    }

    public void delete(long id) throws NoSuchConsumerException {
        try {
            // fetch registration
            Registration reg = registrationService.get(id);
            String type = reg.getType();

            // lookup for consumer
            Consumer consumer = consumerByRegistrationId(type, reg.getId());

            if (consumer != null) {
                // delete consumer - no cleanup or shutdown
                _consumers.get(type).remove(consumer);
            }

            // clear registration
            registrationService.delete(id);
        } catch (NoSuchRegistrationException nrex) {
            throw new NoSuchConsumerException();
        }
    }

    public Consumer lookup(long id) throws NoSuchConsumerException {
        try {
            // fetch registration
            Registration reg = registrationService.get(id);
            String type = reg.getType();

            // lookup for consumer
            Consumer consumer = consumerByRegistrationId(type, reg.getId());

            if (consumer != null) {
                return consumer;
            } else {
                throw new NoSuchConsumerException();
            }
        } catch (NoSuchRegistrationException nrex) {
            throw new NoSuchConsumerException();
        }
    }

    private Consumer consumerByRegistrationId(String type, long id) {
        // lookup for consumer
        // TODO replace with lookup map?
        Consumer consumer = null;
        for (Consumer c : _consumers.get(type)) {
            if (c.getRegistration() != null) {
                if (c.getRegistration().getId() == id) {
                    consumer = c;
                    break;
                }
            }
        }

        return consumer;

    }

    /*
     * Events
     */
    @EventListener
    public void receiveResourceEvent(ResourceEvent event) {
        _log.info("receive message for " + event.getType() + " with payload " + event.getAction() + ":"
                + String.valueOf(event.getId()));

        try {
            // fetch resource (unencrypted)
            Resource res = resourceLocalService.get(event.getId());

            // check if resource events are available for consumption
            if (res.isSubscribed()) {
                String type = event.getType();
                String action = event.getAction();
                String scopeId = event.getScopeId();
                String userId = event.getUserId();

                // notify all active consumers
                // note: filtering by user is performed by consumers, IF required/implemented
                for (Consumer c : _consumers.get(type)) {
                    try {
                        if (c.getStatus() == SystemKeys.STATUS_READY) {
                            _log.info("notify consumer " + c.getId() + " for " + event.getType() + " with payload "
                                    + event.getAction() + ":" + String.valueOf(event.getId()));

                            switch (action) {
                            case SystemKeys.ACTION_CREATE:
                                c.addResource(scopeId, userId, res);
                                break;
                            case SystemKeys.ACTION_UPDATE:
                                c.updateResource(scopeId, userId, res);
                                break;
                            case SystemKeys.ACTION_DELETE:
                                c.deleteResource(scopeId, userId, res);
                                break;
                            case SystemKeys.ACTION_CHECK:
                                c.checkResource(scopeId, userId, res);
                                break;
                            }

                        }
                    } catch (ConsumerException cex) {
                        // log and ignore
                        _log.error("notify consumer " + c.getId() + " for " + event.getType()
                                + " with payload " + event.getAction() + ":" + String.valueOf(event.getId())
                                + " error " + cex.getMessage());
                    }
                }
            }

        } catch (NoSuchResourceException e) {
            _log.error("resource missing for event");
        }

    }
}
