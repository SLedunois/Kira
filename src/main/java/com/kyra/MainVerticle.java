package com.kyra;

import com.kyra.auth.AuthVerticle;
import com.kyra.common.pg.Pg;
import com.kyra.common.redis.Redis;
import com.kyra.portal.PortalVerticle;
import com.kyra.project.ProjectVerticle;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainVerticle extends AbstractVerticle {
  Logger log = LoggerFactory.getLogger(MainVerticle.class);
  List<String> services = Arrays.asList(PortalVerticle.class.getName(), AuthVerticle.class.getName(), ProjectVerticle.class.getName());

  @Override
  public void start(Promise<Void> start) throws Exception {
    long startTime = System.currentTimeMillis();
    initPg();
    initRedis();
    List<Future> futures = new ArrayList<>();
    for (String service : services) {
      Future future = Promise.promise().future();
      deployService(service, future);
      futures.add(future);
    }

    CompositeFuture.all(futures)
      .onFailure(throwable -> {
        log.error("Fail to deploy Kyra application", throwable);
        start.fail(throwable);
      })
      .onSuccess(ar -> {
        long launchTime = System.currentTimeMillis() - startTime;
        log.info(String.format("Kyra application successfully starts in %dms", launchTime));
        start.complete();
      });
  }

  private void deployService(String service, Handler<AsyncResult<String>> handler) {
    JsonObject serviceConfig = config().getJsonObject(service, new JsonObject());
    DeploymentOptions options = new DeploymentOptions().setConfig(serviceConfig);
    vertx.deployVerticle(service, options, handler);
  }

  private void initPg() {
    if (!config().getJsonObject("db", new JsonObject()).containsKey("pg")) {
      log.error("Unable to find pg configuration. Please fill it and restart.");
      return;
    }

    JsonObject pgConfig = config().getJsonObject("db", new JsonObject()).getJsonObject("pg", new JsonObject());
    String host = pgConfig.getString("host");
    int port = pgConfig.getInteger("port");
    String dbName = pgConfig.getString("db");
    String username = pgConfig.getString("username");
    String password = pgConfig.getString("password");
    int poolSize = pgConfig.getInteger("pool_size", 5);
    Pg.getInstance().init(vertx, host, port, dbName, username, password, poolSize);
  }

  private void initRedis() {
    String redisConfig = config().getJsonObject("db", new JsonObject()).getString("redis");
    if (redisConfig == null) {
      log.error("Unable to init redis. Configuration is null. Please fill configuration with redis configuration <db.redis>");
      return;
    }

    Redis.getInstance().init(vertx, redisConfig);
  }
}
