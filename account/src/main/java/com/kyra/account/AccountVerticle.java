package com.kyra.account;

import com.kyra.account.controller.AccountController;
import com.kyra.account.service.impl.AccountProxyImpl;
import com.kyra.common.proxy.AccountProxy;
import com.kyra.common.verticle.ApiVerticle;
import io.vertx.core.Promise;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.types.EventBusService;
import io.vertx.serviceproxy.ServiceBinder;

public class AccountVerticle extends ApiVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(AccountVerticle.class.getName());
    launchHttpServer(AccountVerticle.class.getName(), config().getInteger("port", 3001), ar -> {
      new AccountController(vertx, router, sessionStore);
      log.info(String.format("%s service successfully starts", AccountVerticle.class.getName()));
      startPromise.complete();
    });

    publishServices();
  }

  private void publishServices() {
    AccountProxy accountProxy = new AccountProxyImpl();
    new ServiceBinder(vertx).setAddress(AccountProxy.SERVICE_ADDRESS).register(AccountProxy.class, accountProxy);
    Record record = EventBusService.createRecord(AccountProxy.SERVICE_NAME, AccountProxy.SERVICE_ADDRESS, AccountProxy.class);
    publish(record, ar -> {
      if (ar.failed())
        log.error(String.format("[%s] Unable to publish service proxy %s", AccountVerticle.class.getName(), AccountProxy.class.getName()), ar.cause());
    });
  }
}
