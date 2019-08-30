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

import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.datasphere.engine.common.exception.MetatronException;
import com.datasphere.server.datasource.DataSourceRepository;
import com.datasphere.server.domain.workbook.DashBoard;
import com.datasphere.server.domain.workbook.DashBoardService;
import com.datasphere.server.domain.workbook.WorkBook;
import com.datasphere.server.domain.workbook.WorkBookRepository;
import com.google.common.collect.Lists;


@Component
@Transactional(readOnly = true)
public class BookService {

  @Autowired
  BookRepository bookRepository;

  @Autowired
  WorkBookRepository workBookRepository;

  @Autowired
  DataSourceRepository dataSourceRepository;

  @Autowired
  WorkspaceRepository workspaceRepository;

  @Autowired
  DashBoardService dashBoardService;

  @Autowired
  WorkspaceService workspaceService;

  @Autowired
  BookTreeService bookTreeService;

  @Transactional
  public WorkBook copy(Book book, Optional<String> folderId, Optional<String> workspaceId) {

    // 이동 가능한 workspace 인지 체크 (동일한 데이터 소스 포함 여부)
    if(!canMoveToWorkspace(book.getId(), book.getWorkspace().getId(), workspaceId)) {
      throw new MetatronException("Not allowed copy to workspace( " + workspaceId + " )");
    }

    // FolderId가 워크스페이스에 존재하는지 여부 체크
    if(!existFolderInWorkspace(workspaceId, folderId)) {
      throw new MetatronException("Not allowed copy to folder( " + folderId + " ) in workspace.");
    }

    if(book instanceof WorkBook) {
      Workspace targetWorkspace = workspaceId.isPresent() ? workspaceRepository.findById(workspaceId.get()).get() : null;
      WorkBook sourceWorkBook = (WorkBook) book;
      WorkBook targetWorkBook = (WorkBook) book.copyOf(targetWorkspace, true);
      if(folderId.isPresent()) {
        targetWorkBook.setFolderId(folderId.get());
      } else {
        targetWorkBook.setFolderId(sourceWorkBook.getFolderId());
      }

      workBookRepository.saveAndFlush(targetWorkBook);

      if(CollectionUtils.isNotEmpty(sourceWorkBook.getDashBoards())) {
        List<DashBoard> children = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(sourceWorkBook.getDashBoards())) {
          for (DashBoard dashBoard : sourceWorkBook.getDashBoards()) {
            DashBoard copiedDashBoard = dashBoardService.copy(dashBoard, targetWorkBook, false);
            copiedDashBoard.setSeq(dashBoard.getSeq());
            children.add(copiedDashBoard);
          }
        }
        targetWorkBook.setDashBoards(children);
      }

      bookTreeService.createTree(targetWorkBook);

      return workBookRepository.save(targetWorkBook);

    } else {
      throw new MetatronException("Not supported type.");
    }
  }

  @Transactional
  public Book move(Book book, Optional<String> folderId, Optional<String> workspaceId) {

    // 이동 가능한 workspace 인지 체크 (동일한 데이터 소스 포함 여부)
    if(!canMoveToWorkspace(book.getId(), book.getWorkspace().getId(), workspaceId)) {
      throw new MetatronException("Not allowed copy to workspace( " + workspaceId + " )");
    }

    // TODO: 동일 워크스페이스내 동일한 폴더 인경우는 고려해볼것!

    // FolderId가 워크스페이스에 존재하는지 여부 체크
    if(!existFolderInWorkspace(workspaceId, folderId)) {
      throw new MetatronException("Not allowed copy to folder( " + folderId + " ) in workspace.");
    }

    if(workspaceId.isPresent()) {
      book.setWorkspace(workspaceRepository.findById(workspaceId.get()).get());
    }

    book.setFolderId(folderId.orElse("ROOT"));

    bookTreeService.editTree(book);

    return bookRepository.save(book);
  }

  /**
   * Book 객체를 이동시킬수 있는지 여부 체크
   *
   * @param bookId
   * @param sourceId
   * @param destId
   * @return
   */
  private boolean canMoveToWorkspace(String bookId, String sourceId, Optional<String> destId) {

    if(!destId.isPresent()) {
      return true;
    }

    if(sourceId.equals(destId.get())) {
      return true;
    }

    return workspaceService.checkWorkBookCopyToWorkspace(bookId, destId.get());
  }

  /**
   * 특정 워크스페이스 내 이동 가능한 폴더인지 체크
   *
   * @param workspaceId
   * @param folderId
   * @return
   */
  private boolean existFolderInWorkspace(Optional<String> workspaceId, Optional<String> folderId) {

    // 폴더 ID 만 있는 경우 동일 워크스페이스 라고 가정
    // 워크스페이스 ID 만 있는경우 ROOT 로 가정
    if(!(workspaceId.isPresent() && folderId.isPresent())) {
      return true;
    }

    return bookRepository.count(BookPredicate.checkFolderInWorkspace(workspaceId.get(), folderId.get())) > 0;
  }

}
