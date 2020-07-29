package com.kyra.kanban.controller;

import com.kyra.common.controller.CommonController;
import com.kyra.common.session.AuthCookie;
import com.kyra.common.utils.Render;
import com.kyra.kanban.KanbanVerticle;
import com.kyra.kanban.openapi.OperationId;
import com.kyra.kanban.service.TicketService;
import com.kyra.kanban.service.impl.TicketServiceImpl;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;
import io.vertx.ext.web.sstore.SessionStore;

public class TicketController extends CommonController {
  private final Logger log = LoggerFactory.getLogger(TicketController.class);
  private final TicketService tickerService = new TicketServiceImpl();

  public TicketController(OpenAPI3RouterFactory router, SessionStore sessionStore) {
    super(sessionStore);
    router.addSecurityHandler(AuthCookie.NAME, sessionHandler);
    router
      .addHandlerByOperationId(OperationId.PatchTicket.name(), this::validId)
      .addHandlerByOperationId(OperationId.PatchTicket.name(), this::patchTicket);
  }

  private void validId(RoutingContext rc) {
    try {
      Integer.parseInt(rc.pathParam("id"));
      Integer.parseInt(rc.pathParam("ticketId"));
      rc.next();
    } catch (NumberFormatException e) {
      Render.badRequest(rc);
    }
  }

  private void patchTicket(RoutingContext rc) {
    Integer ticketId = Integer.parseInt(rc.pathParam("ticketId"));
    Integer activityId = rc.getBodyAsJson().getInteger("activity_id");
    Integer index = rc.getBodyAsJson().getInteger("index");

    if (activityId == null || index == null) {
      Render.badRequest(rc);
      return;
    }

    tickerService.moveTicket(ticketId, activityId, index, ar -> {
      if (ar.failed()) {
        log.error(String.format("[%s] failed to move ticket for ticket %d, activity %d, index %d", KanbanVerticle.class.getName(), ticketId, activityId, index), ar.cause());
        Render.internalServerError(rc);
      } else {
        Render.ok(rc, ar.result());
      }
    });
  }
}
