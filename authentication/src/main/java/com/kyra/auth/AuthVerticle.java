package com.kyra.auth;

import com.kyra.auth.controller.AuthController;
import com.kyra.common.verticle.ApiVerticle;
import io.vertx.core.Promise;

public class AuthVerticle extends ApiVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start();
    launchHttpServer(AuthVerticle.class.getName(), config().getInteger("port", 3001), ar -> {
      new AuthController(vertx, router, sessionStore);
      log.info(String.format("%s service successfully starts", AuthVerticle.class.getName()));
      startPromise.complete();
    });
  }
}
