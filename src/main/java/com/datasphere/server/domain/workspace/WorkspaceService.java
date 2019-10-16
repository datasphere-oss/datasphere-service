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

package com.datasphere.server.domain.workspace;

import static com.datasphere.server.domain.workspace.Workspace.PublicType.SHARED;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.datasphere.engine.datasource.DataSourceRepository;
import com.datasphere.engine.datasource.QDataSource;
import com.datasphere.server.common.MatrixResponse;
import com.datasphere.server.common.exception.BadRequestException;
import com.datasphere.server.common.exception.ResourceNotFoundException;
import com.datasphere.server.common.domain.CollectionPatch;
import com.datasphere.server.domain.activities.ActivityStreamService;
import com.datasphere.server.user.service.CachedUserService;
import com.datasphere.server.user.DirectoryProfile;
import com.datasphere.server.user.User;
import com.datasphere.server.user.group.Group;
import com.datasphere.server.user.group.GroupRepository;
import com.datasphere.server.user.group.GroupService;
import com.datasphere.server.user.role.Role;
import com.datasphere.server.user.role.RoleRepository;
import com.datasphere.server.user.role.RoleService;
import com.datasphere.server.user.role.RoleSet;
import com.datasphere.server.user.role.RoleSetRepository;
import com.datasphere.server.user.role.RoleSetService;
import com.datasphere.server.domain.workbook.configurations.format.TimeFieldFormat;
import com.datasphere.server.util.AuthUtils;
import com.datasphere.server.util.EnumUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;

/**
 * Created by aladin on 2019. 7. 23..
 */
@Component
@Transactional(readOnly = true)
public class WorkspaceService {

  private static Logger LOGGER = LoggerFactory.getLogger(WorkspaceService.class);

  @Autowired
  ActivityStreamService activityStreamService;

  @Autowired
  GroupService groupService;

  @Autowired
  GroupRepository groupRepository;

  @Autowired
  RoleService roleService;

  @Autowired
  RoleSetService roleSetService;

  @Autowired
  CachedUserService cachedUserService;

  @Autowired
  DataSourceRepository dataSourceRepository;

  @Autowired
  WorkspaceRepository workspaceRepository;

  @Autowired
  WorkspaceMemberRepository workspaceMemberRepository;

  @Autowired
  WorkspaceFavoriteRepository workspaceFavoriteRepository;

  @Autowired
  RoleSetRepository roleSetRepository;

  @Autowired
  RoleRepository roleRepository;

  @Transactional
  public Workspace createWorkspaceByUserCreation(User user, boolean ifExistThrowException) {
	// 用户创建的同时, 创建工作空间
    // Create Workspace (if no workspace is registered)
    Workspace checkedWorkspace = workspaceRepository.findPrivateWorkspaceByOwnerId(user.getUsername());
    if(checkedWorkspace != null) {
      if (ifExistThrowException ) {
        throw new RuntimeException("Workspace already exist.");
      } else {
        return checkedWorkspace;
      }
    }

    Workspace createWorkspace = new Workspace();
    createWorkspace.setPublicType(Workspace.PublicType.PRIVATE);
    createWorkspace.setName(user.getFullName() + " Workspace");
    createWorkspace.setOwnerId(user.getUsername());

    if(StringUtils.isNotEmpty(user.getWorkspaceType())
        && Workspace.workspaceTypes.contains(user.getWorkspaceType())) {
      createWorkspace.setType(user.getWorkspaceType());
    } else {
      createWorkspace.setType(Workspace.workspaceTypes.get(0)); // "DEFAULT" setting
    }

    return workspaceRepository.saveAndFlush(createWorkspace);

  }

  /**
   * Specify Workspace Access Times
   */
  @Transactional
  public void updateLastAccessedTime(String workspaceId) {
    workspaceRepository.updateLastAccessedTime(workspaceId);
  }

