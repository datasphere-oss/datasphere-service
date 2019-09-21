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

package com.datasphere.server.user.group;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.util.List;
import java.util.Map;

import com.datasphere.server.common.BaseProjections;
import com.datasphere.server.domain.user.UserProfile;

/**
 * Created by aladin on 2019. 5. 16..
 */
public class GroupProjections extends BaseProjections {

  @Projection(types = Group.class, name = "default")
  public interface DefaultProjection {
    String getId();

    String getName();

    Boolean getPredefined();

    Boolean getReadOnly();
  }

  @Projection(types = Group.class, name = "forListView")
  public interface ForListProjection {

    String getId();

    String getName();

    String getDescription();

    Integer getMemberCount();

    Boolean getPredefined();

    Boolean getReadOnly();

    DateTime getCreatedTime();

    DateTime getModifiedTime();
  }

  @Projection(types = Group.class, name = "forDetailView")
  public interface ForDetailProjection {

    String getId();

    String getName();

    String getDescription();

    Integer getMemberCount();

    Boolean getPredefined();

    Boolean getReadOnly();

    @Value("#{@roleService.getRoleNamesByGroupId(target.id)}")
    List<String> getRoleNames();

    @Value("#{@contextService.getContexts(target)}")
    Map<String, Object> getContexts();

    @Value("#{@cachedUserService.findUserProfile(target.createdBy)}")
    UserProfile getCreatedBy();

    DateTime getCreatedTime();

    @Value("#{@cachedUserService.findUserProfile(target.modifiedBy)}")
    UserProfile getModifiedBy();

    DateTime getModifiedTime();
  }
}
