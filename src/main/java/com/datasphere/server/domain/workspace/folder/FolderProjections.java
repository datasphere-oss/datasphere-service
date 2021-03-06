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

package com.datasphere.server.domain.workspace.folder;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.util.List;
import java.util.Map;

import com.datasphere.server.domain.user.UserProfile;
import com.datasphere.server.domain.workspace.Book;

/**
 * Created by aladin on 2019. 11. 3..
 */
public class FolderProjections {

  @Projection(name = "default", types = { Folder.class })
  public interface DefaultProjection {

    String getId();

    String getName();

    String getType();

    String getDescription();

    String getFolderId();

    DateTime getCreatedTime();

    @Value("#{@cachedUserService.findUserProfile(target.createdBy)}")
    UserProfile getCreatedBy();

    @Value("#{@cachedUserService.findUserProfile(target.modifiedBy)}")
    UserProfile getModifiedBy();

    DateTime getModifiedTime();

  }

  @Projection(name = "withSubBooks", types = { Folder.class })
  public interface WithBooksProjection {

    String getId();

    String getName();

    String getType();

    String getDescription();

    @Value("#{@bookTreeService.findBookHierarchies(target.id)}")
    List<Map<String, String>> getHierarchies();

    @Value("#{@bookTreeService.findDecendantBooks(target.id)}")
    List<Book> getBooks();

    DateTime getCreatedTime();

    @Value("#{@cachedUserService.findUserProfile(target.createdBy)}")
    UserProfile getCreatedBy();

    @Value("#{@cachedUserService.findUserProfile(target.modifiedBy)}")
    UserProfile getModifiedBy();

    DateTime getModifiedTime();

  }
}
