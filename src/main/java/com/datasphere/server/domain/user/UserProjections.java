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

package com.datasphere.server.domain.user;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.util.List;
import java.util.Map;

import com.datasphere.server.common.BaseProjections;

/**
 * Created by aladin on 2019. 5. 16..
 */
public class UserProjections extends BaseProjections {

  @Projection(types = User.class, name = "default")
  public interface DefaultUserProjection {

    @Value("#{target.username}")
    String getId();

    String getUsername();

    String getFullName();

    String getEmail();

    String getImageUrl();
  }

  @Projection(types = User.class, name = "forDetailView")
  public interface ForDetailProjection {

    @Value("#{target.username}")
    String getId();

    String getUsername();

    String getFullName();

    String getEmail();

    String getTel();

    String getImageUrl();

    User.Status getStatus();

    String getStatusMessage();

    @Value("#{@roleService.getRoleNamesByUsername(target.getUsername())}")
    List<String> getRoleNames();

    @Value("#{@groupService.getJoinedGroupsForProjection(target.getUsername(), true)}")
    List<Map<String, Object>> getGroups();

    DateTime getCreatedTime();

    DateTime getModifiedTime();
  }

  @Projection(types = User.class, name = "forListView")
  public interface ForListProjection {

    @Value("#{target.username}")
    String getId();

    String getUsername();

    String getFullName();

    String getEmail();

    String getImageUrl();

    User.Status getStatus();

    String getStatusMessage();

    @Value("#{@groupService.getJoinedGroupsForProjection(target.getUsername(), false)}")
    List<Map<String, Object>> getGroups();

    DateTime getCreatedTime();
  }

  @Projection(types = User.class, name = "forMemberListView")
  public interface ForMemberListProjection {

    @Value("#{target.username}")
    String getId();

    String getUsername();

    String getFullName();

    String getEmail();

    String getImageUrl();

    @Value("#{@groupService.getJoinedGroupsForProjection(target.getUsername(), false)}")
    List<Map<String, Object>> getGroups();

  }

}
