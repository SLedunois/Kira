package com.kyra.common.bean;

import com.kyra.common.pg.Pg;
import com.kyra.common.pg.PgResult;
import com.kyra.common.pg.Statement;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.sqlclient.Tuple;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DB {
  private static final Logger log = LoggerFactory.getLogger(DB.class);
  private Vertx vertx;
  private String microservice;
  private String schema;

  public DB(Vertx vertx, String microservice, String schema) {
    this.vertx = vertx;
    this.microservice = microservice;
    this.schema = schema;
  }

  public void loadScripts() {
    if (schema == null) {
      log.info(String.format("[%s] null schema db, skipping", microservice));
      return;
    }

    checkSchema(ar -> {
      if (ar.failed()) log.error(String.format("[%s]: Failed to check %s schema", microservice, schema), ar.cause());
      else retrieveScripts();
    });
  }

  private void checkSchema(Handler<AsyncResult<Void>> handler) {
    String query = "SELECT schema_name FROM information_schema.schemata WHERE schema_name = $1;";
    Pg.getInstance().preparedQuery(query, Tuple.of(schema), PgResult.uniqueJsonResult(ar -> {
      if (ar.failed()) {
        handler.handle(Future.failedFuture(ar.cause()));
        return;
      }

      if (Objects.isNull(ar.result())) {
        log.info(String.format("[%s] %s schema not found. Start creation process", microservice, schema));
        List<Statement> statements = new ArrayList<>();
        statements.add(schemaCreation());
        statements.add(scriptTableCreation());
        Pg.getInstance().transaction(statements, handler);
      } else handler.handle(Future.succeededFuture());
    }));
  }

  private void retrieveScripts() {
    String query = String.format("SELECT name, passed FROM %s.scripts ORDER BY passed", schema);
    Pg.getInstance().query(query, PgResult.jsonResult(ar -> {
      if (ar.failed()) {
        log.error(String.format("[%s] Unable to retrieve scripts", microservice), ar.cause());
        return;
      }

      List<String> proceedScripts = ar.result().stream().map(s -> s.getString("name")).collect(Collectors.toList());
      vertx.fileSystem().readDir("sql", rd -> {
        if (rd.failed()) {
          log.error(String.format("[%s] Failed to read sql dir", microservice));
          return;
        }

        processScript(rd.result(), proceedScripts, 0);
      });
    }));
  }

  private void processScript(List<String> scripts, List<String> proceedScripts, final int index) {
    String script = scripts.get(index);
    String filename = script.split("/")[script.split("/").length - 1];
    if (proceedScripts.contains(filename)) {
      if (scripts.size() < index) processScript(scripts, proceedScripts, index + 1);
      return;
    }

    vertx.fileSystem().readFile(script, evt -> {
      if (evt.failed()) {
        log.error(String.format("[%s] Failed to read sql script %s", microservice, script), evt.cause().getMessage());
        return;
      }

      String insertQuery = String.format("INSERT INTO %s.scripts (name) VALUES ('%s');", schema, filename);
      String transaction = "BEGIN; " + evt.result().toString(StandardCharsets.UTF_8) + insertQuery + " COMMIT;";
      Pg.getInstance().query(transaction, res -> {
        if (res.failed()) {
          log.error(String.format("[%s] Failed to process script %s", microservice, script), res.cause().getMessage());
          return;
        }

        log.info(String.format("[%s] %s proceed", microservice, script));
        if (scripts.size() < index) processScript(scripts, proceedScripts, index + 1);
      });
    });
  }

  private Statement schemaCreation() {
    return new Statement(String.format("CREATE schema %s;", schema));
  }

  private Statement scriptTableCreation() {
    return new Statement(String.format("CREATE TABLE %s.scripts (name character varying NOT NULL PRIMARY KEY, passed timestamp without time zone NOT NULL DEFAULT now());", schema));
  }
}
