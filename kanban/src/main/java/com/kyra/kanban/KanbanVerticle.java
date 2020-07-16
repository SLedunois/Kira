package com.kyra.kanban;

import com.kyra.common.handler.ProjectHandler;
import com.kyra.common.verticle.ApiVerticle;
import com.kyra.kanban.handler.ProjectHandlerImpl;
import io.vertx.core.Promise;
import io.vertx.serviceproxy.ServiceBinder;

public class KanbanVerticle extends ApiVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(KanbanVerticle.class.getName());
    launchHttpServer(KanbanVerticle.class.getName(), config().getInteger("port", 3003), ar -> {
      log.info(String.format("%s service successfully starts", KanbanVerticle.class.getName()));
      startPromise.complete();
    });
    initHandlers();
  }

  private void initHandlers() {
    ProjectHandler projectHandler = new ProjectHandlerImpl();
    new ServiceBinder(vertx).setAddress(ProjectHandler.HANDLER_ADDRESS).register(ProjectHandler.class, projectHandler);
  }
}
