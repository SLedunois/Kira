package com.kyra.account.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface AccountService {
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

  /**
   * Find user based on given email. Used in sign up sequence to check if email address already exists in the database
   *
   * @param email   user email
   * @param handler function handler returning data
   */
  void findUser(String email, Handler<AsyncResult<JsonObject>> handler);

  void search(String query, Handler<AsyncResult<List<JsonObject>>> handler);
}
