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
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.datasphere.datalayer.resource.manager.common.NoSuchConsumerException;
import com.datasphere.datalayer.resource.manager.common.NoSuchRegistrationException;
import com.datasphere.datalayer.resource.manager.model.Registration;
import com.datasphere.datalayer.resource.manager.repository.RegistrationRepository;

@Component
public class RegistrationLocalService {
    private final static Logger _log = LoggerFactory.getLogger(RegistrationLocalService.class);

    @Autowired
    private RegistrationRepository registrationRepository;

    /*
     * Data
     */

    public Registration add(String scopeId, String userId, String type, String consumer,
            Map<String, Serializable> properties, List<String> tags)
            throws NoSuchConsumerException {

        // build registration
        Registration reg = new Registration();
        reg.setScopeId(scopeId);
        reg.setUserId(userId);
        reg.setType(type);
        reg.setConsumer(consumer);
        reg.setPropertiesMap(properties);
        reg.setTags(tags);

        // save registration
        return registrationRepository.saveAndFlush(reg);
    }

    public Registration update(long id, Map<String, Serializable> properties, List<String> tags)
            throws NoSuchRegistrationException, NoSuchConsumerException {

        Registration reg = get(id);
        // update fields
        reg.setPropertiesMap(properties);
        reg.setTags(tags);

        // save registration
        return registrationRepository.save(reg);
    }

    public void delete(long id) throws NoSuchRegistrationException {

        // clear registration
        registrationRepository.deleteById(id);
    }

    public Registration get(long id) throws NoSuchRegistrationException {
        // fetch registration
        Optional<Registration> r = registrationRepository.findById(id);

        if (!r.isPresent()) {
            throw new NoSuchRegistrationException();
        }

        return r.get();
    }

    public Registration fetch(long id) {
        // fetch registration
        Optional<Registration> r = registrationRepository.findById(id);

        if (!r.isPresent()) {
            return null;
        }

        return r.get();
    }

    public boolean exists(long id) {
        return registrationRepository.existsById(id);
    }

    /*
     * Count
     */
    public long count() {
        return registrationRepository.count();
    }

    public long countByType(String type) {
        return registrationRepository.countByType(type);
    }

    public long countByConsumer(String provider) {
        return registrationRepository.countByConsumer(provider);
    }

    public long countByUserIdAndScopeId(String userId, String scopeId) {
        return registrationRepository.countByUserIdAndScopeId(userId, scopeId);
    }

    public long countByScopeId(String scopeId) {
        return registrationRepository.countByScopeId(scopeId);
    }

    public long countByTypeAndScopeId(String type, String scopeId) {
        return registrationRepository.countByTypeAndScopeId(type, scopeId);
    }

    public long countByConsumerAndScopeId(String provider, String scopeId) {
        return registrationRepository.countByConsumerAndScopeId(provider, scopeId);
    }
    /*
     * List
     */

    public List<Registration> list() {
        return registrationRepository.findAll();
    }

    public List<Registration> list(Pageable pageable) {
        Page<Registration> result = registrationRepository.findAll(pageable);
        return result.getContent();
    }

    public List<Registration> listByType(String type) {
        return registrationRepository.findByType(type);
    }

    public List<Registration> listByConsumer(String provider) {
        return registrationRepository.findByConsumer(provider);
    }

    public List<Registration> listByUserIdAndScopeId(String userId, String scopeId) {
        return registrationRepository.findByUserIdAndScopeId(userId, scopeId);
    }

    public List<Registration> listByScopeId(String scopeId) {
        return registrationRepository.findByScopeId(scopeId);
    }

    public List<Registration> listByScopeId(String scopeId, Pageable pageable) {
        Page<Registration> result = registrationRepository.findByScopeId(scopeId, pageable);
        return result.getContent();
    }

    public List<Registration> listByTypeAndScopeId(String type, String scopeId) {
        return registrationRepository.findByTypeAndScopeId(type, scopeId);
    }

    public List<Registration> listByConsumerAndScopeId(String consumer, String scopeId) {
        return registrationRepository.findByConsumerAndScopeId(consumer, scopeId);
    }

}
