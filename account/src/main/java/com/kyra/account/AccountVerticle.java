package com.kyra.account;

import com.kyra.account.controller.AccountController;
import com.kyra.account.service.impl.AccountProxyImpl;
import com.kyra.common.proxy.AccountProxy;
import com.kyra.common.verticle.ApiVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.serviceproxy.ServiceBinder;

public class AccountVerticle extends ApiVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(AccountVerticle.class.getName());
    initHttpServer()
      .compose(this::initControllers)
      .compose(this::publishServices)
      .onFailure(super::onInitFailure)
      .onComplete(startPromise::handle);
  }

  private Future<Void> initControllers(Router router) {
    new AccountController(vertx, router, sessionStore);
    return Future.succeededFuture();
  }

  private Future<Router> initHttpServer() {
    Future<Router> future = Promise.<Router>promise().future();
    launchHttpServer(AccountVerticle.class.getName(), config().getInteger("port", 3001), ar -> {
      if (ar.failed()) {
        log.error(String.format("%s failed to start http server", AccountVerticle.class.getName()), ar.cause());
        future.handle(Future.failedFuture(ar.cause()));
      } else {
        log.info(String.format("%s service successfully starts", AccountVerticle.class.getName()));
        future.handle(Future.succeededFuture(router));
      }
    });

    return future;
  }

  private Future<Void> publishServices(Void unused) {
    AccountProxy accountProxy = new AccountProxyImpl();
    new ServiceBinder(vertx).setAddress(AccountProxy.SERVICE_ADDRESS).register(AccountProxy.class, accountProxy);
    return Future.succeededFuture(unused);
  }
}
