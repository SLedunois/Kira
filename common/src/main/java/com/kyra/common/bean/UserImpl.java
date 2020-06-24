package com.kyra.common.bean;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;

public class UserImpl implements User {
  private String email;
  private String firstName;
  private String lastName;

  public UserImpl(JsonObject object) {
    this.email = object.getString(Field.email.name(), "");
    this.firstName = object.getString(Field.firstName.name(), "");
    this.lastName = object.getString(Field.lastName.name(), "");
  }

  @Override
  public User isAuthorized(String authority, Handler<AsyncResult<Boolean>> resultHandler) {
    return null;
  }

  @Override
  public User clearCache() {
    return null;
  }

  @Override
  public JsonObject principal() {
    return new JsonObject()
      .put(Field.email.name(), this.email)
      .put(Field.firstName.name(), this.firstName)
      .put(Field.lastName.name(), this.lastName);
  }

  @Override
  public void setAuthProvider(AuthProvider authProvider) {
    throw new UnsupportedOperationException("Method not implemented");
  }
}
