package com.kyra.portal.controller;

import com.kyra.auth.handler.AuthSessionHandler;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.sstore.SessionStore;

public class PortalController {
  private Router router;

  public PortalController(Router router, SessionStore sessionStore) {
    this.router = router;
    AuthSessionHandler sessionHandler = new AuthSessionHandler().setSessionStore(sessionStore).setRedirectUri("/auth/sign-in");
    router.get().handler(sessionHandler).handler(this::index);
  }

  private void index(RoutingContext rc) {
    rc.response().sendFile("view/index.html");
  }
}
