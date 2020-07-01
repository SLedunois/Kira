package com.kyra.account.service.impl;

import com.kyra.common.pg.Pg;
import com.kyra.common.pg.PgResult;
import com.kyra.common.proxy.AccountProxy;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Tuple;

import java.util.List;

public class AccountProxyImpl implements AccountProxy {
  @Override
  public void retrieve(List<String> emails, Handler<AsyncResult<List<JsonObject>>> handler) {
    StringBuilder emailList = new StringBuilder("(");
    Tuple params = Tuple.tuple();
    for (int i = 0; i < emails.size(); i++) {
      emailList.append(String.format("$%d,", i + 1));
      params.addString(emails.get(i));
    }
    String where = emailList.toString().substring(0, emailList.length() - 1) + ")";
    String query = String.format("SELECT email, first_name, last_name FROM %s.user WHERE email IN %s ORDER BY last_name", Pg.getInstance().schema(), where);
    Pg.getInstance().preparedQuery(query, params, PgResult.jsonResult(handler));
  }
}
