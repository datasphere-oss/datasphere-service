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

import com.google.common.collect.Lists;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URI;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.datasphere.server.common.entity.SearchParamValidator;
import com.datasphere.server.common.exception.BadRequestException;
import com.datasphere.server.common.exception.ResourceNotFoundException;
import com.datasphere.server.domain.CollectionPatch;
import com.datasphere.server.domain.dataconnection.DataConnection;
import com.datasphere.server.domain.dataconnection.DataConnectionPredicate;
import com.datasphere.server.domain.dataconnection.DataConnectionRepository;
import com.datasphere.server.datasource.DataSource;
import com.datasphere.server.datasource.DataSource.ConnectionType;
import com.datasphere.server.datasource.DataSource.DataSourceType;
import com.datasphere.server.datasource.DataSourcePredicate;
import com.datasphere.server.datasource.DataSourceRepository;
import com.datasphere.server.domain.notebook.NoteBookConnectorPredicate;
import com.datasphere.server.domain.notebook.NotebookConnector;
import com.datasphere.server.domain.notebook.NotebookConnectorRepository;
import com.datasphere.server.domain.notebook.NotebookModel;
import com.datasphere.server.domain.notebook.NotebookModelRepository;
import com.datasphere.server.domain.notebook.QNotebook;
import com.datasphere.server.domain.notebook.QNotebookModel;
import com.datasphere.server.domain.notebook.connector.HttpRepository;
import com.datasphere.server.domain.user.CachedUserService;
import com.datasphere.server.domain.user.User;
import com.datasphere.server.domain.user.group.Group;
import com.datasphere.server.domain.user.group.GroupRepository;
import com.datasphere.server.domain.user.role.RoleService;
import com.datasphere.server.domain.user.role.RoleSet;
import com.datasphere.server.domain.user.role.RoleSetService;
import com.datasphere.server.domain.workbook.DashBoard;
import com.datasphere.server.domain.workbook.DashBoardPredicate;
import com.datasphere.server.domain.workbook.DashboardRepository;
import com.datasphere.server.domain.workbook.WorkBook;
import com.datasphere.server.domain.workbook.configurations.format.TimeFieldFormat;
import com.datasphere.server.util.AuthUtils;
import com.datasphere.server.util.ProjectionUtils;

import static com.datasphere.server.domain.workspace.Workspace.PublicType.PRIVATE;
import static java.util.stream.Collectors.toList;


/**
 *
 */
@RepositoryRestController
public class WorkspaceController {

  private static Logger LOGGER = LoggerFactory.getLogger(WorkspaceController.class);

  @Autowired
  CachedUserService cachedUserService;

  @Autowired
  WorkspaceService workspaceService;

  @Autowired
  RoleService roleService;

  @Autowired
  RoleSetService roleSetService;

  @Autowired
  DataSourceRepository dataSourceRepository;

  @Autowired
  WorkspaceRepository workspaceRepository;

  @Autowired
  BookRepository bookRepository;

  @Autowired
  DashboardRepository dashBoardRepository;

  @Autowired
  WorkspaceMemberRepository workspaceMemberRepository;

  @Autowired
  WorkspaceFavoriteRepository workspaceFavoriteRepository;
  // 群组容器
  @Autowired
  GroupRepository groupRepository;

  @Autowired
  WorkspacePagedResourcesAssembler pagedResourcesAssembler;
  // 模型容器
  @Autowired
  NotebookModelRepository notebookModelRepository;

  @Autowired
  NotebookConnectorRepository notebookConnectorRepository;

  @Autowired
  HttpRepository httpRepository;

  @Autowired
  DataConnectionRepository dataConnectionRepository;

  @Autowired
  ProjectionFactory projectionFactory;

  WorkspaceProjections workspaceProjections = new WorkspaceProjections();

  public WorkspaceController() {
  }

  /**
   *
   * @param resourceAssembler
   * @return
   */
  @RequestMapping(path = "/workspaces/my", method = RequestMethod.GET)
  public @ResponseBody
  ResponseEntity<?> findMyWorkspace(PersistentEntityResourceAssembler resourceAssembler) {

    Workspace myWorkspace = workspaceRepository.findPrivateWorkspaceByOwnerId(AuthUtils.getAuthUserName());

    if (myWorkspace == null) {
      myWorkspace = new Workspace();
      myWorkspace.setId("GUEST_WORKSPACE");
      myWorkspace.setName("Guest Workspace");
      myWorkspace.setOwnerId(AuthUtils.getAuthUserName());
    }

    return ResponseEntity.ok(resourceAssembler.toResource(myWorkspace));
  }