  /**
   * Workspace processing when user is deleted
   */
  @Transactional
  public void disableWorkspaceAndMember(String username, Map<String, String> delegateePair) {
    // In case of Workspace Owner, if there is no delegate information, all Inactive
    if(MapUtils.isEmpty(delegateePair)) {
      workspaceRepository.updateInactiveWorkspaceAndChangeKnownOwnerId(username, Workspace.PublicType.PRIVATE, Workspace.PublicType.SHARED);
    } else {
      // Only disable private workspaces
      workspaceRepository.updateInactiveWorkspaceOfOwner(username, Workspace.PublicType.PRIVATE);
      // TODO: delegate through delegateePair
    }

    // Delete if included as a Workspace Member
    workspaceMemberRepository.deleteByMemberIds(Lists.newArrayList(username));

    // Delete Workspace Favorite
    workspaceFavoriteRepository.deleteByUsername(username);
  }

  /**
   * Delete Workspace When Deleting Users
   */
  @Transactional
  public void deleteWorkspaceAndMember(String username) {
    Workspace workspace = workspaceRepository.findPrivateWorkspaceByOwnerId(username);
    if(workspace == null) {
      LOGGER.warn("Fail to find private workspace : {}", username);
    } else {
      workspaceRepository.delete(workspace);
    }

   
    workspaceMemberRepository.deleteByMemberIds(Lists.newArrayList(username));

    workspaceFavoriteRepository.deleteByUsername(username);
  }

  public Integer countAvailableWorkspaces(String workspaceId) {

    BooleanBuilder builder = new BooleanBuilder();
    QDataSource dataSource = QDataSource.dataSource;

    // full public Sub-Query
    BooleanExpression published = dataSource.id
        .in(JPAExpressions.select(dataSource.id)
                          .from(dataSource)
                          .where(dataSource.published.eq(true)));

    // Subsource Query of Datasource in Workspace
    BooleanExpression workspaceContains = dataSource.id
        .in(JPAExpressions.select(dataSource.id)
                          .from(dataSource)
                          .innerJoin(dataSource.workspaces)
                          .where(dataSource.workspaces.any().id.eq(workspaceId)));

    builder = builder.andAnyOf(workspaceContains, published);

    long count = dataSourceRepository.count(builder);

    return (int) count;
  }

  /**
   *
   * @param workbookId
   * @param workspaceId
   * @return
   */
  public boolean checkWorkBookCopyToWorkspace(String workbookId, String workspaceId) {

    List<String> dataSourceIdInWorkbook = dataSourceRepository.findIdsByWorkbookInNotPublic(workbookId);

    List<String> dataSourceIdInWorkspace = dataSourceRepository.findIdsByWorkspaceIn(workspaceId);

    return CollectionUtils.containsAll(dataSourceIdInWorkspace, dataSourceIdInWorkbook);
  }

  @Transactional
  public void updateMembers(Workspace workspace, List<CollectionPatch> patches) {

	// default roleSet if no RoleSet
    if (CollectionUtils.isEmpty(workspace.getRoleSets())) {
      workspace.addRoleSet(roleSetService.getDefaultRoleSet());
    }

    Map<String, Role> roleMap = workspace.getRoleMap();
    Map<String, WorkspaceMember> memberMap = workspace.getMemberIdMap();
    for (CollectionPatch patch : patches) {
      String memberId = patch.getValue("memberId");
      switch (patch.getOp()) {
        case ADD:
        case REPLACE:
          if (!validateCollectionPatchForMember(patch, roleMap)) {
            continue;
          }

          if (memberMap.containsKey(memberId)) {
            memberMap.get(memberId).setRole(patch.getValue("role"));
            LOGGER.debug("Replaced member in workspace({}) : {}, {}", workspace.getId(), memberId, patch.getValue("role"));
          } else {
            workspace.getMembers().add(new WorkspaceMember(patch, workspace, this.cachedUserService));
            LOGGER.debug("Added member in workspace({}) : {}, {}", workspace.getId(), memberId, patch.getValue("role"));
          }

          break;
        case REMOVE:
          if (memberMap.containsKey(memberId)) {
            workspace.getMembers().remove(memberMap.get(memberId));
            LOGGER.debug("Deleted member in workspace({}) : {}", workspace.getId(), memberId);
         // remove the bookmark if it is registered
            workspaceFavoriteRepository.deleteByUsernameAndWorkspaceId(memberId, workspace.getId());
          }
          break;
        default:
          break;
      }
    }

    workspaceRepository.save(workspace);

  }


