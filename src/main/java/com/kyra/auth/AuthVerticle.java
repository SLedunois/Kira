package com.kyra.auth;

import com.kyra.common.verticle.ApiVerticle;
import io.vertx.core.Promise;

public class AuthVerticle extends ApiVerticle {

  String NAME = "Authentication";
  int PORT = 3000;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start();
    launchHttpServer(NAME, PORT, ar -> {
      log.info(String.format("%s service successfully starts", NAME));
      startPromise.complete();
    });
  }
}
