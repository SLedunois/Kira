package com.kyra.common.handler;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;

@ProxyGen
@VertxGen
public interface ProjectHandler {
  String HANDLER_NAME = "project-handler";
  String HANDLER_ADDRESS = "project.handler";

  static void created(Vertx vertx, JsonObject project) {
    DeliveryOptions opts = new DeliveryOptions();
    opts.addHeader("action", "onCreation");

    JsonObject data = new JsonObject()
      .put("project", project);

    vertx.eventBus().publish(HANDLER_ADDRESS, data, opts);
  }

  void onCreation(JsonObject project);

}
