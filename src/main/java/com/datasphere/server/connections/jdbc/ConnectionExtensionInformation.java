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

package com.datasphere.server.connections.jdbc;

/**
 * The type Connection extension information.
 */
public class ConnectionExtensionInformation {

  /**
   * The Name.
   */
  String name;
  /**
   * The Dialect type.
   */
  DialectType dialectType;
  
  /**
   * The Implementor.
   */
  String implementor;
  /**
   * The Input mandatory spec.
   */
  InputMandatorySpec inputMandatorySpec;

  public String getName() {
    return name;
  }

  /**
   * Sets name.
   *
   * @param name the name
   * @return the name
   */
  public ConnectionExtensionInformation setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Gets dialect type.
   *
   * @return the dialect type
   */
  public DialectType getDialectType() {
    return dialectType;
  }

  /**
   * Sets dialect type.
   *
   * @param dialectType the dialect type
   * @return the dialect type
   */
  public ConnectionExtensionInformation setDialectType(DialectType dialectType) {
    this.dialectType = dialectType;
    return this;
  }

  
  /**
   * Gets implementor.
   *
   * @return the implementor
   */
  public String getImplementor() {
    return implementor;
  }

  /**
   * Sets implementor.
   *
   * @param implementor the implementor
   * @return the implementor
   */
  public ConnectionExtensionInformation setImplementor(String implementor) {
    this.implementor = implementor;
    return this;
  }

  /**
   * Gets input mandatory spec.
   *
   * @return the input mandatory spec
   */
  public InputMandatorySpec getInputMandatorySpec() {
    return inputMandatorySpec;
  }

  /**
   * Sets input mandatory spec.
   *
   * @param inputMandatorySpec the input mandatory spec
   * @return the input mandatory spec
   */
  public ConnectionExtensionInformation setInputMandatorySpec(InputMandatorySpec inputMandatorySpec) {
    this.inputMandatorySpec = inputMandatorySpec;
    return this;
  }

  /**
   * The enum Dialect type.
   */
  public enum DialectType {
    /**
     * Embedded dialect type.
     */
    EMBEDDED,
    /**
     * Extension dialect type.
     */
    EXTENSION
  }

  /**
   * The enum Input mandatory.
   */
  public enum InputMandatory {
    /**
     * None input mandatory.
     */
    NONE,
    /**
     * Optional input mandatory.
     */
    OPTIONAL,
    /**
     * Mandatory input mandatory.
     */
    MANDATORY
  }

  /**
   * The type Input mandatory spec.
   */
  class InputMandatorySpec{
    /**
     * Instantiates a new Input mandatory spec.
     */
    public InputMandatorySpec(){

    }

    /**
     * The Implementor.
     */
    final InputMandatory implementor = InputMandatory.MANDATORY;
    /**
     * The Authentication type.
     */
    InputMandatory authenticationType = InputMandatory.OPTIONAL;
    /**
     * The Url.
     */
    InputMandatory url = InputMandatory.OPTIONAL;
    /**
     * The Options.
     */
    InputMandatory options = InputMandatory.OPTIONAL;
    /**
     * The Hostname.
     */
    InputMandatory hostname = InputMandatory.OPTIONAL;
    /**
     * The Port.
     */
    InputMandatory port = InputMandatory.OPTIONAL;
    /**
     * The Database.
     */
    InputMandatory database = InputMandatory.OPTIONAL;
    /**
     * The Sid.
     */
    InputMandatory sid = InputMandatory.OPTIONAL;
    /**
     * The Catalog.
     */
    InputMandatory catalog = InputMandatory.OPTIONAL;
    /**
     * The Properties.
     */
    InputMandatory properties = InputMandatory.OPTIONAL;
    /**
     * The Username.
     */
    InputMandatory username = InputMandatory.OPTIONAL;
    /**
     * The Password.
     */
    InputMandatory password = InputMandatory.OPTIONAL;

    /**
     * Gets authentication type.
     *
     * @return the authentication type
     */
    public InputMandatory getAuthenticationType() {
      return authenticationType;
    }

    /**
     * Gets implementor.
     *
     * @return the implementor
     */
    public InputMandatory getImplementor() {
      return implementor;
    }

    /**
     * Gets url.
     *
     * @return the url
     */
    public InputMandatory getUrl() {
      return url;
    }

    /**
     * Gets options.
     *
     * @return the options
     */
    public InputMandatory getOptions() {
      return options;
    }

    /**
     * Gets hostname.
     *
     * @return the hostname
     */
    public InputMandatory getHostname() {
      return hostname;
    }

    /**
     * Gets port.
     *
     * @return the port
     */
    public InputMandatory getPort() {
      return port;
    }

    /**
     * Gets database.
     *
     * @return the database
     */
    public InputMandatory getDatabase() {
      return database;
    }

    /**
     * Gets sid.
     *
     * @return the sid
     */
    public InputMandatory getSid() {
      return sid;
    }

    /**
     * Gets catalog.
     *
     * @return the catalog
     */
    public InputMandatory getCatalog() {
      return catalog;
    }

    /**
     * Gets properties.
     *
     * @return the properties
     */
    public InputMandatory getProperties() {
      return properties;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public InputMandatory getUsername() {
      return username;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public InputMandatory getPassword() {
      return password;
    }

    /**
     * Set authentication type input mandatory spec.
     *
     * @param authenticationType the authentication type
     * @return the input mandatory spec
     */
    public InputMandatorySpec setAuthenticationType(InputMandatory authenticationType){
      this.authenticationType = authenticationType;
      return this;
    }

    /**
     * Sets url.
     *
     * @param url the url
     * @return the url
     */
    public InputMandatorySpec setUrl(InputMandatory url) {
      this.url = url;
      return this;
    }

    /**
     * Sets options.
     *
     * @param options the options
     * @return the options
     */
    public InputMandatorySpec setOptions(InputMandatory options) {
      this.options = options;
      return this;
    }

    /**
     * Sets hostname.
     *
     * @param hostname the hostname
     * @return the hostname
     */
    public InputMandatorySpec setHostname(InputMandatory hostname) {
      this.hostname = hostname;
      return this;
    }

    /**
     * Sets port.
     *
     * @param port the port
     * @return the port
     */
    public InputMandatorySpec setPort(InputMandatory port) {
      this.port = port;
      return this;
    }

    /**
     * Sets database.
     *
     * @param database the database
     * @return the database
     */
    public InputMandatorySpec setDatabase(InputMandatory database) {
      this.database = database;
      return this;
    }

    /**
     * Sets sid.
     *
     * @param sid the sid
     * @return the sid
     */
    public InputMandatorySpec setSid(InputMandatory sid) {
      this.sid = sid;
      return this;
    }

    /**
     * Sets catalog.
     *
     * @param catalog the catalog
     * @return the catalog
     */
    public InputMandatorySpec setCatalog(InputMandatory catalog) {
      this.catalog = catalog;
      return this;
    }

    /**
     * Sets properties.
     *
     * @param properties the properties
     * @return the properties
     */
    public InputMandatorySpec setProperties(InputMandatory properties) {
      this.properties = properties;
      return this;
    }

    /**
     * Sets username.
     *
     * @param username the username
     * @return the username
     */
    public InputMandatorySpec setUsername(InputMandatory username) {
      this.username = username;
      return this;
    }

    /**
     * Sets password.
     *
     * @param password the password
     * @return the password
     */
    public InputMandatorySpec setPassword(InputMandatory password) {
      this.password = password;
      return this;
    }
  }
}
