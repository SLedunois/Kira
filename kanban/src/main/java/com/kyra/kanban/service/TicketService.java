package com.kyra.kanban.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface TicketService {
  void list(List<Integer> activities, Handler<AsyncResult<List<JsonObject>>> handler);

  void moveTicket(Integer ticketId, Integer activityId, Integer index, Handler<AsyncResult<JsonObject>> handler);
}
