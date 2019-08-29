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

package com.datasphere.server.domain.workbook.widget;

import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.datasphere.server.common.GlobalObjectMapper;
import com.datasphere.server.domain.workbook.DashBoard;
import com.datasphere.server.domain.workbook.configurations.WidgetConfiguration;
import com.datasphere.server.domain.workbook.configurations.widget.FilterWidgetConfiguration;
import com.datasphere.server.util.PolarisUtils;

/**
 * Created by aladin on 2019. 7. 18..
 */
@Entity
@JsonTypeName("filter")
@DiscriminatorValue("filter")
public class FilterWidget extends Widget {

  /**
   * Default Constructor
   */
  public FilterWidget() {
    // Empty Constructor
  }

  @Override
  public Widget copyOf(DashBoard parent, boolean addPrefix) {
    FilterWidget filterWidget = new FilterWidget();
    filterWidget.setName(addPrefix ? PolarisUtils.COPY_OF_PREFIX + name : name);
    filterWidget.setDescription(description);
    filterWidget.setConfiguration(configuration);

    if(parent == null) {
      filterWidget.setDashBoard(dashBoard);
    } else {
      filterWidget.setDashBoard(parent);
    }

    return filterWidget;
  }

  @Override
  public WidgetConfiguration convertConfiguration() {
    return GlobalObjectMapper.readValue(this.configuration, FilterWidgetConfiguration.class);
  }
}
