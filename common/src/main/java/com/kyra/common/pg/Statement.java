package com.kyra.common.pg;

import io.vertx.sqlclient.Tuple;

public class Statement {
  private String query;
  private Tuple params;
  private boolean prepared;

  public Statement(String query) {
    this.query = query;
    this.prepared = false;
  }

  public Statement(String query, Tuple params) {
    this.query = query;
    this.params = params;
    this.prepared = true;
  }

  public boolean prepared() {
    return prepared;
  }

  public String query() {
    return query;
  }

  public Tuple params() {
    return params;
  }

}
