package com.datasphere.server.datasource.ingestion.rule;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.datasphere.server.spec.druid.ingestion.Validation;

public class ExclusionRule extends ValidationRule implements IngestionRule {

  String expr;

  @JsonCreator
  public ExclusionRule(@JsonProperty("expr") String expr) {
    this.expr = expr;
  }

  public String getExpr() {
    return expr;
  }

  @Override
  public Validation toValidation(String name) {
    return null;
  }
}
