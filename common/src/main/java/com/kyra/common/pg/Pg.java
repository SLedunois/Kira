package com.kyra.common.pg;

import io.vertx.core.*;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.*;

import java.util.ArrayList;
import java.util.List;

public class Pg {
  private PgPool client;

  private static class PgHolder {
    private static final Pg instance = new Pg();
  }

  public void init(Vertx vertx, String host, Integer port, String database, String user, String password, Integer poolSize) {
    PgConnectOptions connectOptions = new PgConnectOptions()
      .setHost(host)
      .setPort(port)
      .setDatabase(database)
      .setUser(user)
      .setPassword(password);
    PoolOptions poolOptions = new PoolOptions().setMaxSize(poolSize);
    client = PgPool.pool(vertx, connectOptions, poolOptions);
  }

  public static Pg getInstance() {
    return PgHolder.instance;
  }

  public void query(String query, Handler<AsyncResult<RowSet<Row>>> handler) {
    client.getConnection(ar -> {
      if (ar.failed()) {
        handler.handle(Future.failedFuture(ar.cause()));
        return;
      }

      SqlConnection connection = ar.result();
      connection.query(query)
        .execute(q -> {
          handler.handle(q);
          connection.close();
        });
    });
  }

  public void preparedQuery(String sql, Tuple arguments, Handler<AsyncResult<RowSet<Row>>> handler) {
    client.getConnection(ar -> {
      if (ar.succeeded()) {
        SqlConnection connection = ar.result();
        connection.preparedQuery(sql)
          .execute(arguments, res -> {
            if (res.failed()) {
              handler.handle(Future.failedFuture(res.cause()));
            } else {
              handler.handle(Future.succeededFuture(res.result()));
            }

            connection.close();
          });
      } else {
        handler.handle(Future.failedFuture(ar.cause()));
      }
    });
  }

  public void transaction(List<Statement> queries, Handler<AsyncResult<Void>> handler) {
    client.getConnection(ar -> {
      if (ar.failed()) {
        handler.handle(Future.failedFuture(ar.cause()));
        return;
      }

      List<Future> futures = new ArrayList<>();
      SqlConnection connection = ar.result();
      Transaction transaction = connection.begin();

      for (Statement query : queries) {
        Future future = Promise.promise().future();
        futures.add(future);
        if (query.prepared()) connection.preparedQuery(query.query()).execute(query.params(), future);
        else connection.query(query.query()).execute(future);
      }

      CompositeFuture.all(futures)
        .onSuccess(qs -> {
          transaction.commit(tx -> {
            if (tx.failed()) handler.handle(Future.failedFuture(tx.cause()));
            else handler.handle(Future.succeededFuture(tx.result()));
            connection.close();
          });
        })
        .onFailure(fail -> {
          handler.handle(Future.failedFuture(fail));
          connection.close();
        });
    });
  }

}
