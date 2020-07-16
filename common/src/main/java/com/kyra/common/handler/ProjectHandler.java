package com.kyra.common.handler;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

@ProxyGen
@VertxGen
public interface ProjectHandler {
  String HANDLER_NAME = "project-handler";
  String HANDLER_ADDRESS = "project.handler";

  static void created(Vertx vertx, JsonObject project) {
    JsonObject data = new JsonObject()
      .put("project", project);

    Handler.publish(vertx, HANDLER_ADDRESS, "onCreation", data);
  }

  static void deleted(Vertx vertx, Integer projectId) {
    JsonObject data = new JsonObject().put("projectId", projectId);
    Handler.publish(vertx, HANDLER_ADDRESS, "onDeletion", data);
  }

  void onCreation(JsonObject project);

  void onDeletion(Integer projectId);

}
