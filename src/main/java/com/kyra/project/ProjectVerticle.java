package com.kyra.project;

import com.kyra.common.verticle.ApiVerticle;
import io.vertx.core.Promise;

public class ProjectVerticle extends ApiVerticle {

  String NAME = "Project";
  int PORT = 3001;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start();
    launchHttpServer(NAME, PORT, ar -> {
      log.info(String.format("%s verticle successfully starts", NAME));
      startPromise.complete();
    });
  }
}
