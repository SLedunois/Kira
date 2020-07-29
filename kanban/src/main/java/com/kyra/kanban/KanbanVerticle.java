package com.kyra.kanban;

import com.kyra.common.handler.ProjectHandler;
import com.kyra.common.verticle.ApiVerticle;
import com.kyra.kanban.controller.KanbanController;
import com.kyra.kanban.controller.TicketController;
import com.kyra.kanban.handler.ProjectHandlerImpl;
import io.vertx.core.Promise;
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;
import io.vertx.serviceproxy.ServiceBinder;

public class KanbanVerticle extends ApiVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(KanbanVerticle.class.getName());
    openApiRouter("Kanban.v1.yaml", openr -> {
      if (openr.failed()) {
        log.error(String.format("[%s] Unable to retrieve open api router description", KanbanVerticle.class.getName()), openr.cause());
      } else {
        OpenAPI3RouterFactory router = openr.result();
        new KanbanController(vertx, router, sessionStore);
        new TicketController(router, sessionStore);
        launchHttpServer(KanbanVerticle.class.getName(), router, config().getInteger("port", 3003), ar -> {
          log.info(String.format("%s service successfully starts", KanbanVerticle.class.getName()));
          startPromise.complete();
        });
      }
    });
    initHandlers();
  }

  private void initHandlers() {
    ProjectHandler projectHandler = new ProjectHandlerImpl();
    new ServiceBinder(vertx).setAddress(ProjectHandler.HANDLER_ADDRESS).register(ProjectHandler.class, projectHandler);
  }
}
