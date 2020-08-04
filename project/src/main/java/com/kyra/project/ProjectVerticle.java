package com.kyra.project;

import com.kyra.common.verticle.ApiVerticle;
import com.kyra.project.controller.ProjectController;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;

public class ProjectVerticle extends ApiVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(ProjectVerticle.class.getName());
    openApiRouter("Project.v1.yaml")
      .compose(this::initControllers)
      .compose(this::initHttpServer)
      .onFailure(super::onInitFailure)
      .onComplete(startPromise::handle);
  }

  private Future<Void> initHttpServer(OpenAPI3RouterFactory router) {
    Future<Void> future = Promise.<Void>promise().future();
    launchHttpServer(ProjectVerticle.class.getName(), router, config().getInteger("port", 3002), ar -> {
      if (ar.failed()) {
        log.error(String.format("[%s] Failed to start http server", ProjectVerticle.class.getName()), ar.cause());
        future.handle(Future.failedFuture(ar.cause()));
      } else {
        log.info(String.format("%s service successfully starts", ProjectVerticle.class.getName()));
        future.handle(Future.succeededFuture());
      }
    });

    return future;
  }

  private Future<OpenAPI3RouterFactory> initControllers(OpenAPI3RouterFactory router) {
    new ProjectController(vertx, router, sessionStore);
    return Future.succeededFuture(router);
  }
}
