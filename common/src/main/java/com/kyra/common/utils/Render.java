package com.kyra.common.utils;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class Render {
  public static void redirect(RoutingContext rc, String uri) {
    rc.response()
      .setStatusCode(302)
      .putHeader("Location", uri)
      .end();
  }

  public static void badRequest(RoutingContext rc) {
    rc.response().setStatusCode(400).end();
  }

  public static void renderJson(RoutingContext rc, int statusCode, String value) {
    rc.response().putHeader("content-type", "application/json");
    rc.response().putHeader("Cache-Control", "no-cache, must-revalidate");
    rc.response().putHeader("Expires", "-1");
    rc.response().setStatusCode(statusCode);
    rc.response().end(value);
  }

  public static void created(RoutingContext rc, JsonArray values) {
    renderJson(rc, 201, values.encode());
  }

  public static void created(RoutingContext rc, JsonObject value) {
    renderJson(rc, 201, value.encode());
  }

  public static void unauthorized(RoutingContext rc) {
    rc.response().setStatusCode(401).end();
  }

  public static void internalServerError(RoutingContext rc) {
    rc.response().setStatusCode(500).end();
  }

}
