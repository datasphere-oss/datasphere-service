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

package com.datasphere.server.user.controller;

import static com.datasphere.server.user.UserService.DuplicatedTarget.EMAIL;
import static com.datasphere.server.user.UserService.DuplicatedTarget.USERNAME;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.datasphere.server.common.Mailer;
import com.datasphere.server.common.entity.SearchParamValidator;
import com.datasphere.server.common.exception.BadRequestException;
import com.datasphere.server.common.exception.ResourceNotFoundException;
import com.datasphere.server.domain.images.Image;
import com.datasphere.server.domain.images.ImageService;
import com.datasphere.server.domain.workspace.Workspace;
import com.datasphere.server.domain.workspace.WorkspaceMemberRepository;
import com.datasphere.server.domain.workspace.WorkspaceService;
import com.datasphere.server.user.User;
import com.datasphere.server.user.UserErrorCodes;
import com.datasphere.server.user.UserException;
import com.datasphere.server.user.UserPredicate;
import com.datasphere.server.user.UserRepository;
import com.datasphere.server.user.UserService;
import com.datasphere.server.user.group.Group;
import com.datasphere.server.user.group.GroupMember;
import com.datasphere.server.user.group.GroupService;
import com.datasphere.server.user.role.RoleRepository;
import com.datasphere.server.user.role.RoleService;
import com.datasphere.server.user.role.RoleSetRepository;
import com.datasphere.server.user.role.RoleSetService;
import com.datasphere.server.user.service.CachedUserService;
import com.datasphere.server.util.AuthUtils;
import com.datasphere.server.util.PolarisUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.querydsl.core.types.Predicate;

/**
 * Created by aladin on 2019. 7. 21..
 */
@RepositoryRestController
public class UserController {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

  @Autowired
  RoleService roleService;

  @Autowired
  GroupService groupService;

  @Autowired
  RoleSetService roleSetService;

  @Autowired
  WorkspaceService workspaceService;

  @Autowired
  ImageService imageService;

  @Autowired
  UserRepository userRepository;

  @Autowired
  WorkspaceMemberRepository workspaceMemberRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  RoleSetRepository roleSetRepository;

  @Autowired
  ProjectionFactory projectionFactory;

  @Autowired
  CachedUserService cachedUserService;

  @Autowired
  PagedResourcesAssembler pagedResourcesAssembler;

  @Autowired
  UserService userService;

  @Autowired
  Mailer mailer;

  @Autowired
  PasswordEncoder passwordEncoder;

  /**
   * User List Lookup
   */
  @RequestMapping(path = "/users", method = RequestMethod.GET)
  public ResponseEntity<?> findUsers(@RequestParam(value = "level", required = false) String level,
                                     @RequestParam(value = "active", required = false) Boolean active,
                                     @RequestParam(value = "status", required = false) List<String> status,
                                     @RequestParam(value = "nameContains", required = false) String nameContains,
                                     @RequestParam(value = "searchDateBy", required = false) String searchDateBy,
                                     @RequestParam(value = "from", required = false)
                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime from,
                                     @RequestParam(value = "to", required = false)
                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime to,
                                     Pageable pageable, PersistentEntityResourceAssembler resourceAssembler) {

    List<User.Status> reqStatus = null;
    if (CollectionUtils.isNotEmpty(status)) {
      reqStatus = Lists.newArrayList();
      for (String s : status) {
        reqStatus.add(SearchParamValidator.enumUpperValue(User.Status.class, s, "status"));
      }
    }

    // Get Predicate
    Predicate searchPredicated = UserPredicate.searchList(level, active, reqStatus, nameContains, searchDateBy, from, to);

    // Default sort condition settings
    if (pageable.getSort() == null || !pageable.getSort().iterator().hasNext()) {
      pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                                 new Sort(Sort.Direction.ASC, "fullName"));
    }

    Page<User> users = userRepository.findAll(searchPredicated, pageable);


