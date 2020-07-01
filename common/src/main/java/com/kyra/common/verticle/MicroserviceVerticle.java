package com.kyra.common.verticle;

import com.kyra.common.bean.DB;
import com.kyra.common.pg.Pg;
import com.kyra.common.redis.Redis;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class MicroserviceVerticle extends AbstractVerticle {
  protected Logger log = LoggerFactory.getLogger(MicroserviceVerticle.class);
  private String microservice;

  public void start(String microservice) throws Exception {
    super.start();
    this.microservice = microservice;
    initPg();
    initRedis();
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
    String schema = pgConfig.getString("schema", "public");
    int poolSize = pgConfig.getInteger("pool_size", 5);
    Pg.getInstance().init(vertx, host, port, dbName, username, password, poolSize);
    Pg.getInstance().setSchema(schema);
    new DB(vertx, microservice, schema).loadScripts();
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