  private boolean validateCollectionPatchForMember(CollectionPatch patch, Map<String, Role> roleMap) {

    // Required 'MemberId' property
    String memberId = patch.getValue("memberId");
    if (StringUtils.isEmpty(memberId)) {
      LOGGER.debug("memberId required.");
      return false;
    }

    // Checking valid member type.
    WorkspaceMember.MemberType memberType = null;
    if (patch.getValue("memberType") != null) {
      memberType = EnumUtils.getUpperCaseEnum(WorkspaceMember.MemberType.class, patch.getValue("memberType"));
      if (memberType == null) {
        LOGGER.debug("Invalid memberType(user/group).");
        return false;
      }

      DirectoryProfile profile = cachedUserService.findProfileByMemberType(memberId, memberType);
      if (profile == null) {
        LOGGER.debug("Unknown member {}({}).", memberId, memberType);
        return false;
      }
    }

    // Required 'role' property, checking valid role name.
    String roleName = patch.getValue("role");
    if (StringUtils.isNotEmpty(roleName)) {
      if (!roleMap.containsKey(roleName)) {
        LOGGER.debug("Unknown role name {}.", roleName);
        return false;
      }
    }

    return true;
  }

  /**
   * Transfer the role of Member and Group containing Member in the workspace (for Projection)
   */
  public List<WorkspaceMember> myRoles(String workspaceId, String ownerId) {

    String currentUser = AuthUtils.getAuthUserName();

    if (currentUser.equals(ownerId)) {
      return null;
    }

    // Check Membership in Workspace and Get Role Name
    List<String> joinedIds = groupService.getJoinedGroups(currentUser).stream()
                                         .map(Group::getName)
                                         .collect(Collectors.toList());
    joinedIds.add(currentUser);

    List<WorkspaceMember> members = workspaceMemberRepository.findByWorkspaceIdAndMemberIds(workspaceId, joinedIds);

    return members;

  }

  /**
   * Get a list of workspace permissions (for Projection)
   *
   * @return Permission list
   */
  public Set<String> getPermissions(Workspace workspace) {

    String currentUser = AuthUtils.getAuthUserName();
    if (currentUser.equals(workspace.getOwnerId())) {
      return WorkspacePermissions.allPermissions();
    }

    List<String> joinedIds = groupService.getJoinedGroups(currentUser).stream()
                                         .map(Group::getId)
                                         .collect(Collectors.toList());
    joinedIds.add(currentUser);

    List<String> roleNames = workspaceMemberRepository.findRoleNameByMemberIdsAndWorkspaceId(joinedIds, workspace.getId());

    if (CollectionUtils.isEmpty(roleNames)) {
      return Sets.newHashSet();
    }

    if (CollectionUtils.isEmpty(workspace.getRoleSets())) {
      workspace.addRoleSet(roleSetService.getDefaultRoleSet());
    }

    return roleSetRepository.getPermissionsByRoleSetAndRoleName(workspace.getRoleSets(), roleNames);
  }

  /**
   * Add RoleSet in Workspace
   *
   * @return Permission List
   */
  @Transactional
  public void addRoleSet(Workspace workspace, String roleSetName) {

    RoleSet addRoleSet = roleSetRepository.findByName(roleSetName);
    if (addRoleSet == null) {
      throw new ResourceNotFoundException(roleSetName);
    }

    List<RoleSet> roleSets = workspace.getRoleSets();

    if (roleSets.contains(addRoleSet)) {
      throw new IllegalArgumentException("Already added roleset.");
    }

    roleSets.add(addRoleSet);

    workspaceRepository.save(workspace);
  }

