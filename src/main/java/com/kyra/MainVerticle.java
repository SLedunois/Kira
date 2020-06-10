package com.kyra;

import com.kyra.auth.AuthVerticle;
import com.kyra.project.ProjectVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

import java.util.Arrays;
import java.util.List;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    List<String> services = Arrays.asList(AuthVerticle.class.getName(), ProjectVerticle.class.getName());
    for (String service : services) {
      vertx.deployVerticle(service);
    }

    startPromise.complete();
  }
}
