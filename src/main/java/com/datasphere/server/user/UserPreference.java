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

import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_preference")
public class UserPreference {

  @Id
  @Column(name = "id")
  String username;
  // 用户语言
  @Column(name = "user_lang")
  String language;
  // 用户位置
  @Column(name = "user_locale")
  String locale;
  // 用户时区
  @Column(name = "user_timezone")
  String timezone;
  // 用户最后访问时间
  @Column(name = "user_last_access_time")
  DateTime lastAccessTime;

  public UserPreference() {
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public String getTimezone() {
    return timezone;
  }

  public void setTimezone(String timezone) {
    this.timezone = timezone;
  }

  public DateTime getLastAccessTime() {
    return lastAccessTime;
  }

  public void setLastAccessTime(DateTime lastAccessTime) {
    this.lastAccessTime = lastAccessTime;
  }

  @Override
  public String toString() {
    return "UserPreference{" +
        "username='" + username + '\'' +
        ", language='" + language + '\'' +
        ", locale='" + locale + '\'' +
        ", timezone='" + timezone + '\'' +
        ", lastAccessTime=" + lastAccessTime +
        '}';
  }
}
