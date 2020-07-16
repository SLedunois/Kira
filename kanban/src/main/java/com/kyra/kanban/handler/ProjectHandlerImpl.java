package com.kyra.kanban.handler;

import com.kyra.common.handler.ProjectHandler;
import com.kyra.common.pg.Pg;
import com.kyra.kanban.KanbanVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.Arrays;
import java.util.List;


public class ProjectHandlerImpl implements ProjectHandler {
  private final Logger log = LoggerFactory.getLogger(ProjectHandlerImpl.class);
  private final List<String> DEFAULT_COLUMNS = Arrays.asList("TODO", "IN PROGRESS", "DONE");

  @Override
  public void onCreation(JsonObject project) {
    Integer projectId = project.getInteger("id");
    String query = String.format("INSERT INTO %s.column (name, project_id) VALUES ", Pg.getInstance().schema());
    for (String column : DEFAULT_COLUMNS) {
      query += String.format("('%s', %d),", column, projectId);
    }

    query = query.substring(0, query.length() - 1);
    Pg.getInstance().query(query, ar -> {
      if (ar.failed()) {
        log.error(String.format("[%s] Unable to init default project column for project %d", KanbanVerticle.class.getName(), projectId));
      }
    });
  }
}
