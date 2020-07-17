package com.kyra.kanban.handler;

import com.kyra.common.handler.ProjectHandler;
import com.kyra.common.pg.Pg;
import com.kyra.kanban.KanbanVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.sqlclient.Tuple;

import java.util.Arrays;
import java.util.List;


public class ProjectHandlerImpl implements ProjectHandler {
  private final Logger log = LoggerFactory.getLogger(ProjectHandlerImpl.class);
  private final List<String> DEFAULT_COLUMNS = Arrays.asList("To do", "In progress", "Done");

  @Override
  public void onCreation(JsonObject project) {
    Integer projectId = project.getInteger("id");
    String query = String.format("INSERT INTO %s.activity (name, position, project_id) VALUES ", Pg.getInstance().schema());

    for (int i = 0; i < DEFAULT_COLUMNS.size(); i++) {
      query += String.format("('%s', %d, %d),", DEFAULT_COLUMNS.get(i), i + 1, projectId);
    }
    query = query.substring(0, query.length() - 1);
    Pg.getInstance().query(query, ar -> {
      if (ar.failed()) {
        log.error(String.format("[%s] Unable to init default project activity for project %d", KanbanVerticle.class.getName(), projectId), ar.cause());
      }
    });
  }

  @Override
  public void onDeletion(Integer projectId) {
    String query = String.format("DELETE FROM %s.activity WHERE project_id = $1", Pg.getInstance().schema());
    Pg.getInstance().preparedQuery(query, Tuple.of(projectId), ar -> {
      if (ar.failed()) {
        log.error(String.format("[%s] Failed to delete activities for project %d", KanbanVerticle.class.getName(), projectId), ar.cause());
      }
    });
  }
}
