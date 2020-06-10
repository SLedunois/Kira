package com.kyra;

import io.vertx.core.Launcher;

public class KyraLauncher extends Launcher {
  public static void main(String[] args) {
    new KyraLauncher().dispatch(args);
  }
}
