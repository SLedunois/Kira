package com.kyra.auth.router;

public enum Route {
  HANDLEBARS_TEMPLATES(".+\\.hbs"),
  INDEX("/"),
  SIGN_IN("/auth/sign-in"),
  SIGN_OUT("/auth/sign-out"),
  SIGN_UP("/auth/sign-up"),
  STATIC("/auth/static/*");

  private final String path;

  Route(String path) {
    this.path = path;
  }

  public String path() {
    return this.path;
  }
}
