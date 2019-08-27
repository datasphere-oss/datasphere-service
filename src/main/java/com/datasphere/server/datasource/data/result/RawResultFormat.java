package com.datasphere.server.datasource.data.result;

import com.fasterxml.jackson.databind.JsonNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datasphere.server.datasource.DataSource;

public class RawResultFormat extends SearchResultFormat {

  private static final Logger LOGGER = LoggerFactory.getLogger(RawResultFormat.class);

  public RawResultFormat() {
    // Empty Constructor
  }

  public RawResultFormat(DataSource.ConnectionType connType) {
    super(connType);
  }

  @Override
  public Object makeResult(JsonNode obj) {
    return obj;
  }
}
