package com.datasphere.server.query.druid.postaggregations;

import com.datasphere.server.query.druid.PostAggregation;

public class ExprPostAggregator implements PostAggregation {

  String expression;

  public ExprPostAggregator(String expression) {
    this.expression = expression;
  }

  public String getExpression() {
    return expression;
  }

  @Override
  public String getName() {
    return null;
  }
}
