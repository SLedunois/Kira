package com.kyra.portal;

import com.kyra.common.verticle.ApiVerticle;
import com.kyra.portal.controller.PortalController;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;

public class PortalVerticle extends ApiVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(PortalVerticle.class.getName());
    initHttpServer()
      .compose(this::initControllers)
      .onFailure(super::onInitFailure)
      .onComplete(startPromise::handle);
  }

  private Future<Router> initHttpServer() {
    Future<Router> future = Promise.<Router>promise().future();
    launchHttpServer(PortalVerticle.class.getName(), config().getInteger("port", 3000), ar -> {
      if (ar.failed()) {
        log.error(String.format("%s failed to start http server", PortalVerticle.class.getName()), ar.cause());
        future.handle(Future.failedFuture(ar.cause()));
      } else {
        log.info(String.format("%s service successfully starts", PortalVerticle.class.getName()));
        future.handle(Future.succeededFuture(router));
      }
    });

    return future;
  }

  private Future<Void> initControllers(Router router) {
    new PortalController(router, sessionStore);
    return Future.succeededFuture();
  }
}