  /**
   * Change RoleSet in Workspace
   *
   * @return Permission List
   */
  @Transactional
  public void changeRoleSet(Workspace workspace, String from, String to, String defaultRoleName, Map<String, String> mapper) {

    RoleSet fromRoleSet = null;
    RoleSet defaultRoleSet = roleSetService.getDefaultRoleSet();
    // If no RoleSet exists, check if fromRoleSet is the default rolset
    List<RoleSet> roleSets = workspace.getRoleSets();
    if (CollectionUtils.isEmpty(roleSets)) {
      if (defaultRoleSet.getName().equals(from)) {
        fromRoleSet = defaultRoleSet;
      } else {
        throw new BadRequestException("Invalid fromRoleSet name.");
      }
    } else {
      fromRoleSet = roleSets.stream()
                            .filter(roleSet -> from.equals(roleSet.getName()))
                            .findFirst()
                            .orElseThrow(() -> new BadRequestException("fromRoleSet(" + from + ") not found in lined workspace"));
    }

    RoleSet toRoleSet = roleSetRepository.findByName(to);
    if (fromRoleSet == null) {
      throw new BadRequestException("toRoleSet(" + to + ") not found.");
    }

    workspace.removeRoleSet(fromRoleSet);
    workspace.addRoleSet(toRoleSet);

    workspaceRepository.saveAndFlush(workspace);

    // Updating workspace member role via mapper
    Role toDefaultRole = getDefaultRole(defaultRoleName, Lists.newArrayList(toRoleSet));

    if (MapUtils.isEmpty(mapper)) { // If there is no mapper information, change to default role
      for (Role role : fromRoleSet.getRoles()) {
        workspaceMemberRepository.updateMemberRoleInWorkspace(workspace, role.getName(), toDefaultRole.getName());
      }
    } else {

      List<String> fromRoleNames = fromRoleSet.getRoleNames();
      List<String> toRoleNames = toRoleSet.getRoleNames();
      String toDefaultRoleName = toDefaultRole.getName();

      for (String fromRoleName : mapper.keySet()) {
        if (!fromRoleNames.contains(fromRoleName)) {
          continue;
        }

        // If toRoleName does not exist, change to default RoleName
        String toRoleName = mapper.get(fromRoleName);
        if (!toRoleNames.contains(toRoleName)) {
          toRoleName = toDefaultRoleName;
        }

        workspaceMemberRepository.updateMemberRoleInWorkspace(workspace, fromRoleName, toRoleName);

        fromRoleNames.remove(fromRoleName);
      }

      if (!fromRoleNames.isEmpty()) {
        // Bulk change member roles of the not included from RoleSet to to RoleSet default Role Name
        workspaceMemberRepository.updateMultiMemberRoleInWorkspace(workspace, fromRoleSet.getRoleNames(), toDefaultRoleName);
      }

    }
  }

  /**
   * Delete RoleSet in Workspace
   *
   * @return Permission List
   */
  @Transactional
  public void deleteRoleSet(Workspace workspace, String roleSetName, String defaultRoleName) {

    RoleSet deleteRoleSet = roleSetRepository.findByName(roleSetName);
    if (deleteRoleSet == null) {
      throw new ResourceNotFoundException(roleSetName);
    }

    List<RoleSet> roleSets = workspace.getRoleSets();

    if (roleSets.contains(deleteRoleSet)) {
      roleSets.remove(deleteRoleSet);
    } else {
      throw new IllegalArgumentException("Already added roleset.");
    }

    // Specify if deleted and no RoleSets exist
    if (roleSets.size() == 0) {
      roleSets.add(roleSetService.getDefaultRoleSet());
    }

    workspaceRepository.saveAndFlush(workspace);

    // Update specified Workspace Member Role in RoleSet to be deleted by Default Role
    Role defaultRole = getDefaultRole(defaultRoleName, roleSets);

    List<String> targetRoleNames = deleteRoleSet.getRoles().stream()
                                                .map(role -> role.getName())
                                                .collect(Collectors.toList());

    workspaceMemberRepository.updateMultiMemberRoleInWorkspace(workspace, targetRoleNames, defaultRole.getName());

  }

  private Role getDefaultRole(String defaultRoleName, List<RoleSet> roleSets) {

    final List<Role> roles = Lists.newArrayList();
    roleSets.forEach(roleSet -> {
      roles.addAll(roleSet.getRoles());
    });

    Role defaultRole = roles.stream()
                            .filter(r -> r.equals(defaultRoleName))
                            .findFirst().orElse(null);

    if (defaultRole == null) {
      defaultRole = roles.stream()
                         .filter(r -> r.getDefaultRole())
                         .findFirst().orElseThrow(() -> new IllegalArgumentException("default role not found"));
    }

    return defaultRole;
  }

