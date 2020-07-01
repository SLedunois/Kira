package com.kyra.project.service.impl;

import com.kyra.common.bean.UserImpl;
import com.kyra.common.pg.Pg;
import com.kyra.common.pg.PgResult;
import com.kyra.common.pg.Statement;
import com.kyra.project.service.ProjectService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.auth.User;
import io.vertx.sqlclient.Tuple;

import java.util.ArrayList;
import java.util.List;

public class ProjectServiceImpl implements ProjectService {
  Logger log = LoggerFactory.getLogger(ProjectServiceImpl.class);

  @Override
  public void list(User user, Integer sort, Handler<AsyncResult<List<JsonObject>>> handler) {
    String email = ((UserImpl) user).email();
    String sortFilter = "";
    if (sort != null) sortFilter = String.format(" ORDER BY name %s", sort == 1 ? "ASC" : "DESC");
    String query = "WITH user_projects AS (" +
      "SELECT id " +
      "FROM %s.project " +
      "WHERE owner = $1 " +
      "UNION " +
      "SELECT %s.id " +
      "FROM %s.project " +
      "INNER JOIN project.project_member ON (%s.id = project_member.project_id) " +
      "WHERE project_member.email = $1)" +
      "SELECT %s.*, json_agg(project_member.*) as members " +
      "FROM %s.project " +
      "INNER JOIN %s.project_member ON (%s.id = project_member.project_id) " +
      "INNER JOIN user_projects ON (user_projects.id = %s.id) " +
      "GROUP BY %s.id %s";
    String schema = Pg.getInstance().schema();
    String formattedQuery = String.format(query, schema, schema, schema, schema, schema, schema, schema, schema, schema, schema, sortFilter);
    Pg.getInstance().preparedQuery(formattedQuery, Tuple.of(email), PgResult.jsonResult(handler));
  }

  @Override
  public void update(int id, JsonObject project, Handler<AsyncResult<JsonObject>> handler) {
    List<Statement> statements = new ArrayList<>();
    statements.add(membersDeletion(id));
    statements.add(projectUpdate(id, project));
    if (!project.getJsonArray("members", new JsonArray()).isEmpty())
      statements.add(membersInsertion(id, project.getJsonArray("members")));
    Pg.getInstance().transaction(statements, ar -> {
      if (ar.failed()) handler.handle(Future.failedFuture(ar.cause()));
      else handler.handle(Future.succeededFuture(project.put("id", id)));
    });
  }

  private Statement projectUpdate(int id, JsonObject project) {
    String query = String.format("UPDATE %s.project SET name = $1 WHERE id = $2;", Pg.getInstance().schema());
    return new Statement(query, Tuple.of(project.getString("name", ""), id));
  }

  private Statement membersDeletion(int id) {
    String query = String.format("DELETE FROM %s.project_member WHERE project_id = $1;", Pg.getInstance().schema());
    return new Statement(query, Tuple.of(id));
  }

  @Override
  public void findProjectByIdAndUser(int id, User user, Handler<AsyncResult<Void>> handler) {
    String email = ((UserImpl) user).email();
    String query = String.format("SELECT id FROM %s.project WHERE id = $1 AND owner = $2", Pg.getInstance().schema());
    Pg.getInstance().preparedQuery(query, Tuple.of(id, email), PgResult.uniqueJsonResult(ar -> {
      if (ar.failed() || ar.result() == null) handler.handle(Future.failedFuture("Not found"));
      else handler.handle(Future.succeededFuture());
    }));
  }

  @Override
  public void create(JsonObject project, User user, Handler<AsyncResult<JsonObject>> handler) {
    String query = String.format("SELECT nextval('%s.project_id_seq') as id", Pg.getInstance().schema());
    Pg.getInstance().query(query, PgResult.uniqueJsonResult(idr -> {
      if (idr.failed() || idr.result() == null) {
        log.error("Unable to select next project val", idr.cause());
        handler.handle(Future.failedFuture(idr.cause()));
        return;
      }

      int projectId = idr.result().getInteger("id");
      List<Statement> statements = new ArrayList<>();
      statements.add(projectCreation(projectId, project, user));
      JsonArray members = project.getJsonArray("members", new JsonArray());
      if (!members.isEmpty()) {
        statements.add(membersInsertion(projectId, members));
      }

      Pg.getInstance().transaction(statements, tx -> {
        if (tx.failed()) {
          log.error("Failed to create project", project.encode(), tx.cause());
          handler.handle(Future.failedFuture(tx.cause()));
        } else {
          project.put("id", projectId);
          handler.handle(Future.succeededFuture(project));
        }
      });
    }));
  }

  @Override
  public void delete(int id, Handler<AsyncResult<Void>> handler) {
    String query = String.format("DELETE FROM %s.project WHERE id = $1", Pg.getInstance().schema());
    Pg.getInstance().preparedQuery(query, Tuple.of(id), ar -> {
      if (ar.failed()) handler.handle(Future.failedFuture(ar.cause()));
      else handler.handle(Future.succeededFuture());
    });
  }

  private Statement projectCreation(int id, JsonObject project, User user) {
    String query = "INSERT INTO project.project(id, name, owner) VALUES ($1, $2, $3);";
    Tuple params = Tuple.of(id, project.getString("name", ""), ((UserImpl) user).email());
    return new Statement(query, params);
  }

  private Statement membersInsertion(int id, JsonArray members) {
    StringBuilder query = new StringBuilder(String.format("INSERT INTO %s.project_member(project_id, email) VALUES", Pg.getInstance().schema()));
    for (int i = 0; i < members.size(); i++) {
      query.append(String.format("(%d, '%s'),", id, members.getJsonObject(i).getString("email", "")));
    }
    return new Statement(query.toString().substring(0, query.length() - 1) + ";");
  }

}
