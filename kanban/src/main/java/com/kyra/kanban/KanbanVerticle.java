package com.kyra.kanban;

import com.kyra.common.verticle.ApiVerticle;
import io.vertx.core.Promise;

public class KanbanVerticle extends ApiVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(KanbanVerticle.class.getName());
    launchHttpServer(KanbanVerticle.class.getName(), config().getInteger("port", 3003), ar -> {
      log.info(String.format("%s service successfully starts", KanbanVerticle.class.getName()));
      startPromise.complete();
    });
  }
}
