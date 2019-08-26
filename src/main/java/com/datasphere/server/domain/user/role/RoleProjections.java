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
import java.util.Map;
import java.util.Set;

import com.datasphere.server.common.BaseProjections;
import com.datasphere.server.domain.user.UserProfile;

/**
 * Created by kyungtaak on 2016. 5. 16..
 */
public class RoleProjections extends BaseProjections {

  @Projection(types = Role.class, name = "default")
  public interface DefaultRoleProjection {
    String getId();

    String getName();

    Boolean getPredefined();
  }

  @Projection(types = Role.class, name = "withPermission")
  public interface RoleWithPermissionProjection {

    String getId();

    String getName();

    Boolean getPredefined();

    Role.RoleScope getScope();

    Boolean getDefaultRole();

    @Value("#{target.getAllPermissionNames()}")
    List<String> getPermissions();
  }

  @Projection(types = Role.class, name = "forListView")
  public interface ForListViewProjection {

    String getId();

    String getName();

    String getDescription();

    Integer getUserCount();

    Integer getGroupCount();

    Boolean getPredefined();

    Boolean getDefaultRole();

  }

  @Projection(types = Role.class, name = "forDetailView")
  public interface ForDetailViewProjection {

    String getId();

    String getName();

    String getDescription();

    Boolean getPredefined();

    Boolean getDefaultRole();

    Integer getUserCount();

    Integer getGroupCount();

    Set<Permission> getPermissions();

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
