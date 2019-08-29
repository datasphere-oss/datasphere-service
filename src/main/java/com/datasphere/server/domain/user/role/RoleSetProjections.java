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

package com.datasphere.server.domain.user.role;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.util.List;

import com.datasphere.server.common.BaseProjections;
import com.datasphere.server.domain.user.UserProfile;

/**
 * Created by aladin on 2019. 5. 16..
 */
public class RoleSetProjections extends BaseProjections {

  @Projection(types = RoleSet.class, name = "default")
  public interface DefaultRoleProjection {

    String getId();

    String getName();

    Boolean getPredefined();

    Boolean getReadOnly();
  }

  @Projection(types = RoleSet.class, name = "forListView")
  public interface ForListViewProjection {

    String getId();

    String getName();

    String getDescription();

    Boolean getPredefined();

    Boolean getReadOnly();

    Integer getLinkedWorkspaces();

    @Value("#{@cachedUserService.findUserProfile(target.createdBy)}")
    UserProfile getCreatedBy();

    DateTime getCreatedTime();

    @Value("#{@cachedUserService.findUserProfile(target.modifiedBy)}")
    UserProfile getModifiedBy();

    DateTime getModifiedTime();
  }

  @Projection(types = RoleSet.class, name = "forDetailView")
  public interface ForDetailViewProjection {

    String getId();

    String getName();

    String getDescription();

    Boolean getPredefined();

    Boolean getReadOnly();

    Integer getLinkedWorkspaces();

    //Set<Role> getRoles();
    @Value("#{T(com.datasphere.server.util.ProjectionUtils).toListResource(@projectionFactory, T(com.datasphere.server.domain.user.role.RoleProjections$RoleWithPermissionProjection), target.roles)}")
    List getRoles();

    @Value("#{@cachedUserService.findUserProfile(target.createdBy)}")
    UserProfile getCreatedBy();

    DateTime getCreatedTime();

    @Value("#{@cachedUserService.findUserProfile(target.modifiedBy)}")
    UserProfile getModifiedBy();

    DateTime getModifiedTime();
  }
}
