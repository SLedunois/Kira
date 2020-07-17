package com.kyra.kanban.controller;

import com.kyra.common.controller.CommonController;
import com.kyra.common.session.AuthCookie;
import com.kyra.common.utils.Render;
import com.kyra.kanban.KanbanVerticle;
import com.kyra.kanban.openapi.OperationId;
import com.kyra.kanban.service.KanbanService;
import com.kyra.kanban.service.impl.KanbanServiceImpl;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;
import io.vertx.ext.web.sstore.SessionStore;

import java.util.List;

public class KanbanController extends CommonController {

  private final KanbanService kanbanService = new KanbanServiceImpl();

  public KanbanController(OpenAPI3RouterFactory router, SessionStore sessionStore) {
    super(sessionStore);
    router.addSecurityHandler(AuthCookie.NAME, sessionHandler);
    router.addHandlerByOperationId(OperationId.GetKanban.name(), this::checkId)
      .addHandlerByOperationId(OperationId.GetKanban.name(), this::listKanban);
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
        else Render.ok(rc, new JsonArray(ar.result()));
      }
    });
  }
}
