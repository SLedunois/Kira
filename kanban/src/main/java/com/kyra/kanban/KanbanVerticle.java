package com.kyra.kanban;

import com.kyra.common.handler.ProjectHandler;
import com.kyra.common.verticle.ApiVerticle;
import com.kyra.kanban.controller.KanbanController;
import com.kyra.kanban.controller.TicketController;
import com.kyra.kanban.handler.ProjectHandlerImpl;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;
import io.vertx.serviceproxy.ServiceBinder;

public class KanbanVerticle extends ApiVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(KanbanVerticle.class.getName());
    openApiRouter("Kanban.v1.yaml")
      .compose(this::initControllers)
      .compose(this::launchHttpServer)
      .compose(this::initHandlers)
      .onFailure(super::onInitFailure)
      .onComplete(startPromise::handle);
  }

  private Future<Void> launchHttpServer(OpenAPI3RouterFactory router) {
    Future<Void> future = Promise.<Void>promise().future();
    launchHttpServer(KanbanVerticle.class.getName(), router, config().getInteger("port", 3003), ar -> {
      if (ar.failed()) {
        log.error(String.format("%s http server can not start", KanbanVerticle.class.getName()), ar.cause());
        future.handle(Future.failedFuture(ar.cause()));
      } else {
        log.info(String.format("%s service successfully starts", KanbanVerticle.class.getName()));
        future.handle(ar);
      }
    });

    return future;
  }

  private Future<OpenAPI3RouterFactory> initControllers(OpenAPI3RouterFactory router) {
    new KanbanController(vertx, router, sessionStore);
    new TicketController(router, sessionStore);
    return Future.succeededFuture(router);
  }

  private Future<Void> initHandlers(Void unused) {
    ProjectHandler projectHandler = new ProjectHandlerImpl();
    new ServiceBinder(vertx).setAddress(ProjectHandler.HANDLER_ADDRESS).register(ProjectHandler.class, projectHandler);
    return Future.succeededFuture(unused);
  }
}
