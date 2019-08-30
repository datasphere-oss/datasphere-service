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

package com.datasphere.server.domain.context;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.datasphere.server.common.entity.DomainType;
import com.datasphere.server.domain.MetatronDomain;

@Component
@Transactional
public class ContextService {

  private static Logger LOGGER = LoggerFactory.getLogger(ContextService.class);

  @Autowired
  ContextRepository contextRepository;

  @Autowired
  ContextDomainRepositoryFactory domainRepositoryFactory;

  @Autowired
  ProjectionFactory projectionFactory;

  public void saveContextFromDomain(ContextEntity domain) {

    Map<String, String> reqContexts = domain.getContextMap();

    if(reqContexts == null) {
      return;
    }

    if(reqContexts.isEmpty()) {
      removeContextFromDomain(domain);
      return;
    }

    DomainType type = DomainType.getType(domain);
    String domainId = ((MetatronDomain<String>) domain).getId();

    Map<String, Context> savedContextMap = contextRepository.findByDomainTypeAndDomainId(type, domainId)
                                                            .stream()
                                                            .collect(Collectors.toMap(Context::getKey, context -> context));

    List<Context> removeEntities = Lists.newArrayList();
    for(String key : savedContextMap.keySet()) {
      if(!reqContexts.containsKey(key)) {
        removeEntities.add(savedContextMap.get(key));
      }
    }
    contextRepository.deleteInBatch(removeEntities);

    List<Context> saveEntities = Lists.newArrayList();
    for (String key : reqContexts.keySet()) {
      if(savedContextMap.containsKey(key)) {
        Context context = savedContextMap.get(key);
        context.setValue(reqContexts.get(key));
        saveEntities.add(context);
      } else {
        saveEntities.add(new Context(type, domainId, key, reqContexts.get(key)));
      }
    }
    contextRepository.save(saveEntities);
    LOGGER.info("Saved {} context from {}, {}", saveEntities.size(), type, domainId);
  }

  public void removeContextFromDomain(ContextEntity domain) {

    DomainType type = DomainType.getType(domain);
    String domainId = ((MetatronDomain<String>) domain).getId();

    contextRepository.deleteByDomainTypeAndDomainId(type, domainId);

    LOGGER.info("Deleted all context from {}, {}", type, domainId);
  }

  @Transactional(readOnly = true)
  public Map<String, String> getContexts(ContextEntity domain) {

    DomainType type = DomainType.getType(domain);
    String domainId = ((MetatronDomain<String>) domain).getId();

    return contextRepository.findByDomainTypeAndDomainId(type, domainId)
                            .stream()
                            .collect(Collectors.toMap(Context::getKey, Context::getValue));
  }

  @Transactional(readOnly = true)
  public Page<Context> getContextDomain(DomainType type, String key,
                                      String value, String domainProjection, Pageable pageable) {

    Page<Context> contexts = contextRepository.findAll(ContextPredicate.searchByKeyValue(type, key, value), pageable);

    final List<String> domainIds = Lists.newArrayList();
    contexts.forEach(context -> domainIds.add(context.getDomainId()));

    List<Object> domainResults = domainRepositoryFactory.getDomainRepository(type)
                                                           .findByIdIn(domainIds);

    Map<String, Object> domainMap = Maps.newHashMap();
    for (Object domainResult : domainResults) {
      ContextEntity entity = (ContextEntity) domainResult;
      domainMap.put(((MetatronDomain<String>) entity).getId(), entity);
    }

    contexts.forEach(context -> {
      context.setDomain(projectionFactory.createProjection(
          type.getProjection().getProjectionByName(domainProjection),
          domainMap.get(context.getDomainId())
          ));
    });

    return contexts;
  }

}
