package com.kyra.account.service.impl;

import com.kyra.account.service.AccountService;
import com.kyra.common.bean.Field;
import com.kyra.common.pg.Pg;
import com.kyra.common.pg.PgResult;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Tuple;

import java.util.List;

public class AccountServiceImpl implements AccountService {
  @Override
  public void register(String email, String firstName, String lastName, String password, Handler<AsyncResult<JsonObject>> handler) {
    String query = String.format("INSERT INTO account.user(email, %s, %s, password) " +
      "VALUES ($1, $2, $3, $4) RETURNING email, %s, %s;", Field.first_name.name(), Field.last_name.name(), Field.first_name.name(), Field.last_name.name());
    Tuple params = Tuple.of(email, firstName, lastName, password);
    Pg.getInstance().preparedQuery(query, params, PgResult.uniqueJsonResult(handler));
  }

  @Override
  public void findUser(String email, Handler<AsyncResult<JsonObject>> handler) {
    String query = "SELECT email FROM account.user WHERE email = $1";
    Pg.getInstance().preparedQuery(query, Tuple.of(email), PgResult.uniqueJsonResult(handler));
  }

  @Override
  public void search(String query, Handler<AsyncResult<List<JsonObject>>> handler) {
    String sql = "SELECT email, first_name, last_name FROM account.user " +
      "WHERE search_field LIKE $1";
    Pg.getInstance().preparedQuery(sql, Tuple.of("%" + query + "%"), PgResult.jsonResult(handler));
  }
}
