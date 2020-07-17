package com.kyra.common.utils;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class Render {
  public static void redirect(RoutingContext rc, String uri) {
    rc.response()
      .setStatusCode(HttpResponseStatus.FOUND.code())
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
    renderJson(rc, HttpResponseStatus.CREATED.code(), values.encode());
  }

  public static void created(RoutingContext rc, JsonObject value) {
    renderJson(rc, HttpResponseStatus.CREATED.code(), value.encode());
  }

  public static void ok(RoutingContext rc, JsonObject object) {
    renderJson(rc, HttpResponseStatus.OK.code(), object.encode());
  }

  public static void ok(RoutingContext rc, JsonArray array) {
    renderJson(rc, HttpResponseStatus.OK.code(), array.encode());
  }

  public static void unauthorized(RoutingContext rc) {
    rc.response().setStatusCode(HttpResponseStatus.UNAUTHORIZED.code()).end();
  }

  public static void internalServerError(RoutingContext rc) {
    rc.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
  }

  public static void noContent(RoutingContext rc) {
    rc.response().setStatusCode(HttpResponseStatus.NO_CONTENT.code()).end();
  }

  public static void notFound(RoutingContext rc) {
    rc.response().setStatusCode(HttpResponseStatus.NOT_FOUND.code()).end();
  }
}
