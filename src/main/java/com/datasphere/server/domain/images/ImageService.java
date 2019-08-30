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

package com.datasphere.server.domain.images;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.List;

import com.datasphere.server.common.exception.ResourceNotFoundException;

@Service
@Transactional(readOnly = true)
public class ImageService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ImageService.class);

  @Autowired
  ImageRepository imageRepository;

  /**
   * 내부적으로 정의된 metatron Images URL 을 통해 Image 객체를 로드힙니다.
   *
   * @param url
   * @return
   */
  public Image loadImageByImageUrl(String url) {

    // URL Sample : metatron://images/{domain}/{itemId}/thumbnail
    URI uri = null;
    try {
      uri = URI.create(url);
    } catch (IllegalArgumentException e) {
      LOGGER.error("Invalid url - schema : {}", url);
      throw new IllegalArgumentException("Invalid url - schema");
    }

    if(!"images".equals(uri.getHost())) {
      LOGGER.error("Invalid url - images(host) : {}", url);
      throw new IllegalArgumentException("Invalid url - images(host)");
    }

    String[] values = StringUtils.split(uri.getPath(), "/");
    int length = values.length;
    if(length < 2 && length > 3) {
      LOGGER.error("Invalid url - type of images : {}", url);
      throw new IllegalArgumentException("Invalid url - type of images");
    }

    Image image = imageRepository.findByDomainAndItemIdAndEnabled(values[0], values[1], true);
    if(image == null) {
      LOGGER.error("Image resource not found. : {}", url);
      throw  new ResourceNotFoundException(url);
    }

    return image;
  }

  @Transactional
  public Image copyByUrl(String targetDomainId, String url) {

    Image image = null;
    try {
      image = loadImageByImageUrl(url);
    } catch (Exception e) {
      LOGGER.warn("Fail to copy image entity. Caused by " + e.getMessage());
      return null;
    }

    Image copiedImage = image.copyOf();
    if(StringUtils.isNotEmpty(targetDomainId)) {
      copiedImage.setItemId(targetDomainId);
    }

    return imageRepository.save(copiedImage);
  }

  public void deleteImage(String domain, String id) {

    List<Image> targetImages = imageRepository.findByDomainAndItemIdOrderByModifiedTimeDesc(domain, id);

    if(CollectionUtils.isEmpty(targetImages)) {
      return;
    }

    imageRepository.deleteInBatch(targetImages);
  }
}
