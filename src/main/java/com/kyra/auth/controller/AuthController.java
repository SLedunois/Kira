package com.kyra.auth.controller;

import com.kyra.auth.bean.Salt;
import com.kyra.auth.constant.AuthCookie;
import com.kyra.auth.handler.AuthFormLoginHandler;
import com.kyra.auth.provider.PgAuthProvider;
import com.kyra.auth.router.Route;
import com.kyra.auth.service.AuthService;
import com.kyra.auth.service.impl.AuthServiceImpl;
import com.kyra.common.utils.Render;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.http.Cookie;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.common.template.TemplateEngine;
import io.vertx.ext.web.handler.FormLoginHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.TemplateHandler;
import io.vertx.ext.web.sstore.SessionStore;
import io.vertx.ext.web.templ.handlebars.HandlebarsTemplateEngine;

public class AuthController {
  private final AuthService authService = new AuthServiceImpl();
  private final FormLoginHandler formLoginHandler;
  private final Router router;
  private final SessionStore sessionStore;
  private final TemplateEngine templateEngine;

  public AuthController(Vertx vertx, Router router, SessionStore sessionStore) {
    this.sessionStore = sessionStore;
    AuthProvider authProvider = new PgAuthProvider();
    formLoginHandler = new AuthFormLoginHandler()
      .setSessionStore(sessionStore)
      .setAuthProvider(authProvider)
      .setUsernameParam("email")
      .setPasswordParam("password")
      .setDirectLoggedInOKURL(Route.INDEX.path());
    this.router = router;
    templateEngine = HandlebarsTemplateEngine.create(vertx);
    //TODO Create error handler and add it to index, login and register page
  }

  public void initRouter() {
    router.get(Route.STATIC.path()).handler(StaticHandler.create("static"));
    router.getWithRegex(Route.HANDLEBARS_TEMPLATES.path()).handler(TemplateHandler.create(templateEngine));
    router.get(Route.SIGN_UP.path()).handler(this::signUp);
    router.post(Route.SIGN_UP.path()).handler(this::registerRequestValidation).handler(this::register);
    router.get(Route.SIGN_IN.path()).handler(this::sessionCheck).handler(this::signIn);
    router.post(Route.SIGN_IN.path()).handler(formLoginHandler);
    router.get(Route.SIGN_OUT.path()).handler(this::signOut);
  }

  /**
   * Check if session exists.
   * If session exists redirect to index page
   * else call next middleware
   *
   * @param rc Current routing context
   */
  private void sessionCheck(RoutingContext rc) {
    Cookie cookie = rc.getCookie(AuthCookie.NAME);
    if (cookie == null) {
      rc.next();
      return;
    }

    sessionStore.get(cookie.getValue(), ar -> {
      if (ar.failed() || ar.result() == null) rc.next();
      else Render.redirect(rc, Route.INDEX.path());
    });
  }

  /**
   * Check register request validity. Check if first name, last name, email and password are not null.
   * In case of one of those parameter is null or empty string, it returns a bad request error code.
   * Otherwise, it calls the next handler to process register request.
   *
   * @param rc Current routing context
   */
  private void registerRequestValidation(RoutingContext rc) {
    MultiMap body = rc.request().formAttributes();
    String firstName = body.get("firstName");
    String lastName = body.get("lastName");
    String email = body.get("email");
    String password = body.get("password");

    if (firstName == null || "".equals(firstName.trim()) || lastName == null || "".equals(lastName.trim())
      || email == null && "".equals(email.trim()) || password == null || "".equals(password.trim())) {
      Render.badRequest(rc);
      return;
    }

    rc.next();
  }

  private void register(RoutingContext rc) {
    MultiMap body = rc.request().formAttributes();
    String firstName = body.get("firstName");
    String lastName = body.get("lastName");
    String email = body.get("email");
    String password = body.get("password");
    authService.register(email, firstName, lastName, new Salt(password).SHA1(), ar -> {
      if (ar.failed()) Render.internalServerError(rc);
      else Render.created(rc, ar.result());
    });
  }

  private void signIn(RoutingContext rc) {
    rc.put("title", "Sign in");
    rc.reroute("/auth/sign_in.hbs");
  }

  private void signUp(RoutingContext rc) {
    rc.put("title", "Sign up");
    rc.reroute("/auth/sign_up.hbs");
  }

  /**
   * Sign out current user. Delete session in store and remove current cookie.
   *
   * @param rc Current routing Context
   */
  private void signOut(RoutingContext rc) {
    Cookie cookie = rc.getCookie(AuthCookie.NAME);
    if (cookie == null) {
      Render.redirect(rc, Route.SIGN_IN.path());
      return;
    }

    sessionStore.delete(cookie.getValue(), ar -> {
      if (ar.failed()) {
        Render.internalServerError(rc);
      } else {
        rc.removeCookie(AuthCookie.NAME, true);
        Render.redirect(rc, Route.SIGN_IN.path());
      }
    });
  }
}
