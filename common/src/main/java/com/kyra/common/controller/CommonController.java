package com.kyra.common.controller;

import com.kyra.common.session.AuthSessionHandler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.sstore.SessionStore;

public class CommonController {
  protected Logger log = LoggerFactory.getLogger(CommonController.class);
  protected final AuthSessionHandler sessionHandler = new AuthSessionHandler();
  protected final SessionStore sessionStore;

  public CommonController(SessionStore sessionStore) {
    this.sessionStore = sessionStore;
    sessionHandler.setSessionStore(sessionStore);
  }
}
