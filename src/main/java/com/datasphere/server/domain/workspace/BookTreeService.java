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
import com.google.common.collect.Maps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.datasphere.server.domain.workspace.folder.Folder;
import com.datasphere.server.domain.workspace.folder.FolderRepository;

import static com.datasphere.server.domain.workspace.BookProjections.BookProjectionType.LIST;

/**
 * Created by aladin on 2019. 12. 21..
 */
@Component
@Transactional(readOnly = true)
public class BookTreeService {

  private static Logger LOGGER = LoggerFactory.getLogger(BookTreeService.class);

  @Autowired
  BookRepository bookRepository;

  @Autowired
  BookTreeRepository bookTreeRepository;

  @Autowired
  FolderRepository folderRepository;

  BookComparator comparator;

  public BookTreeService() {
    this.comparator = new BookComparator();
  }

  public List<Book> findRootBooks(String workspaceId) {

    List<Book> rootBooks = bookRepository.findRootBooksInWorkspace(workspaceId);
    rootBooks.sort(comparator);
    for (Book book : rootBooks) {
      if (book instanceof Folder) {
        Folder folder = (Folder) book;
        List<Book> books = bookRepository.findOnlySubBooks(folder.getId());
        books.sort(comparator);
        folder.setBooks(books);
      }
    }

    return rootBooks;
  }

  public List<Book> findDecendantBooks(String folderId) {
    List<Book> books = bookRepository.findOnlySubBooks(folderId);
    books.sort(comparator);

    return books;
  }

  public List<Book> findSubBooks(String bookId, boolean isRoot, String... type) {

    List<Book> books;
    if (isRoot) {
      books = bookRepository.findRootBooksInWorkspace(bookId, type);
    } else {
      books = bookRepository.findOnlySubBooks(bookId, type);
    }
    // Adjust Folder to Top
    books.sort(comparator);

    // Check if subbooks exist in case of folder type among subbooks
    for (Book book : books) {
      if (book instanceof Folder) {
        Folder folder = (Folder) book;
        folder.setHasSubBooks(
            bookRepository.countOnlySubBooks(folder.getId(), type) > 0 ? true : false
        );
      }
    }
    return books;
  }

  public List<Map<String, Object>> findSubBooksInfoForView(String bookId, boolean isRoot,
                                                           BookProjections.BookProjectionType type,
                                                           String bookType) {
    List<Book> books = findSubBooks(bookId, isRoot, bookType);

    return books.stream().map(book -> {
      if (type == LIST) {
        return book.listViewProjection();
      } else {
        return book.treeViewProjection();
      }
    }).collect(Collectors.toList());

  }

  public List<Map<String, String>> findBookHierarchies(String bookId) {
    List<Book> books = bookRepository.findAllAncestorBooks(bookId);

    return books.stream()
                .map(book -> {
                  Map<String, String> map = Maps.newHashMap();
                  map.put("id", book.getId());
                  map.put("name", book.getName());
                  return map;
                })
                .collect(Collectors.toList());
  }

  @Transactional
  public void createSelfTree(Book book) {
    BookTree tree = new BookTree(book.getId(), book.getId(), 0);
    bookTreeRepository.save(tree);
  }

  @Transactional
  public void createTree(Book book) {
    List<BookTree> bookTrees = Lists.newArrayList();
    bookTrees.add(new BookTree(book.getId(), book.getId(), 0));

    if (Folder.ROOT.equals(book.getFolderId())) {
      bookTreeRepository.saveAll(bookTrees);
      return;
    }

    Folder folder = folderRepository.findById(book.getFolderId()).get();
    if (folder == null) {
      throw new IllegalArgumentException("Invalid Folder : " + book.getFolderId());
    }

    List<BookTree> ancestors = bookTreeRepository.findByIdDescendant(folder.getId());

    for (BookTree ancestor : ancestors) {
      bookTrees.add(new BookTree(ancestor.getId().getAncestor(), book.getId(), ancestor.getDepth() + 1));
    }

    bookTreeRepository.saveAll(bookTrees);
  }

  @Transactional
  public void editTree(Book book) {
    List<BookTree> bookTrees = Lists.newArrayList();
    List<String> deleteDescendants = Lists.newArrayList();

    if ("ROOT".equals(book.getFolderId())) {

      List<BookTree> descendants = bookTreeRepository.findDescendantNotAncenstor(book.getId());
      for (BookTree bookTree : descendants) {
        deleteDescendants.add(bookTree.getId().getDescendant());
        bookTrees.add(new BookTree(book.getId(), bookTree.getId().getDescendant(), bookTree.getDepth()));
      }

    } else {

      Folder folder = folderRepository.findById(book.getFolderId()).get();
      if (folder == null) {
        throw new IllegalArgumentException("Invalid Folder : " + book.getFolderId());
      }

      List<BookTree> ancestors = bookTreeRepository.findByIdDescendant(folder.getId());
      Map<String, Integer> depthMap = Maps.newHashMap();
      int depth;
      for (BookTree ancestor : ancestors) {
        depth = ancestor.getDepth() + 1;
        bookTrees.add(new BookTree(ancestor.getId().getAncestor(), book.getId(), depth));
        depthMap.put(ancestor.getId().getAncestor(), depth);
      }

      List<BookTree> descendants = bookTreeRepository.findDescendantNotAncenstor(book.getId());
      for (BookTree bookTree : descendants) {
        deleteDescendants.add(bookTree.getId().getDescendant());
        depthMap.forEach((ancestor, i) ->
                             bookTrees.add(new BookTree(ancestor, bookTree.getId().getDescendant(), i + bookTree.getDepth()))
        );
      }
    }

    // Empty IN clause occurrence situation meeting (an error occurs in mysql)
    bookTreeRepository.deleteEditedBookTree(deleteDescendants.isEmpty() ? null : deleteDescendants,
                                            book.getId());

    bookTreeRepository.saveAll(bookTrees);
  }

  @Transactional
  public void deleteTree(Book book) {
    // delete sub-book
    List<BookTree> descendants = bookTreeRepository.findDescendantNotAncenstor(book.getId());
    if (descendants.size() > 0) {
      for (BookTree bookTree : descendants) {
        String descendantId = bookTree.getId().getDescendant();
        bookRepository.deleteById(descendantId);
        bookTreeRepository.deteleAllBookTree(descendantId);
      }
    }

    bookTreeRepository.deteleAllBookTree(book.getId());
  }

  public class BookComparator implements Comparator<Book> {

    @Override
    public int compare(Book book1, Book book2) {

      if (book1 instanceof Folder && !(book2 instanceof Folder)) {
        return -1;
      } else if (!(book1 instanceof Folder) && book2 instanceof Folder) {
        return 1;
      }

      return book1.getName().compareTo(book2.getName());
    }
  }
}