  /**
   *
   * @param resourceAssembler
   * @return
   */
  @RequestMapping(path = "/workspaces/my/all", method = RequestMethod.GET)
  public @ResponseBody
  ResponseEntity<?> findMyWorkspaceList(PersistentEntityResourceAssembler resourceAssembler) {

    String username = AuthUtils.getAuthUserName();
    List<String> targets = Lists.newArrayList(username);
    targets.addAll(groupRepository.findGroupIdsByMemberId(username));

    List<Workspace> myWorkspaces = workspaceRepository.findMyWorkspaces(username, targets);

    List<PersistentEntityResource> resources = myWorkspaces.stream()
                                                           .map((workspace) -> resourceAssembler.toResource(workspace))
                                                           .collect(toList());

    return ResponseEntity.ok(new Resources(resources));
  }

  /**
   *
   * @param onlyFavorite
   * @param myWorkspace
   * @param nameContains
   * @param pageable
   * @param projection
   * @param resourceAssembler
   * @return
   * */
  @RequestMapping(path = "/workspaces/my/public", method = RequestMethod.GET)
  public @ResponseBody
  ResponseEntity<?> findPublicWorkspaceList(
      @RequestParam(required = false) boolean onlyFavorite,
      @RequestParam(required = false) Boolean myWorkspace,
      @RequestParam(required = false) Boolean published,
      @RequestParam(required = false) String nameContains,
      @RequestParam(value = "projection", required = false, defaultValue = "default") String projection,
      Pageable pageable, PersistentEntityResourceAssembler resourceAssembler) {

    Page<Workspace> publicWorkspaces = workspaceService.getPublicWorkspaces(
        onlyFavorite, myWorkspace, published, nameContains, pageable);

    return ResponseEntity.ok(this.pagedResourcesAssembler.toResource(ProjectionUtils.toPageResource(projectionFactory,
                                                                                                    workspaceProjections.getProjectionByName(projection),
                                                                                                    publicWorkspaces)));
  }
  // 查找工作空间
  @RequestMapping(path = "/workspaces", method = RequestMethod.GET)
  public @ResponseBody
  ResponseEntity<?> findWorkspaces(
      @RequestParam(required = false) String publicType,
      @RequestParam(required = false) String linkedType,
      @RequestParam(required = false) String linkedId,
      @RequestParam(required = false) boolean onlyLinked,
      @RequestParam(required = false) String nameContains,
      Pageable pageable, PersistentEntityResourceAssembler resourceAssembler) {

    Workspace.PublicType type = SearchParamValidator.enumUpperValue(Workspace.PublicType.class,
                                                                    publicType, "publicType");

    if (onlyLinked) {
      SearchParamValidator.checkNull(linkedType, "linkType");
      SearchParamValidator.checkNull(linkedId, "linkedId");

      Page<Workspace> workspaces = workspaceRepository
          .findAll(WorkspacePredicate.searchPublicTypeAndNameContainsAndLink(type, nameContains, linkedType, linkedId), pageable);

      long totalWorkspaces = workspaceRepository.count(WorkspacePredicate.searchPublicTypeAndNameContains(type, nameContains));

      workspaces.getContent().forEach(workspace -> workspace.setLinked(true));

      return ResponseEntity.ok(this.pagedResourcesAssembler
                                   .toWorkspaceResource(workspaces,
                                                        new WorkspacePagedResources
                                                            .WorkspaceMetadata(workspaces.getTotalElements(),
                                                                               totalWorkspaces),
                                                        resourceAssembler));

    } else {
      Page<Workspace> workspaces = workspaceRepository
          .findAll(WorkspacePredicate.searchPublicTypeAndNameContainsAndActive(type, nameContains, true), pageable);

      // Find Workspaces Connected to Specific Data Sources
      List<String> linkedWorkspaceIds = null;

      if (StringUtils.isNotEmpty(linkedId)) {

        SearchParamValidator.checkNull(linkedType, "linkType");

        switch (linkedType.toUpperCase()) {
          case "DATASOURCE":
            linkedWorkspaceIds = workspaceRepository.findWorkspaceIdByLinkedDataSource(linkedId, type, nameContains);
            break;
          case "CONNECTION":
            linkedWorkspaceIds = workspaceRepository.findWorkspaceIdByLinkedConnection(linkedId, type, nameContains);
            break;
          case "CONNECTOR":
            linkedWorkspaceIds = Lists.newArrayList();
            break;
          default:
            throw new BadRequestException("Not supported type of link. choose one of datasource, connection, connector");
        }

        for (Workspace workspace : workspaces.getContent()) {
          if (linkedWorkspaceIds.contains(workspace.getId())) {
            workspace.setLinked(true);
          } else {
            workspace.setLinked(false);
          }
        }
      } else {
        workspaces.forEach(workspace -> workspace.setLinked(false));
      }
      return ResponseEntity.ok(this.pagedResourcesAssembler
                                   .toWorkspaceResource(workspaces,
                                                        new WorkspacePagedResources
                                                            .WorkspaceMetadata(linkedWorkspaceIds == null ? 0 : linkedWorkspaceIds.size(),
                                                                               workspaces.getTotalElements()),
                                                        resourceAssembler));
    }
  }
  // 管理者工作空间
  @RequestMapping(path = "/workspaces/byadmin", method = RequestMethod.GET)
  public @ResponseBody
  ResponseEntity<?> findWorkspacesByAdmin(
      @RequestParam(value = "username", required = false) String username,
      @RequestParam(value = "onlyOwner", required = false) Boolean onlyOwner,
      @RequestParam(value = "publicType", required = false) String publicType,
      @RequestParam(value = "published", required = false) Boolean published,
      @RequestParam(value = "active", required = false) Boolean active,
      @RequestParam(value = "nameContains", required = false) String nameContains,
      @RequestParam(value = "searchDateBy", required = false) String searchDateBy,
      @RequestParam(value = "from", required = false)
      @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) DateTime from,
      @RequestParam(value = "to", required = false)
      @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) DateTime to,
      Pageable pageable, PersistentEntityResourceAssembler resourceAssembler) {

    List<String> memberIds = Lists.newArrayList();
    if (StringUtils.isNotEmpty(username)) {
      memberIds.add(username);
      memberIds.addAll(groupRepository.findGroupIdsByMemberId(username));
    }

    Workspace.PublicType type = SearchParamValidator.enumUpperValue(Workspace.PublicType.class,
                                                                    publicType, "publicType");

    Page<Workspace> workspaces = workspaceRepository
        .findAll(WorkspacePredicate.searchWorkspaceList(username, memberIds, onlyOwner,
                                                        published, active,
                                                        type, nameContains,
                                                        searchDateBy, from, to),
                 pageable);

    return ResponseEntity.ok(this.pagedResourcesAssembler.toResource(workspaces, resourceAssembler));
  }


  /**
   *
   * @param id
   * @param pageable
   * @param resourceAssembler
   * @return notebook model
   */
  @RequestMapping(path = "/workspaces/{id}/nbmodels", method = RequestMethod.GET)
  public @ResponseBody
  ResponseEntity<?> findNotebookModelsInWorkspace(@PathVariable("id") String id,
                                                  Pageable pageable,
                                                  PersistentEntityResourceAssembler resourceAssembler) {
    QNotebookModel notebookModel = QNotebookModel.notebookModel;
    QNotebook notebook = QNotebook.notebook;

    BooleanBuilder builder = new BooleanBuilder();
    BooleanExpression workspaceContains = notebookModel.id
        .in(JPAExpressions.select(notebookModel.id)
                          .from(notebookModel, notebook)
                          .where(notebookModel.notebook.id.eq(notebook.id)
                                                          .and(notebook.workspace.id.eq(id))
                                                          .and(notebookModel.statusType.eq(NotebookModel.StatusType.APPROVAL))));
    builder.and(workspaceContains);

    Page<NotebookModel> models = notebookModelRepository.findAll(builder, pageable);

    return ResponseEntity.ok(pagedResourcesAssembler.toResource(models, resourceAssembler));
  }

  /**
   *
   *
   * @param workspaceId
   * @param type
   * @param connType
   * @param onlyPublic
   * @param status
   * @param nameContains
   * @param pageable
   * @param resourceAssembler
   * @return
   */
  @RequestMapping(path = "/workspaces/{workspaceId}/datasources", method = RequestMethod.GET)
  public @ResponseBody
  ResponseEntity<?> findDataSourcesInWorkspace(@PathVariable("workspaceId") String workspaceId,
                                               @RequestParam(required = false) String type,
                                               @RequestParam(required = false) String connType,
                                               @RequestParam(required = false) Boolean onlyPublic,
                                               @RequestParam(required = false) List<String> status,
                                               @RequestParam(required = false) String nameContains,
                                               Pageable pageable,
                                               PersistentEntityResourceAssembler resourceAssembler) {

    Workspace workspace = workspaceRepository.findById(workspaceId).get();
    if (workspace == null) {
      return ResponseEntity.notFound().build();
    }

    List<DataSource.Status> statuses = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(status)) {
      for (String aStatus : status) {
        statuses.add(SearchParamValidator.enumUpperValue(DataSource.Status.class, aStatus, "status"));
      }
    }

    // Search by Source Type
    DataSourceType sourceType = null;
    if (StringUtils.isNotEmpty(type)) {
      sourceType = SearchParamValidator.enumUpperValue(DataSourceType.class, type, "type");
    }

    // Search by Connection Type
    ConnectionType connectionType = null;
    if (StringUtils.isNotEmpty(connType)) {
      connectionType = SearchParamValidator.enumUpperValue(ConnectionType.class, connType, "connType");
    }

    // Default sort condition settings
    if (pageable.getSort() == null || !pageable.getSort().iterator().hasNext()) {
      pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                                 new Sort(Sort.Direction.ASC, "name"));
    }

    Page<DataSource> dataSources = dataSourceRepository.findAll(
        DataSourcePredicate.searchDatasourcesInWorkspace(
            workspace, sourceType, connectionType, statuses, onlyPublic, nameContains),
        pageable);

    return ResponseEntity.ok(pagedResourcesAssembler.toResource(dataSources, resourceAssembler));
  }

  @RequestMapping(path = "/workspaces/{id}/connections", method = RequestMethod.GET)
  public @ResponseBody
  ResponseEntity<?> findConnectionsInWorkspace(@PathVariable("id") String id,
                                               @RequestParam(value = "name", required = false) String name,
                                               @RequestParam(value = "implementor", required = false) String implementor,
                                               @RequestParam(value = "authenticationType", required = false) String authenticationType,
                                               Pageable pageable) {

    LOGGER.debug("name = {}", name);
    LOGGER.debug("implementor = {}", implementor);
    LOGGER.debug("authenticationType = {}", authenticationType);
    LOGGER.debug("workspaceId = {}", id);
    LOGGER.debug("pageable = {}", pageable);
    // 获取认证类型
    DataConnection.AuthenticationType authenticationTypeValue = null;
    if (StringUtils.isNotEmpty(authenticationType)) {
      authenticationTypeValue = SearchParamValidator
          .enumUpperValue(DataConnection.AuthenticationType.class, authenticationType, "authenticationType");
    }

    // Get Predicate
    Predicate searchPredicated = DataConnectionPredicate
        .searchListForWorkspace(name, implementor, authenticationTypeValue, id);

    // Default sort condition settings
    if (pageable.getSort() == null || !pageable.getSort().iterator().hasNext()) {
      pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                                 new Sort(Sort.Direction.ASC, "name"));
    }
    Page<DataConnection> connections = dataConnectionRepository.findAll(searchPredicated, pageable);

    return ResponseEntity.ok(this.pagedResourcesAssembler.toResource(connections));
  }

  /**
   * 通过工作空间查找连接器
   * @param workspaceId
   * @param type
   */
  @RequestMapping(path = "/workspaces/{workspaceId}/connectors", method = RequestMethod.GET)
  public @ResponseBody
  ResponseEntity<?> findConnectorsWithLinkedWorkspace(@PathVariable("workspaceId") String workspaceId,
                                                      @RequestParam(value = "type", required = false) String type,
                                                      Pageable pageable,
                                                      PersistentEntityResourceAssembler resourceAssembler) {
	
    Workspace workspace = workspaceRepository.findById(workspaceId).get();
    if (workspace == null) {
      throw new ResourceNotFoundException(workspaceId);
    }

    // Default sort condition settings
    if (pageable.getSort() == null || !pageable.getSort().iterator().hasNext()) {
      pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                                 new Sort(Sort.Direction.ASC, "name"));
    }

    Predicate searchPredicated = NoteBookConnectorPredicate.searchAvailableConnectorInWorkspace(workspace, type);

    Page<NotebookConnector> connectors = notebookConnectorRepository.findAll(searchPredicated, pageable);

    return ResponseEntity.ok(this.pagedResourcesAssembler.toResource(connectors, resourceAssembler));
  }

  /**
   * 在工作空间中添加连接器
   * @param wid  workspace Id
   * @param cids notebook-connectors Id
   */
  @RequestMapping(path = "/workspaces/{wid}/connectors/{cids}", method = RequestMethod.POST)
  public @ResponseBody
  ResponseEntity<?> addConnectorsInWorkspace(
      @PathVariable("wid") String wid,
      @PathVariable("cids") List<String> cids) {

    Workspace workspace = workspaceRepository.findById(wid).get();
    if (workspace == null) {
      return ResponseEntity.notFound().build();
    }
    workspace.setConnectors(Collections.emptySet());
    Set<NotebookConnector> updateConnectors = new HashSet<>();
    for (String cid : cids) {
      NotebookConnector connector = notebookConnectorRepository.findById(cid).get();
      if (connector == null) {
        throw new RuntimeException("Connector not found");
      }
      updateConnectors.add(connector);
      if (connector.getType().equals("jupyter")) {
        connector.setHttpRepository(httpRepository);
        connector.createDirectory(workspace.getId());
      }
    }
    workspace.setConnectors(updateConnectors);
    workspaceRepository.saveAndFlush(workspace);

    return ResponseEntity.noContent().build();
  }

  @RequestMapping(path = "/workspaces/{workspaceId}/books/{bookId}", method = RequestMethod.GET)
  public @ResponseBody
  ResponseEntity<?> findBooksInWorkspace(@PathVariable("workspaceId") String workspaceId,
                                         @PathVariable("bookId") String bookId,
                                         @RequestParam(required = false) String bookType,
                                         PersistentEntityResourceAssembler resourceAssembler) {

    if (StringUtils.isNotEmpty(bookType) && !Book.SEARCHABLE_BOOKS.contains(bookType.toLowerCase())) {
      throw new IllegalArgumentException("Invalid widget type. choose " + Book.SEARCHABLE_BOOKS);
    }

    Workspace workspace = workspaceRepository.findById(workspaceId).get();
    if (workspace == null) {
      return ResponseEntity.notFound().build();
    }

    if ("ROOT".equalsIgnoreCase(bookId)) {
      workspace.setBookType(bookType); //To search by type
      return ResponseEntity.ok(resourceAssembler.toResource(workspace));
    }

    Book book = bookRepository.findById(bookId).get();
    if (book == null) {
      throw new ResourceNotFoundException(bookId);
    }
    book.setBookType(bookType); // To search by type


    return ResponseEntity.ok(resourceAssembler.toResource(book));
  }
  // 找到仪表盘
  @RequestMapping(path = "/workspaces/{workspaceId}/dashboards", method = RequestMethod.GET)
  public @ResponseBody
  ResponseEntity<?> findDashBoardsInWorkspace(@PathVariable("workspaceId") String workspaceId,
                                              @RequestParam(required = false) String nameContains,
                                              Pageable pageable,
                                              PersistentEntityResourceAssembler resourceAssembler) {

    Workspace workspace = workspaceRepository.findById(workspaceId).get();
    if (workspace == null) {
      throw new ResourceNotFoundException(workspaceId);
    }

    Page<DashBoard> dashBoards = dashBoardRepository.findAll(
        DashBoardPredicate.searchListInWorkspace(workspaceId, nameContains), pageable);

    return ResponseEntity.ok(pagedResourcesAssembler.toResource(dashBoards, resourceAssembler));
  }
  // 工作空间统计信息
  @RequestMapping(path = "/workspaces/{workspaceId}/statistics", method = RequestMethod.GET)
  public @ResponseBody
  ResponseEntity<?> findWorkspaceStatistics(@PathVariable("workspaceId") String workspaceId,
                                            @RequestParam(value = "timeUnit", required = false) String timeUnit,
                                            @RequestParam(value = "accumulated", required = false, defaultValue = "true") Boolean accumulated,
                                            @RequestParam(value = "from", required = false)
                                            @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) DateTime from,
                                            @RequestParam(value = "to", required = false)
                                            @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) DateTime to) {

    Workspace workspace = workspaceRepository.findById(workspaceId).get();
    if (workspace == null) {
      throw new ResourceNotFoundException(workspaceId);
    }

    TimeFieldFormat.TimeUnit unit = null;
    if (StringUtils.isNotEmpty(timeUnit)) {
      unit = SearchParamValidator
          .enumUpperValue(TimeFieldFormat.TimeUnit.class, timeUnit, "timeUnit");
    } else {
      unit = TimeFieldFormat.TimeUnit.DAY;
    }

    DateTime now = DateTime.now();
    if (from == null) {
      from = now.minusMonths(1);
    }

    if (to == null) {
      to = now;
    }

    return ResponseEntity.ok(workspaceService.getStatistics(workspace, unit, from, to, accumulated));
  }

  /**
   * @param bookId
   * @param excludes
   * @param publicType
   * @param nameContains
   * @param pageable
   * @param resourceAssembler
   * @return
   */
  @RequestMapping(path = "/workspaces/import/books/{bookId}/available", method = RequestMethod.GET)
  public @ResponseBody
  ResponseEntity<?> findWorkspacesToMoveOrCopy(@PathVariable("bookId") String bookId,
                                               @RequestParam(required = false) List<String> excludes,
                                               @RequestParam(required = false) String publicType,
                                               @RequestParam(required = false) String nameContains,
                                               Pageable pageable,
                                               PersistentEntityResourceAssembler resourceAssembler) {

    Book book = bookRepository.findById(bookId).get();
    if (book == null || !(book instanceof WorkBook)) {
      throw new ResourceNotFoundException(bookId);
    }

    if (CollectionUtils.isEmpty(excludes)) {
      excludes = Lists.newArrayList(book.getWorkspace().getId());
    }

    Workspace.PublicType pubType = SearchParamValidator.enumUpperValue(Workspace.PublicType.class, publicType, "publicType");

    // Data sources that are connected to dashboards within your workbook (except public data sources)
    List<String> dataSourceIds;
    if (book instanceof WorkBook) {
      dataSourceIds = dataSourceRepository.findIdsByWorkbookInNotPublic(bookId);
    } else {
      dataSourceIds = Lists.newArrayList();
    }

    List<String> perms = WorkspacePermissions.editPermissions(book);

    String username = AuthUtils.getAuthUserName();
    List<String> targets = Lists.newArrayList(username);
    targets.addAll(groupRepository.findGroupIdsByMemberId(username));

    // List of workspaces accessible to the user
    List<String> joinedWorkspaceIds = workspaceRepository
        .findMyWorkspaceIdsByPermission(username, targets, perms.toArray(new String[perms.size()]));

    if (joinedWorkspaceIds.size() > 0 && CollectionUtils.isNotEmpty(excludes)) {
      joinedWorkspaceIds.removeAll(excludes);
    }

    Page<Workspace> results;
    if (CollectionUtils.isEmpty(dataSourceIds) && CollectionUtils.isEmpty(joinedWorkspaceIds)) {
      // Cases where both dataSourceIds and joinedWorkspaceIds are 0 do not require a query
      results = new PageImpl(Collections.emptyList(), pageable, 0);
    } else {
      results = workspaceRepository.findAll(
          WorkspacePredicate.searchWorkbookImportAvailable(dataSourceIds, joinedWorkspaceIds, pubType, nameContains), pageable);
    }

    return ResponseEntity.ok(pagedResourcesAssembler.toResource(results, resourceAssembler));
  }

  @RequestMapping(path = "/workspaces/members/refresh", method = RequestMethod.POST)
  public @ResponseBody
  ResponseEntity<?> refreshMembers() {

    int pageNum = 0;
    int size = 50;

    PageRequest pageRequest = null;
    Page<WorkspaceMember> page = null;
    do {
      pageRequest = new PageRequest(pageNum, size);
      List<WorkspaceMember> members = Lists.newArrayList();

      page = workspaceMemberRepository.findAll(pageRequest);

      for (WorkspaceMember member : page) {
        // Preset user / group information
        if (member.getMemberType() == WorkspaceMember.MemberType.GROUP) {
          Group group = cachedUserService.findGroup(member.getMemberId());
          if (group != null) member.setMemberName(group.getName());
        } else {
          User user = cachedUserService.findUser(member.getMemberId());
          if (user != null) member.setMemberName(user.getFullName());
        }
        members.add(member);
      }

      workspaceMemberRepository.saveAll(members);

      pageNum++;

    } while (page.hasNext());

    return ResponseEntity.noContent().build();
  }


  @RequestMapping(path = "/workspaces/{id}/members", method = RequestMethod.GET)
  public @ResponseBody
  ResponseEntity<?> findMemebersInWorkspace(@PathVariable("id") String id,
                                            @RequestParam(required = false, defaultValue = "all") String type,
                                            @RequestParam(required = false) String nameContains,
                                            @RequestParam(required = false) List<String> roles,
                                            Pageable pageable,
                                            PersistentEntityResourceAssembler resourceAssembler) {

    Workspace workspace = workspaceRepository.findById(id).get();
    if (workspace == null) {
      return ResponseEntity.notFound().build();
    }

    List<WorkspaceMember.MemberType> memberTypes = null;
    if ("all".equalsIgnoreCase(type)) {
      memberTypes = Lists.newArrayList(WorkspaceMember.MemberType.USER, WorkspaceMember.MemberType.GROUP);
    } else if ("user".equalsIgnoreCase(type)) {
      memberTypes = Lists.newArrayList(WorkspaceMember.MemberType.USER);
    } else if ("group".equalsIgnoreCase(type)) {
      memberTypes = Lists.newArrayList(WorkspaceMember.MemberType.GROUP);
    } else {
      throw new IllegalArgumentException("Unsupported value of 'type' property. choose [all, user, group]");
    }

    if (pageable.getSort() == null) {
      Sort sort = new Sort(Sort.Direction.ASC, "memberType", "memberName");
      pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
    }

    Page<WorkspaceMember> members = workspaceMemberRepository
        .findAll(WorkspaceMemberPredicate.searchWorkspaceMember(workspace, memberTypes, roles, nameContains), pageable);

    return ResponseEntity.ok(pagedResourcesAssembler.toResource(members, resourceAssembler));
  }

  /**
   *
   * @param id
   * @param patches
   * @return
   */
  @RequestMapping(path = "/workspaces/{id}/members", method = {RequestMethod.PATCH, RequestMethod.PUT})
  public @ResponseBody
  ResponseEntity<?> patchMemebersInWorkspace(
      @PathVariable("id") String id, @RequestBody List<CollectionPatch> patches) {

    Workspace workspace = workspaceRepository.findById(id).get();
    if (workspace == null) {
      return ResponseEntity.notFound().build();
    }

    workspaceService.updateMembers(workspace, patches);

    return ResponseEntity.noContent().build();
  }

  /**
   *
   * @param id
   * @param memberIds
   * @return
   */
  @RequestMapping(path = "/workspaces/{id}/members/{memberIds}", method = RequestMethod.DELETE)
  public @ResponseBody
  ResponseEntity<?> deleteMemebersInWorkspace(
      @PathVariable("id") String id, @PathVariable("memberId") List<String> memberIds) {

    if (workspaceRepository.findById(id) == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.noContent().build();
  }

  /**
   *
   * @param request
   * @param id
   * @return
   */
  @RequestMapping(path = "/workspaces/{id}/favorite", method = {RequestMethod.POST, RequestMethod.DELETE})
  public @ResponseBody
  ResponseEntity<?> saveFavoriteWorkspace(HttpServletRequest request, @PathVariable("id") String id) {

    if (workspaceRepository.findById(id) == null) {
      return ResponseEntity.notFound().build();
    }

    if ("POST".equals(request.getMethod())) {
      workspaceFavoriteRepository.save(new WorkspaceFavorite(AuthUtils.getAuthUserName(), id));
    } else if ("DELETE".equals(request.getMethod())) {
      workspaceFavoriteRepository.deleteByUsernameAndWorkspaceId(AuthUtils.getAuthUserName(), id);
    } else {
      throw new IllegalArgumentException("Not supported method.");
    }

    return ResponseEntity.noContent().build();
  }

  @RequestMapping(path = "/workspaces/{id}/delegate/{owner:.+}", method = RequestMethod.POST)
  public ResponseEntity<?> delegateWorkspace(@PathVariable("id") String workspaceId,
                                             @PathVariable("owner") String owner) {

    Workspace workspace = workspaceRepository.findById(workspaceId).get();
    if (workspace == null) {
      throw new ResourceNotFoundException(workspaceId);
    }

    final User user = cachedUserService.findUser(owner);
    if (user == null) {
      throw new BadRequestException("Invalid username of owner : " + owner);
    }
    if (!workspace.checkMemberExistByUserName(user)) {
      throw new BadRequestException("Invalid username of owner : " + owner);
    }

    workspace.setOwnerId(owner);

    if (CollectionUtils.isNotEmpty(workspace.getMembers())) {
      Optional<WorkspaceMember> originalMember = workspace.getMembers().stream()
                                                          .filter(member ->
                                                                      (member.getMemberType() == WorkspaceMember.MemberType.USER) && owner.equals(member.getMemberId()))
                                                          .findFirst();
      originalMember.ifPresent(workspaceMember -> workspace.getMembers().remove(workspaceMember));
    }

    workspaceRepository.save(workspace);

    return ResponseEntity.noContent().build();
  }

  /**
   * Specify whether to activate the workspace
   */
  @RequestMapping(path = "/workspaces/{id}/activate/{status:active|inactive}", method = RequestMethod.POST)
  public ResponseEntity<?> activateWorkspace(@PathVariable("id") String workspaceId,
                                             @PathVariable("status") String status) {

    Workspace workspace = workspaceRepository.findById(workspaceId).get();
    if (workspace == null) {
      throw new ResourceNotFoundException(workspaceId);
    }

    // If there is additional Action
    if ("inactive".equals(status)) {
      workspace.setActive(false);
    } else {
      workspace.setActive(true);
    }

    workspaceRepository.save(workspace);

    return ResponseEntity.noContent().build();
  }
  // 获取工作空间的角色信息
  @RequestMapping(path = "/workspaces/{id}/rolesets", method = RequestMethod.POST)
  public ResponseEntity<?> addRoleSetForWorkspace(@PathVariable("id") String workspaceId,
                                                  @RequestBody RoleSet roleSet) {

    Workspace workspace = workspaceRepository.findById(workspaceId).get();
    if (workspace == null) {
      throw new ResourceNotFoundException(workspaceId);
    }
    // 在角色中添加工作空间
    roleSet.addWorkspace(workspace);
    roleSet.setScope(RoleSet.RoleSetScope.PRIVATE);

    RoleSet addRoleSet = roleSetService.createRoleSet(roleSet);

    return ResponseEntity.created(URI.create("")).body(addRoleSet);

  }

  @RequestMapping(path = "/workspaces/{id}/rolesets/{roleSetName}", method = RequestMethod.POST)
  public ResponseEntity<?> setWorkspaceRoleSet(@PathVariable("id") String workspaceId,
                                               @PathVariable("roleSetName") String roleSetName) {

    Workspace workspace = workspaceRepository.findById(workspaceId).get();
    if (workspace == null) {
      throw new ResourceNotFoundException(workspaceId);
    }

    workspaceService.addRoleSet(workspace, roleSetName);

    return ResponseEntity.noContent().build();
  }
  // 更改工作空间的角色
  @RequestMapping(path = "/workspaces/{id}/rolesets/{roleSetName}/to/{toRoleSetName}",
      method = {RequestMethod.PUT, RequestMethod.PATCH})
  public ResponseEntity<?> changeWorkspaceRoleSet(@PathVariable("id") String workspaceId,
                                                  @PathVariable("roleSetName") String roleSetName,
                                                  @PathVariable("toRoleSetName") String toRoleSetName,
                                                  @RequestParam(required = false) String defaultRoleName,
                                                  @RequestBody(required = false) Map<String, String> mapper) {

    Workspace workspace = workspaceRepository.findById(workspaceId).get();
    if (workspace == null) {
      throw new ResourceNotFoundException(workspaceId);
    }

    workspaceService.changeRoleSet(workspace, roleSetName, toRoleSetName, defaultRoleName, mapper);

    return ResponseEntity.noContent().build();
  }

  @RequestMapping(path = "/workspaces/{id}/rolesets/{roleSetName}", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteWorkspaceRoleSet(@PathVariable("id") String workspaceId,
                                                  @PathVariable("roleSetName") String roleSetName,
                                                  @RequestParam(required = false) String defaultRoleName) {

    Workspace workspace = workspaceRepository.findById(workspaceId).get();
    if (workspace == null) {
      throw new ResourceNotFoundException(workspaceId);
    }

    workspaceService.deleteRoleSet(workspace, roleSetName, defaultRoleName);

    return ResponseEntity.noContent().build();
  }

  /**
   *
   */
  public class WorkspaceListComparator implements Comparator<Workspace> {

    @Override
    public int compare(Workspace w1, Workspace w2) {

      if (w1.getPublicType() == PRIVATE && w2.getPublicType() != PRIVATE) {
        return -1;
      } else if (w1.getPublicType() != PRIVATE && w2.getPublicType() == PRIVATE) {
        return 1;
      }

      return w1.getName().compareTo(w2.getName());
    }
  }

}
