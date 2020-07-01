package com.kyra.common.proxy;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.List;

@ProxyGen
@VertxGen
public interface AccountProxy {

  String SERVICE_NAME = "account-service";
  String SERVICE_ADDRESS = "account.service";

  static AccountProxy createProxy(Vertx vertx) {
    return new AccountProxyVertxEBProxy(vertx, SERVICE_ADDRESS);
  }

  void retrieve(List<String> emails, Handler<AsyncResult<List<JsonObject>>> handler);
}
