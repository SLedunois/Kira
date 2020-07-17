package com.kyra.kanban.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface KanbanService {

  void retrieve(Integer projectId, Handler<AsyncResult<List<JsonObject>>> handler);
}
