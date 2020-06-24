package com.kyra.common.session;

import com.kyra.common.bean.UserImpl;
import com.kyra.common.utils.Render;
import io.vertx.core.Handler;
import io.vertx.core.http.Cookie;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.sstore.SessionStore;

public class AuthSessionHandler implements Handler<RoutingContext> {
  private SessionStore sessionStore;
  private String redirectUri;

  @Override
  public void handle(RoutingContext rc) {
    Cookie cookie = rc.getCookie(AuthCookie.NAME);
    if (cookie == null) {
      if (redirectUri != null) {
        Render.redirect(rc, redirectUri);
      } else {
        Render.unauthorized(rc);
      }
      return;
    }

    sessionStore.get(cookie.getValue(), ar -> {
      if (ar.failed()) {
        //Invalid cookie or expired cookie
        rc.removeCookie(AuthCookie.NAME, true);
        Render.redirect(rc, redirectUri);
      } else {
        rc.setUser(new UserImpl(((RedisSession) ar.result()).toJSON()));
        rc.next();
      }
    });
  }

  public AuthSessionHandler setRedirectUri(String redirectUri) {
    this.redirectUri = redirectUri;
    return this;
  }

  public AuthSessionHandler setSessionStore(SessionStore sessionStore) {
    this.sessionStore = sessionStore;
    return this;
  }

}
