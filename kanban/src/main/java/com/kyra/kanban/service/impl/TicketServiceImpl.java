package com.kyra.kanban.service.impl;

import com.kyra.common.pg.Pg;
import com.kyra.common.pg.PgResult;
import com.kyra.kanban.service.TicketService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Tuple;

import java.util.List;

public class TicketServiceImpl implements TicketService {
  @Override
  public void list(List<Integer> activities, Handler<AsyncResult<List<JsonObject>>> handler) {
    String query = String.format("SELECT * FROM %s.ticket WHERE activity_id IN " + Pg.getInstance().preparedList(activities) + " ORDER BY index ASC", Pg.getInstance().schema());
    Pg.getInstance().preparedQuery(query, Tuple.tuple((List) activities), PgResult.jsonResult(handler));
  }

  @Override
  public void moveTicket(Integer ticketId, Integer activityId, Integer index, Handler<AsyncResult<JsonObject>> handler) {
    String query = String.format("SELECT * FROM %s.move_ticket($1, $2, $3)", Pg.getInstance().schema());
    Pg.getInstance().preparedQuery(query, Tuple.of(ticketId, activityId, index), PgResult.uniqueJsonResult(handler));
  }
}
