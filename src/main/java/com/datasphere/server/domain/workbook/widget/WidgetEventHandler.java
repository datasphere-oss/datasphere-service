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

package com.datasphere.server.domain.workbook.widget;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

import com.datasphere.server.domain.images.Image;
import com.datasphere.server.domain.images.ImageRepository;

/**
 * Created by aladin on 2019. 5. 13..
 */
@RepositoryEventHandler(Widget.class)
public class WidgetEventHandler {

  @Autowired
  ImageRepository imageRepository;

  @HandleBeforeCreate
  @PreAuthorize("hasPermission(#widget, 'PERM_WORKSPACE_WRITE_BOOK')")
  public void checkBeforeCreate(Widget widget) {
  }

  @HandleBeforeSave
  @PreAuthorize("hasPermission(#widget, 'PERM_WORKSPACE_WRITE_BOOK')")
  public void checkBeforeUpdate(Widget widget) {

    if(widget instanceof PageWidget) {
      PageWidget pageWidget = (PageWidget) widget;
      // Image processing
      if(StringUtils.isNotEmpty(pageWidget.getImageUrl())) {
        updateImages(pageWidget.getId());
      }
    }

  }

  @HandleBeforeDelete
  @PreAuthorize("hasPermission(#widget, 'PERM_WORKSPACE_WRITE_BOOK')")
  public void checkBeforeDelete(Widget widget) {

    if(widget instanceof PageWidget) {
      PageWidget pageWidget = (PageWidget) widget;
      // Image processing
      if(StringUtils.isNotEmpty(pageWidget.getImageUrl())) {
        deleteImages(pageWidget.getId());
      }
    }

  }

  private void updateImages(String id) {
    List<Image> targetImages = imageRepository.findByDomainAndItemIdOrderByModifiedTimeDesc("page", id);
    if(CollectionUtils.isEmpty(targetImages)) {
      return;
    }

    if(targetImages.size() == 1) {
      targetImages.get(0).setEnabled(true);
      imageRepository.save(targetImages.get(0));
    } else {
      // If you uploaded a photo multiple times
      for (int i = 0; i < targetImages.size(); i++) {
        if (i == 0) {
          targetImages.get(i).setEnabled(true);
          imageRepository.save(targetImages.get(i));
        } else {
          imageRepository.delete(targetImages.get(i));
        }
      }
    }
  }

  private void deleteImages(String id) {
    List<Image> targetImages = imageRepository.findByDomainAndItemIdOrderByModifiedTimeDesc("page", id);

    if(CollectionUtils.isEmpty(targetImages)) {
      return;
    }

    imageRepository.deleteInBatch(targetImages);
  }
}
