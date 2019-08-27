package com.datasphere.server.datasource.ingestion.rule;

import com.datasphere.server.spec.druid.ingestion.Validation;

public abstract class ValidationRule {
  public abstract Validation toValidation(String name);
}
