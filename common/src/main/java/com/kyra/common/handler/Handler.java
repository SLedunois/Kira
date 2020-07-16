package com.kyra.common.handler;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;

public class Handler {

  static void publish(Vertx vertx, String address, String action, Object data) {
    DeliveryOptions opts = new DeliveryOptions();
    opts.addHeader("action", action);

    vertx.eventBus().publish(address, data, opts);
  }
}
