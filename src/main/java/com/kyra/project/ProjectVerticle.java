package com.kyra.project;

import com.kyra.common.verticle.ApiVerticle;
import io.vertx.core.Promise;

public class ProjectVerticle extends ApiVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start();
    launchHttpServer(ProjectVerticle.class.getName(), config().getInteger("port", 3002), ar -> {
      log.info(String.format("%s verticle successfully starts", ProjectVerticle.class.getName()));
      startPromise.complete();
    });
  }
}