    return ResponseEntity.ok(this.pagedResourcesAssembler.toResource(users, resourceAssembler));

  }

  /**
   * User Detailed Search
   */
  @RequestMapping(path = "/users/{username:.+}", method = RequestMethod.GET)
  public ResponseEntity<?> findDetailUser(@PathVariable("username") String username,
                                          PersistentEntityResourceAssembler resourceAssembler) {

    User user = userRepository.findByUsername(username);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(resourceAssembler.toResource(user));

  }

  /**
   * Delete User
   */
  @Transactional
  @PreAuthorize("authentication.name == #username or hasAuthority('PERM_SYSTEM_MANAGE_USER')")
  @RequestMapping(path = "/users/{username:.+}", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteUser(@PathVariable("username") String username) {

    User user = userRepository.findByUsername(username);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }

    userRepository.delete(user);

    // Image processing
    if (StringUtils.isNotEmpty(user.getImageUrl())) {
      imageService.deleteImage(Image.DOMAIN_USER, user.getId());
    }

    // Delete member in group
    groupService.deleteGroupMember(user.getUsername());

    // Workspace related processing
    workspaceService.deleteWorkspaceAndMember(user.getUsername());

    // Delete any user information stored in the cache
    cachedUserService.removeCachedUser(user.getUsername());

    return ResponseEntity.noContent().build();
  }

  /**
   * Modify user information by User himself
   */
  @Transactional
  @PreAuthorize("authentication.name == #username")
  @RequestMapping(path = "/users/{username:.+}", method = RequestMethod.PATCH)
  public ResponseEntity<?> updateUser(@PathVariable("username") String username, @RequestBody User user) {

    User updatedUser = userRepository.findByUsername(username);
    if (updatedUser == null) {
      throw new ResourceNotFoundException(username);
    }

    if (!username.equals(AuthUtils.getAuthUserName())) {
      throw new UserException("Fail to update permission. only user(" + username + ") can update.");
    }

    if (user.getPassword() != null){
      String encodedPassword = passwordEncoder.encode(user.getPassword());
      updatedUser.setPassword(encodedPassword);
    }

    if (user.getFullName() != null) updatedUser.setFullName(user.getFullName());
    if (user.getEmail() != null) updatedUser.setEmail(user.getEmail());
    if (user.getTel() != null) updatedUser.setTel(user.getTel());
    if (user.getImageUrl() != null) {
      if (StringUtils.isBlank(user.getImageUrl())) {
        userService.deleteUserImage(updatedUser.getUsername());
        updatedUser.setImageUrl(null);
      } else {
        userService.updateUserImage(updatedUser.getUsername());
        updatedUser.setImageUrl(user.getImageUrl());
      }
    }

    userRepository.saveAndFlush(updatedUser);

    // update user information
    updatedUser.setRoleService(roleService);
    AuthUtils.refreshAuth(updatedUser);
    cachedUserService.removeCachedUser(username);

    // Update Workspace Member Name
    workspaceMemberRepository.updateMemberName(updatedUser.getUsername(), updatedUser.getFullName());

    return ResponseEntity.ok(updatedUser);

  }

  /**
   * User signup request
   */
  @RequestMapping(path = "/users/signup", method = RequestMethod.POST)
  public ResponseEntity<?> createUserBySignup(@RequestBody User user) {

    // Username duplication check
    if (userService.checkDuplicated(USERNAME, user.getUsername())) {
      throw new UserException(UserErrorCodes.DUPLICATED_USERNAME_CODE, "Duplicated username : " + user.getUsername());
    }

    // duplicate email check
    if (userService.checkDuplicated(USERNAME, user.getUsername())) {
      throw new UserException(UserErrorCodes.DUPLICATED_EMAIL_CODE, "Duplicated e-mail : " + user.getEmail());
    }

    if (StringUtils.isBlank(user.getFullName())) {
      user.setFullName(user.getUsername());
    }

    if (StringUtils.isNotBlank(user.getImageUrl())) {
      userService.updateUserImage(user.getUsername());
    }

    if (user.getPassword() != null){
      String encodedPassword = passwordEncoder.encode(user.getPassword());
      user.setPassword(encodedPassword);
    }

    user.setStatus(User.Status.REQUESTED);

    userRepository.save(user);

    mailer.sendSignUpRequestMail(user, false);

    return ResponseEntity.created(URI.create("")).build();
  }

  /**
   * User Registration for Administrators
   */
  @Transactional
  @PreAuthorize("hasAuthority('PERM_SYSTEM_MANAGE_USER')")
  @RequestMapping(path = "/users/manual", method = RequestMethod.POST)
  public ResponseEntity<?> createUserByAdmin(@RequestBody User user) {

    String userEmail = user.getEmail();

    // Username duplication check
    if (userService.checkDuplicated(USERNAME, user.getUsername())) {
      throw new UserException(UserErrorCodes.DUPLICATED_USERNAME_CODE, "Duplicated username : " + user.getUsername());
    }

    // duplicate email check
    if (StringUtils.isNotEmpty(userEmail) && userService.checkDuplicated(EMAIL, user.getEmail())) {
      throw new UserException(UserErrorCodes.DUPLICATED_EMAIL_CODE, "Duplicated e-mail : " + user.getEmail());
    }

    if (StringUtils.isBlank(user.getFullName())) {
      user.setFullName(user.getUsername());
    }

    if (StringUtils.isNotBlank(user.getImageUrl())) {
      userService.updateUserImage(user.getUsername());
    }

    // If you do not perform a mail transfer and do not specify a password
    if (!user.getPassMailer() || StringUtils.isEmpty(user.getPassword())) {
      String encodedPassword = passwordEncoder.encode(PolarisUtils.createTemporaryPassword(8));
      user.setPassword(encodedPassword);
    }

    user.setStatus(User.Status.ACTIVATED);

    // Specify default group if there is no Group information
    if (CollectionUtils.isNotEmpty(user.getGroupNames())) {
      userService.setUserToGroups(user, user.getGroupNames(), false);
    } else {
      Group defaultGroup = groupService.getDefaultGroup();
      if (defaultGroup == null) {
        LOGGER.warn("Default group not found.");
      } else {
        defaultGroup.addGroupMember(new GroupMember(user.getUsername(), user.getFullName()));
      }
    }

    // Create Workspace (if no workspace is registered)
    Workspace createdWorkspace = workspaceService.createWorkspaceByUserCreation(user, false);

    userRepository.save(user);

    if (!user.getPassMailer()) {
      mailer.sendSignUpApprovedMail(user, true, user.getPassword());
    }

    Map<String, Object> responseMap = Maps.newHashMap();
    responseMap.put("username", user.getUsername());
    responseMap.put("privateWorkspace", "/api/workspaces/" + createdWorkspace.getId());

    return ResponseEntity.created(URI.create("")).body(responseMap);
  }

  /**
   * Update User Information for Administrators
   */
  @Transactional
  @PreAuthorize("hasAuthority('PERM_SYSTEM_MANAGE_USER')")
  @RequestMapping(path = "/users/{username}/manual", method = RequestMethod.PUT)
  public ResponseEntity<?> updateUserByAdmin(@PathVariable("username") String username, @RequestBody User user) {

    User updatedUser = userRepository.findByUsername(username);

    if (updatedUser == null) {
      return ResponseEntity.notFound().build();
    }

    userService.setUserToGroups(updatedUser, user.getGroupNames(), true);
    updatedUser.setFullName(user.getFullName());
    updatedUser.setEmail(user.getEmail());
    updatedUser.setTel(user.getTel());

    if (StringUtils.isBlank(user.getImageUrl()) && StringUtils.isNotBlank(updatedUser.getImageUrl())) {
      userService.deleteUserImage(updatedUser.getUsername());
      updatedUser.setImageUrl(null);
    }

    if (StringUtils.isNotBlank(user.getImageUrl())) {
      userService.updateUserImage(username);
      updatedUser.setImageUrl(user.getImageUrl());
    }

    userRepository.saveAndFlush(updatedUser);

    // update user information
    if (AuthUtils.getAuthUserName().equals(updatedUser.getUsername())) {
      updatedUser.setRoleService(roleService);
      AuthUtils.refreshAuth(updatedUser);
    }

    // Update cache storage information
    cachedUserService.removeCachedUser(username);

    // Update Workspace Member Name
    workspaceMemberRepository.updateMemberName(updatedUser.getUsername(), updatedUser.getFullName());

    return ResponseEntity.ok(updatedUser);
  }

  /**
   *
   * @param additionalInfo
   * @return
   */
  @RequestMapping(path = "/users/password/reset", method = RequestMethod.POST)
  public ResponseEntity<?> resetPassword(@RequestBody Map<String, Object> additionalInfo) {

    if (!additionalInfo.containsKey("email")) {
      throw new BadRequestException("E-mail address required");
    }

    String email = (String) additionalInfo.get("email");
    User user = userRepository.findByEmail(email);
    if (user == null) {
      throw new BadRequestException("User not found by email( " + email + " )");
    }

    String temporaryPassword = PolarisUtils.createTemporaryPassword(8);
    String encodedPassword = passwordEncoder.encode(temporaryPassword);
    user.setPassword(encodedPassword);

    userRepository.saveAndFlush(user);

    boolean isAdmin = false;
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {
      isAdmin = true;
    }

    mailer.sendPasswordResetMail(user, temporaryPassword, isAdmin);

    return ResponseEntity.noContent().build();
  }

  /**
   *
   * @param username
   * @param additionalInfo
   * @return
   */
  @RequestMapping(path = "/users/{username}/check/password", method = RequestMethod.POST)
  public ResponseEntity<?> checkPassword(@PathVariable("username") String username, @RequestBody Map<String, Object> additionalInfo) {

    if (!additionalInfo.containsKey("password")) {
      throw new BadRequestException("password required");
    }

    String password = (String) additionalInfo.get("password");
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new ResourceNotFoundException(username);
    }

    Map<String, Boolean> matched = Maps.newHashMap();
    matched.put("matched", passwordEncoder.matches(password, user.getPassword()));

    return ResponseEntity.ok(matched);
  }

  /**
   *
   * @param target
   * @param value
   * @return
   */
  @RequestMapping(path = "/users/{target}/{value}/duplicated", method = RequestMethod.GET)
  public ResponseEntity<?> checkDuplicatedValue(@PathVariable("target") String target, @PathVariable("value") String value) {

    UserService.DuplicatedTarget targetType = SearchParamValidator
        .enumUpperValue(UserService.DuplicatedTarget.class, target, "target");

    Map<String, Boolean> duplicated = Maps.newHashMap();
    duplicated.put("duplicated", userService.checkDuplicated(targetType, value));

    return ResponseEntity.ok(duplicated);

  }

  /**
   * Approval of subscription
   */
  @Transactional
  @PreAuthorize("hasAuthority('PERM_SYSTEM_MANAGE_USER')")
  @RequestMapping(path = "/users/{username}/approved", method = RequestMethod.POST)
  public ResponseEntity<?> approvedJoinedUser(@PathVariable("username") String username) {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new ResourceNotFoundException(username);
    }

    user.setStatus(User.Status.ACTIVATED);

    // Include in primary group
    Group defaultGroup = groupService.getDefaultGroup();
    if (defaultGroup == null) {
      LOGGER.warn("Default group not found.");
    } else {
      defaultGroup.addGroupMember(new GroupMember(user.getUsername(), user.getFullName()));
    }

    // Create Workspace (if no workspace is registered)
    workspaceService.createWorkspaceByUserCreation(user, false);

    mailer.sendSignUpApprovedMail(user, false, null);

    return ResponseEntity.noContent().build();
  }

  /**
   *Return to join approval
   */
  @PreAuthorize("hasAuthority('PERM_SYSTEM_MANAGE_USER')")
  @RequestMapping(path = "/users/{username}/rejected", method = RequestMethod.POST)
  public ResponseEntity<?> rejectedJoinedUser(@PathVariable("username") String username,
                                              @RequestBody Map<String, Object> additionalInfo) {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new ResourceNotFoundException(username);
    }

    user.setStatus(User.Status.REJECTED);
    user.setStatusMessage((String) additionalInfo.get("message"));

    userRepository.save(user);

    mailer.sendSignUpDeniedMail(user);

    return ResponseEntity.noContent().build();
  }

  /**
   * Only user status change and administrator (user change) authority can be handled
   */
  @PreAuthorize("hasAuthority('PERM_SYSTEM_MANAGE_USER')")
  @RequestMapping(path = "/users/{username}/status/{status}", method = RequestMethod.POST)
  public ResponseEntity<?> changeStatus(@PathVariable("username") String username,
                                        @PathVariable("status") String status) {

    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new ResourceNotFoundException(username);
    }

    User.Status reqStatus = SearchParamValidator
        .enumUpperValue(User.Status.class, status, "status");

    switch (reqStatus) {
      case ACTIVATED:
        if (user.getStatus() == User.Status.ACTIVATED) {
          throw new UserException("Already activated user.");
        }
        user.setStatus(User.Status.ACTIVATED);
        break;
      case LOCKED:
        if (user.getStatus() == User.Status.LOCKED) {
          throw new RuntimeException("Already inactivated user.");
        }
        user.setStatus(User.Status.LOCKED);
        break;
      case DELETED:
        if (user.getStatus() == User.Status.DELETED) {
          throw new RuntimeException("Already deleted user.");
        }
        user.setStatus(User.Status.DELETED);

        // Delete any user information stored in the cache
        cachedUserService.removeCachedUser(user.getUsername());
        break;
      default:
        throw new BadRequestException("Unsupported status. choose 'activate' or 'locked'");
    }

    userRepository.save(user);

    return ResponseEntity.noContent().build();
  }

}
