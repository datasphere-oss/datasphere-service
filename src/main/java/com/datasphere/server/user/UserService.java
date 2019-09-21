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

package com.datasphere.server.user;

import com.google.common.collect.Lists;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import javax.transaction.Transactional;

import com.datasphere.server.domain.images.Image;
import com.datasphere.server.domain.images.ImageRepository;
import com.datasphere.server.domain.user.group.Group;
import com.datasphere.server.domain.user.group.GroupMember;
import com.datasphere.server.domain.user.group.GroupMemberRepository;
import com.datasphere.server.domain.user.group.GroupRepository;
import com.datasphere.server.domain.user.role.RoleRepository;

@Component
public class UserService {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

  @Autowired
  UserRepository userRepository;

  @Autowired
  GroupRepository groupRepository;

  @Autowired
  GroupMemberRepository groupMemberRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  ImageRepository imageRepository;

  public boolean checkDuplicated(DuplicatedTarget target, String value) {
    Long count = 0L;
    switch (target) {
      case USERNAME:
        count = userRepository.countByUsername(value);
        break;
      case EMAIL:
        count = userRepository.countByEmail(value);
        break;
    }

    return (count > 0) ? true : false;
  }

  public void updateUserImage(String username) {

    List<Image> targetImages = imageRepository.findByDomainAndItemIdOrderByModifiedTimeDesc("user", username);
    if (CollectionUtils.isEmpty(targetImages)) {
      return;
    }

    if (targetImages.size() == 1) {
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

  public void deleteUserImage(String username) {

    List<Image> targetImages = imageRepository.findByDomainAndItemIdOrderByModifiedTimeDesc("user", username);

    if (CollectionUtils.isEmpty(targetImages)) {
      return;
    }

    imageRepository.deleteInBatch(targetImages);
  }

  @Transactional
  public void setUserToGroups(User user, List<String> groupNames, boolean clear) {

    if(clear) {
      groupMemberRepository.deleteByMemberIds(Lists.newArrayList(user.getUsername()));
    }

    for (String groupName : groupNames) {
      Group group = groupRepository.findByName(groupName);
      if(group == null) {
        LOGGER.debug("Group({}) not found. skip!", groupName);
        continue;
      }

      group.addGroupMember(new GroupMember(user.getUsername(), user.getFullName()));
    }
  }

  public enum DuplicatedTarget {
    USERNAME, EMAIL
  }
}
