package com.kyra.account.router;

public enum Route {
  HANDLEBARS_TEMPLATES(".+\\.hbs"),
  INDEX("/"),
  SIGN_IN("/account/sign-in"),
  SIGN_OUT("/account/sign-out"),
  SIGN_UP("/account/sign-up"),
  STATIC("/account/static/*");

  private final String path;

  Route(String path) {
    this.path = path;
  }

  public String path() {
    return this.path;
  }
}
