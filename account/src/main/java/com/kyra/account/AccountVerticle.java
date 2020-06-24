package com.kyra.account;

import com.kyra.account.controller.AccountController;
import com.kyra.common.verticle.ApiVerticle;
import io.vertx.core.Promise;

public class AccountVerticle extends ApiVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start();
    launchHttpServer(AccountVerticle.class.getName(), config().getInteger("port", 3001), ar -> {
      new AccountController(vertx, router, sessionStore);
      log.info(String.format("%s service successfully starts", AccountVerticle.class.getName()));
      startPromise.complete();
    });
  }
}
