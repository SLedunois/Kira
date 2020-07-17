package com.kyra.project;

import com.kyra.common.verticle.ApiVerticle;
import com.kyra.project.controller.ProjectController;
import io.vertx.core.Promise;
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;

public class ProjectVerticle extends ApiVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(ProjectVerticle.class.getName());
    openApiRouter("Project.v1.yaml", openr -> {
      if (openr.failed())
        log.error(String.format("[%s] Unable to retrieve open api router description", ProjectVerticle.class.getName()), openr.cause());
      else {
        OpenAPI3RouterFactory router = openr.result();
        new ProjectController(vertx, router, sessionStore);
        launchHttpServer(ProjectVerticle.class.getName(), router, config().getInteger("port", 3002), ar -> {
          if (ar.failed())
            log.error(String.format("[%s] Failed to start http server", ProjectVerticle.class.getName()), ar.cause());
          else {
            log.info(String.format("%s service successfully starts", ProjectVerticle.class.getName()));
            startPromise.complete();
          }
        });
      }
    });
  }
}
