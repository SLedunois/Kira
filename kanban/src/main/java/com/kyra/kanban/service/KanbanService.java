package com.kyra.kanban.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;

import java.util.List;

public interface KanbanService {

  void retrieve(Integer projectId, Handler<AsyncResult<List<JsonObject>>> handler);

  void create(Integer projectId, JsonObject ticket, User user, Handler<AsyncResult<JsonObject>> handler);
}