  public Map<String, Object> getStatistics(Workspace workspace,
                                           TimeFieldFormat.TimeUnit timeUnit,
                                           DateTime from, DateTime to, boolean accumulated) {
    Map<String, Long> countBookByType = workspaceRepository.countByBookType(workspace);
    Double avgDashboardByWorkBook = workspaceRepository.avgDashBoardByWorkBook(workspace);
    Long countFavoriteWorkspace = workspaceFavoriteRepository.countDistinctByWorkspaceId(workspace.getId());

    // Obtaining connection history statistics
    Map<String, Long> viewCountByTime = activityStreamService
        .getWorkspaceViewByDateTime(workspace.getId(), timeUnit, from, to);

    // Add cumulative option
    List<Long> viewCountValues;
    if(accumulated) {
      viewCountValues = Lists.newArrayList();
      Long tempValue = 0L;
      for (Long viewCountValue : viewCountByTime.values()) {
        tempValue += viewCountValue;
        viewCountValues.add(tempValue);
      }
    } else {
      viewCountValues = Lists.newArrayList(viewCountByTime.values());
    }

//    MatrixResponse<String, Long> matrix = new MatrixResponse<>(Lists.newArrayList(viewCountByTime.keySet()),
//                                                               Lists.newArrayList(new MatrixResponse.Column("Count", viewCountValues)));
    MatrixResponse<String, Long> matrix = new MatrixResponse<>();
    Map<String, Object> statMap = Maps.newLinkedHashMap();
    statMap.put("countBookByType", countBookByType);
    statMap.put("avgDashboardByWorkBook", avgDashboardByWorkBook);
    statMap.put("countFavoriteWorkspace", countFavoriteWorkspace);
    statMap.put("viewCountByTime", matrix);

    return statMap;
  }

  public Page<Workspace> getPublicWorkspaces(Boolean onlyFavorite,
                                             Boolean myWorkspace,
                                             Boolean published,
                                             String nameContains,
                                             Pageable pageable){
    String username = AuthUtils.getAuthUserName();

    Predicate publicWorkspacePredicate = getPublicWorkspacePredicate(onlyFavorite, myWorkspace, published, nameContains);

    // Result query
    Page<Workspace> publicWorkspaces = workspaceRepository.findAll(publicWorkspacePredicate, pageable);

    // Favorite Whether Processing
    if (onlyFavorite) {
      publicWorkspaces.forEach(publicWorkspace -> publicWorkspace.setFavorite(true));
    } else {
      Set<String> favoriteWorkspaceIds = workspaceFavoriteRepository.findWorkspaceIdByUsername(username);
      for (Workspace publicWorkspace : publicWorkspaces) {
        if (favoriteWorkspaceIds.contains(publicWorkspace.getId())) {
          publicWorkspace.setFavorite(true);
        } else {
          publicWorkspace.setFavorite(false);
        }
      }
    }

    return publicWorkspaces;
  }

  public List<Workspace> getPublicWorkspaces(Boolean onlyFavorite,
                                             Boolean myWorkspace,
                                             Boolean published,
                                             String nameContains){
    String username = AuthUtils.getAuthUserName();

    Predicate publicWorkspacePredicate = getPublicWorkspacePredicate(onlyFavorite, myWorkspace, published, nameContains);

    // Result query
    List<Workspace> publicWorkspaces = (List) workspaceRepository.findAll(publicWorkspacePredicate);

    // Favorite Whether Processing
    if (onlyFavorite) {
      publicWorkspaces.forEach(publicWorkspace -> publicWorkspace.setFavorite(true));
    } else {
      Set<String> favoriteWorkspaceIds = workspaceFavoriteRepository.findWorkspaceIdByUsername(username);
      for (Workspace publicWorkspace : publicWorkspaces) {
        if (favoriteWorkspaceIds.contains(publicWorkspace.getId())) {
          publicWorkspace.setFavorite(true);
        } else {
          publicWorkspace.setFavorite(false);
        }
      }
    }

    return publicWorkspaces;
  }

