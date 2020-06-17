package com.kyra.auth.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

public interface AuthService {
  /**
   * Register new user
   *
   * @param email     user email
   * @param firstName user first name
   * @param lastName  user last name
   * @param password  user salt password
   * @param handler   function handler returning data
   */
  void register(String email, String firstName, String lastName, String password, Handler<AsyncResult<JsonObject>> handler);
}
