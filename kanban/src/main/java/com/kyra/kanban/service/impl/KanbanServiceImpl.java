package com.kyra.kanban.service.impl;

import com.kyra.common.pg.Pg;
import com.kyra.common.pg.PgResult;
import com.kyra.kanban.service.KanbanService;
import com.kyra.kanban.service.TicketService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Tuple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KanbanServiceImpl implements KanbanService {
  private final TicketService ticketService = new TicketServiceImpl();

  @Override
  public void retrieve(Integer projectId, Handler<AsyncResult<List<JsonObject>>> handler) {
    String query = String.format("SELECT id, name, position FROM %s.activity WHERE project_id = $1 ORDER BY position ASC", Pg.getInstance().schema());
    Pg.getInstance().preparedQuery(query, Tuple.of(projectId), PgResult.jsonResult(ar -> {
      if (ar.failed()) {
        handler.handle(ar);
        return;
      }

      List<JsonObject> activities = ar.result();
      Map<Integer, JsonObject> map = new HashMap<>();
      activities.forEach(activity -> {
        activity.put("tickets", new JsonArray());
        map.put(activity.getInteger("id"), activity);
      });
      List<Integer> activitiesId = activities.stream().map(activity -> activity.getInteger("id")).collect(Collectors.toList());
      ticketService.list(activitiesId, aH -> {
        if (aH.failed()) handler.handle(aH);
        else {
          List<JsonObject> tickets = aH.result();
          tickets.forEach(ticket -> map.get(ticket.getInteger("activity_id")).getJsonArray("tickets").add(ticket));
          handler.handle(Future.succeededFuture(activities));
        }
      });
    }));
  }
}
