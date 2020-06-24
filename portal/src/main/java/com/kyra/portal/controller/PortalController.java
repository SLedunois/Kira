package com.kyra.portal.controller;

import com.kyra.common.session.AuthSessionHandler;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.sstore.SessionStore;

public class PortalController {
  private Router router;
  private AuthSessionHandler sessionHandler;

  public PortalController(Router router, SessionStore sessionStore) {
    this.router = router;
    sessionHandler = new AuthSessionHandler().setSessionStore(sessionStore).setRedirectUri("/account/sign-in");
    initRouter();
  }

  private void initRouter() {
    router.get().handler(sessionHandler).handler(this::index);
  }

  private void index(RoutingContext rc) {
    rc.response().sendFile("view/index.html");
  }
}
