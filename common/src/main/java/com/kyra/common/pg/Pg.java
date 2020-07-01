package com.kyra.common.pg;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.*;

import java.util.List;

public class Pg {
  private PgPool client;
  private String schema;

  private static class PgHolder {
    private static final Pg instance = new Pg();
  }

  public void setSchema(String schema) {
    this.schema = schema;
  }

  public String schema() {
    return this.schema;
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

  private void launchTransaction(SqlConnection connection, Transaction ctx, List<Statement> statements, Handler<AsyncResult<Void>> handler) {
    Statement query = statements.get(0);

    Handler<AsyncResult<RowSet<Row>>> qHandler = ar -> {
      if (ar.failed()) {
        ctx.rollback();
        handler.handle(Future.failedFuture(ar.cause()));
        connection.close();
      } else {
        statements.remove(0);

        if (!statements.isEmpty()) launchTransaction(connection, ctx, statements, handler);
        else {
          ctx.commit(cr -> {
            if (cr.failed()) {
              ctx.rollback();
              handler.handle(cr);
            } else {
              handler.handle(Future.succeededFuture());
            }

            connection.close();
          });
        }
      }
    };

    if (query.prepared()) connection.preparedQuery(query.query()).execute(query.params(), qHandler);
    else connection.query(query.query()).execute(qHandler);
  }

  public void transaction(List<Statement> queries, Handler<AsyncResult<Void>> handler) {
    client.getConnection(ar -> {
      if (ar.failed()) {
        handler.handle(Future.failedFuture(ar.cause()));
        return;
      }

      SqlConnection connection = ar.result();
      Transaction transaction = connection.begin();
      launchTransaction(connection, transaction, queries, handler);
    });
  }

  public String preparedList(List<Object> list) {
    StringBuilder builder = new StringBuilder("(");
    for (int i = 0; i < list.size(); i++) {
      builder.append(String.format("$%d,", i));
    }

    String array = builder.substring(0, list.size() - 1);
    array += ")";
    return array;
  }

}
