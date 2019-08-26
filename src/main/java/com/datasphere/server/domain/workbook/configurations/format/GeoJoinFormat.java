package com.datasphere.server.domain.workbook.configurations.format;

import com.fasterxml.jackson.annotation.JsonCreator;

public class GeoJoinFormat extends GeoFormat implements FieldFormat {

  @JsonCreator
  public GeoJoinFormat() {
  }

}
