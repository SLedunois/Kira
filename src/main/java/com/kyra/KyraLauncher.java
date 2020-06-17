package com.kyra;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Launcher;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class KyraLauncher extends Launcher {

  public static void main(String[] args) {
    new KyraLauncher().dispatch(args);
  }

  @Override
  public void beforeDeployingVerticle(DeploymentOptions deploymentOptions) {
    super.beforeDeployingVerticle(deploymentOptions);

    if (deploymentOptions.getConfig() == null) {
      deploymentOptions.setConfig(new JsonObject());
    }

    File conf = new File("src/conf/default-config.json");
    deploymentOptions.getConfig().mergeIn(getConfiguration(conf));
  }

  private JsonObject getConfiguration(File config) {
    JsonObject conf = new JsonObject();
    if (config.isFile()) {
      System.out.println("Reading config file: " + config.getAbsolutePath());
      try (Scanner scanner = new Scanner(config).useDelimiter("\\A")) {
        String sConf = scanner.next();
        try {
          conf = new JsonObject(sConf);
        } catch (DecodeException e) {
          System.err.println("Configuration file " + sConf + " does not contain a valid JSON object");
        }
      } catch (FileNotFoundException e) {
        // Ignore it.
      }
    } else {
      System.out.println("Config file not found " + config.getAbsolutePath());
    }
    return conf;
  }

}
