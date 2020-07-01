package com.kyra.project.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;

import java.util.List;

public interface ProjectService {

  void create(JsonObject project, User user, Handler<AsyncResult<JsonObject>> handler);

  void delete(int id, Handler<AsyncResult<Void>> handler);

  void findProjectByIdAndUser(int id, User user, Handler<AsyncResult<Void>> handler);

  void list(User user, Integer sort, Handler<AsyncResult<List<JsonObject>>> handler);

  void update(int id, JsonObject project, Handler<AsyncResult<JsonObject>> handler);
}
