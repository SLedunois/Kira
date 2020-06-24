package com.kyra.account.bean;

import com.kyra.account.form.Field;
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
    this.email = object.getString("email", "");
    this.firstName = object.getString("first_name", "");
    this.lastName = object.getString("last_name", "");
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
