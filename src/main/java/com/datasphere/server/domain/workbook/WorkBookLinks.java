/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.datasphere.server.domain.workbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

/**
 * Created by aladin on 2019. 2. 4..
 */
@Component
public class WorkBookLinks {
  // 实体链接
  @Autowired
  EntityLinks entityLinks;

  public Link getSelfLink(WorkBook workBook) {
    return this.entityLinks.linkForSingleResource(WorkBook.class, workBook.getId())
            .withSelfRel();
  }
  // 获得配置链接
  public Link getConfigurationLink(WorkBook workBook) {
    return this.entityLinks.linkForSingleResource(WorkBook.class, workBook.getId())
            .slash("configuration").withRel("configuration");
  }
}
