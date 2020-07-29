package com.kyra.kanban.controller;

import com.kyra.common.controller.CommonController;
import com.kyra.common.proxy.AccountProxy;
import com.kyra.common.session.AuthCookie;
import com.kyra.common.utils.Render;
import com.kyra.kanban.KanbanVerticle;
import com.kyra.kanban.openapi.OperationId;
import com.kyra.kanban.service.KanbanService;
import com.kyra.kanban.service.impl.KanbanServiceImpl;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;
import io.vertx.ext.web.sstore.SessionStore;

import java.util.*;

public class KanbanController extends CommonController {
  private final AccountProxy accountProxy;
  private final KanbanService kanbanService = new KanbanServiceImpl();

  public KanbanController(Vertx vertx, OpenAPI3RouterFactory router, SessionStore sessionStore) {
    super(sessionStore);
    this.accountProxy = AccountProxy.createProxy(vertx);
    router.addSecurityHandler(AuthCookie.NAME, sessionHandler);
    router.addHandlerByOperationId(OperationId.GetKanban.name(), this::checkId)
      .addHandlerByOperationId(OperationId.GetKanban.name(), this::listKanban);
    router.addHandlerByOperationId(OperationId.PostTicket.name(), this::checkId)
      .addHandlerByOperationId(OperationId.PostTicket.name(), this::createTicket);
  }

  private void createTicket(RoutingContext rc) {
    Integer projectId = Integer.parseInt(rc.request().getParam("id"));
    JsonObject ticket = rc.getBodyAsJson();
    kanbanService.create(projectId, ticket, rc.user(), ar -> {
      if (ar.failed()) {
        log.error(String.format("[%s] Failed to create ticket for activity number %d", KanbanVerticle.class.getName(), projectId), ar.cause());
        Render.internalServerError(rc);
      } else {
        JsonObject created = ar.result();
        accountProxy.retrieve(Arrays.asList(created.getString("assignee")), accHandler -> {
          if (accHandler.failed()) {
            log.error(String.format("[%s] Failed to retrieve user", accHandler.cause()));
          } else {
            if (!accHandler.result().isEmpty()) {
              created.put("assignee", accHandler.result().get(0));
            }
          }

          Render.created(rc, created);
        });
      }
    });
  }

  private void checkId(RoutingContext rc) {
    try {
      Integer.parseInt(rc.pathParam("id"));
      rc.next();
    } catch (NumberFormatException e) {
      Render.badRequest(rc);
    }
  }

  private void listKanban(RoutingContext rc) {
    int projectId = Integer.parseInt(rc.pathParam("id"));
    kanbanService.retrieve(projectId, ar -> {
      if (ar.failed()) {
        log.error(String.format("[%s] Unable to retrieve kanban for project number %d", KanbanVerticle.class.getName(), projectId), ar.cause());
        Render.internalServerError(rc);
      } else {
        List<JsonObject> result = ar.result();
        if (result.isEmpty()) Render.notFound(rc);
        else {
          List<JsonObject> activities = ar.result();
          List<String> users = new ArrayList<>();
          activities
            .forEach(activity -> ((List<JsonObject>) activity.getJsonArray("tickets").getList()).forEach(ticket -> users.add(ticket.getString("assignee"))));
          accountProxy.retrieve(users, accHandler -> {
            if (accHandler.failed()) {
              Render.internalServerError(rc);
            } else {
              Map<String, JsonObject> map = new HashMap<>();
              accHandler
                .result()
                .forEach(user -> map.put(user.getString("email"), user));
              activities
                .forEach(activity -> ((List<JsonObject>) activity.getJsonArray("tickets").getList()).forEach(ticket -> ticket.put("assignee", map.get(ticket.getString("assignee")))));
              Render.ok(rc, new JsonArray(activities));
            }
          });
        }
      }
    });
  }
}
