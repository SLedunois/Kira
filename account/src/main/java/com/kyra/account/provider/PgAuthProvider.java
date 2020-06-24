package com.kyra.account.provider;

import com.kyra.account.bean.Salt;
import com.kyra.common.bean.UserImpl;
import com.kyra.common.pg.Pg;
import com.kyra.common.pg.PgResult;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;
import io.vertx.sqlclient.Tuple;

import java.util.Objects;

public class PgAuthProvider implements AuthProvider {
  @Override
  public void authenticate(JsonObject authInfo, Handler<AsyncResult<User>> handler) {
    String email = authInfo.getString("username");
    String password = authInfo.getString("password");
    Salt salt = new Salt(password);
    String query = "SELECT * FROM account.user WHERE email = $1 AND password = $2";
    Pg.getInstance().preparedQuery(query, Tuple.of(email, salt.SHA1()), PgResult.uniqueJsonResult(ar -> {
      if (ar.succeeded() && Objects.nonNull(ar.result())) {
        handler.handle(Future.succeededFuture(new UserImpl(ar.result())));
      } else {
        handler.handle(Future.failedFuture("User not found"));
      }
    }));
  }
}
