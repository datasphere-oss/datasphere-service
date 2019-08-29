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

import org.springframework.data.rest.core.config.Projection;

import com.datasphere.server.common.BaseProjections;
import com.datasphere.server.domain.user.DirectoryProfile;

/**
 * Created by aladin on 2019. 5. 16..
 */
public class RoleDirectoryProjections extends BaseProjections {

  @Projection(types = RoleDirectory.class, name = "default")
  public interface DefaultProjection {

    String getDirectoryId();

    String getDirectoryName();

    DirectoryProfile.Type getType();
  }

  @Projection(types = RoleDirectory.class, name = "forListView")
  public interface ForListViewProjection {

    String getId();

    String getName();

    String getDescription();

    Boolean getPredefined();

    Boolean getDefaultRole();

  }

}
