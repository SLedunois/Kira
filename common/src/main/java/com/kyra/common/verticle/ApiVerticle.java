package com.kyra.common.verticle;

import com.kyra.common.proxy.ProxyHelper;
import com.kyra.common.session.RedisSessionStore;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.sstore.SessionStore;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.EventBusService;

public class ApiVerticle extends MicroserviceVerticle {
  protected ServiceDiscovery discovery;
  protected ProxyHelper proxyHelper;
  protected Logger log = LoggerFactory.getLogger(ApiVerticle.class);
  protected Router router;
  protected SessionStore sessionStore;

  public void start(String microserviceName) throws Exception {
    super.start(microserviceName);
    discovery = ServiceDiscovery.create(vertx);
    proxyHelper = new ProxyHelper(discovery);
    sessionStore = new RedisSessionStore();
  }

  public void launchHttpServer(String name, int port, Handler<AsyncResult<Void>> handler) {
    router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    startHttpServer(name, router, port, handler);
  }

  public void launchHttpServer(String name, OpenAPI3RouterFactory router, int port, Handler<AsyncResult<Void>> handler) {
    startHttpServer(name, router.getRouter(), port, handler);
  }

  private void startHttpServer(String name, Router router, int port, Handler<AsyncResult<Void>> handler) {
    vertx.createHttpServer()
      .requestHandler(router)
      .listen(port, ar -> {
        if (ar.failed()) {
          handler.handle(Future.failedFuture(ar.cause()));
          throw new RuntimeException(String.format("Unable to launch http server on port %n", port), ar.cause());
        }

        log.info(String.format("%s http server successfully launch on port %d", name, port));
        handler.handle(Future.succeededFuture());
      });
  }

  public void publish(String name, String address, Class<?> serviceClass, Handler<AsyncResult<Void>> handler) {
    Record record = EventBusService.createRecord(name, address, serviceClass);
    publish(record, handler);
  }

  public void publish(Record record, Handler<AsyncResult<Void>> handler) {
    if (discovery == null) {
      try {
        start();
      } catch (Exception e) {
        throw new RuntimeException("Cannot create discovery service");
      }
    }

    discovery.publish(record, ar -> {
      if (ar.failed()) handler.handle(Future.failedFuture(ar.cause()));
      else handler.handle(Future.succeededFuture());
    });
  }

  /**
   * Retrieve Open API router based on description file
   *
   * @param file    Description file name
   * @param handler Function handler returning OpenAPI3Router
   */
  protected void openApiRouter(String file, Handler<AsyncResult<OpenAPI3RouterFactory>> handler) {
    OpenAPI3RouterFactory.create(vertx, String.format("openapi/%s", file), handler);
  }
}