  public Map<String, Long> countOfBookByType(Workspace workspace) {
    Map<String, Long> countOfBook = workspaceRepository.countByBookType(workspace);

    Map<String, Long> result = Maps.newHashMap();
    result.put("folder", countOfBook.get("folder") == null ? 0L : countOfBook.get("folder"));
    result.put("workBook", countOfBook.get("workbook") == null ? 0L : countOfBook.get("workbook"));
    result.put("workBench", countOfBook.get("workbench") == null ? 0L : countOfBook.get("workbench"));
    result.put("notebook", countOfBook.get("notebook") == null ? 0L : countOfBook.get("notebook"));

    return result;
  }

  public Map<String, Long> countByMemberType(Workspace workspace, boolean includePublished) {
    Map<WorkspaceMember.MemberType, Long> countOfMember = Maps.newHashMap();

    if (Workspace.PublicType.SHARED.equals(workspace.getPublicType()) && (includePublished || !workspace.getPublished())) {
      countOfMember = workspaceRepository.countByMemberType(workspace);
    }

    Map<String, Long> result = Maps.newHashMap();
    result.put("group", countOfMember.get(WorkspaceMember.MemberType.GROUP) == null ? 0L : countOfMember.get(WorkspaceMember.MemberType.GROUP));
    result.put("user", countOfMember.get(WorkspaceMember.MemberType.USER) == null ? 0L : countOfMember.get(WorkspaceMember.MemberType.USER));

    return result;
  }

  private Predicate getPublicWorkspacePredicate(Boolean onlyFavorite,
                                                Boolean myWorkspace,
                                                Boolean published,
                                                String nameContains){
    String username = AuthUtils.getAuthUserName();

    List<String> targets = Lists.newArrayList(username);
    targets.addAll(groupRepository.findGroupIdsByMemberId(username));

    BooleanBuilder builder = new BooleanBuilder();
    QWorkspace workspace = QWorkspace.workspace;
    QWorkspaceFavorite workspaceFavorite = QWorkspaceFavorite.workspaceFavorite;

    builder.and(workspace.publicType.eq(SHARED));

    Predicate pOwnerEq = null;
    if(myWorkspace != null) {
      pOwnerEq = myWorkspace ? workspace.ownerId.eq(username) : workspace.ownerId.ne(username);
    }
    Predicate pPublished = null;
    if(published != null) {
      pPublished = published ? builder.and(workspace.published.isTrue())
              : builder.andAnyOf(workspace.published.isNull(), workspace.published.isFalse());
    }

    if (onlyFavorite) {
      BooleanExpression favorite = workspace.id
              .in(JPAExpressions.select(workspace.id)
                      .from(workspace, workspaceFavorite)
                      .where(workspace.id.eq(workspaceFavorite.workspaceId).and(workspaceFavorite.username.eq(username))));
      builder.and(favorite);

      if (myWorkspace != null) {
        builder.and(pOwnerEq);
      }

      if (published != null) {
        builder.and(pPublished);
      }
    } else {

      BooleanExpression memberIn = workspace.id
              .in(JPAExpressions.select(workspace.id)
                      .from(workspace)
                      .innerJoin(workspace.members)
                      .where(workspace.members.any().memberId.in(targets)));
      if (myWorkspace != null) {
        if(published == null) {
          if(myWorkspace) {
            builder.and(pOwnerEq);
          } else {
            builder.andAnyOf(memberIn, workspace.published.isTrue());
          }
        } else {
          builder.and(pOwnerEq);
          builder.and(pPublished);

          if(!myWorkspace) {
            builder.and(memberIn);
          }
        }
      } else {
        if (published == null) {
          builder.andAnyOf(memberIn, workspace.ownerId.eq(username), workspace.published.isTrue());
        } else {
          builder.and(pPublished);
        }

      }
    }

    if (StringUtils.isNotEmpty(nameContains)) {
      builder = builder.and(workspace.name.containsIgnoreCase(nameContains));
    }

    return builder;
  }
}

