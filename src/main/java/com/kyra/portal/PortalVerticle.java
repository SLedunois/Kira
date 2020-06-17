package com.kyra.portal;

import com.kyra.common.verticle.ApiVerticle;
import com.kyra.portal.controller.PortalController;
import io.vertx.core.Promise;

public class PortalVerticle extends ApiVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start();
    launchHttpServer(PortalVerticle.class.getName(), config().getInteger("port", 3000), ar -> {
      new PortalController(router, sessionStore);
      log.info(String.format("%s service successfully starts", PortalVerticle.class.getName()));
      startPromise.complete();
    });
  }
}
