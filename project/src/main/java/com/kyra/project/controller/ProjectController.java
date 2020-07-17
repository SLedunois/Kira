package com.kyra.project.controller;

import com.kyra.common.bean.UserImpl;
import com.kyra.common.controller.CommonController;
import com.kyra.common.handler.ProjectHandler;
import com.kyra.common.proxy.AccountProxy;
import com.kyra.common.session.AuthCookie;
import com.kyra.common.utils.Render;
import com.kyra.project.openapi.OperationId;
import com.kyra.project.service.ProjectService;
import com.kyra.project.service.impl.ProjectServiceImpl;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;
import io.vertx.ext.web.sstore.SessionStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProjectController extends CommonController {
  private final Vertx vertx;
  private final AccountProxy accountProxy;
  private final ProjectService projectService = new ProjectServiceImpl();

  public ProjectController(Vertx vertx, OpenAPI3RouterFactory router, SessionStore sessionStore) {
    super(sessionStore);
    this.vertx = vertx;
    this.accountProxy = AccountProxy.createProxy(vertx);
    router.addSecurityHandler(AuthCookie.NAME, sessionHandler);
    router.addHandlerByOperationId(OperationId.ListProjects.name(), this::listProjects);
    router.addHandlerByOperationId(OperationId.CreateProject.name(), this::createProject);
    router.addHandlerByOperationId(OperationId.DeleteProject.name(), this::userCanManage)
      .addHandlerByOperationId(OperationId.DeleteProject.name(), this::deleteProject);
    router.addHandlerByOperationId(OperationId.UpdateProject.name(), this::userCanManage)
      .addHandlerByOperationId(OperationId.UpdateProject.name(), this::updateProject);
  }

  private void updateProject(RoutingContext rc) {
    try {
      int id = Integer.parseInt(rc.pathParam("id"));
      JsonObject project = rc.getBodyAsJson();
      projectService.update(id, project, ar -> {
        if (ar.failed()) Render.internalServerError(rc);
        else Render.ok(rc, ar.result());
      });
    } catch (NumberFormatException e) {
      Render.badRequest(rc);
    }
  }

  private void userCanManage(RoutingContext rc) {
    try {
      int id = Integer.parseInt(rc.pathParam("id"));
      projectService.findProjectByIdAndUser(id, rc.user(), ar -> {
        if (ar.failed()) Render.unauthorized(rc);
        else rc.next();
      });
    } catch (NumberFormatException e) {
      Render.unauthorized(rc);
    }
  }

  private void deleteProject(RoutingContext rc) {
    try {
      int id = Integer.parseInt(rc.pathParam("id"));
      projectService.delete(id, ar -> {
        if (ar.failed()) Render.internalServerError(rc);
        else {
          ProjectHandler.deleted(vertx, id);
          Render.noContent(rc);
        }
      });
    } catch (NumberFormatException e) {
      Render.badRequest(rc);
    }
  }

  private void createProject(RoutingContext rc) {
    JsonObject project = rc.getBodyAsJson();
    project.getJsonArray("members", new JsonArray())
      .add(new JsonObject().put("email", ((UserImpl) rc.user()).email()));
    projectService.create(project, rc.user(), ar -> {
      if (ar.failed()) Render.internalServerError(rc);
      else {
        ProjectHandler.created(vertx, ar.result());
        Render.created(rc, ar.result());
      }
    });
  }

  private void listProjects(RoutingContext rc) {
    try {
      Integer sort = null;
      List<String> sortParams = rc.queryParam("sort");
      if (!sortParams.isEmpty()) {
        sort = Integer.parseInt(sortParams.get(0));
        if (sort != 1 && sort != -1) {
          Render.badRequest(rc);
          return;
        }
      }

      projectService.list(rc.user(), sort, ar -> {
        if (ar.failed()) Render.internalServerError(rc);
        else {
          List<JsonObject> projects = ar.result();
          List<String> emails = new ArrayList<>();
          projects.forEach(project -> {
            JsonArray members = project.getJsonArray("members");
            for (int i = 0; i < members.size(); i++) {
              JsonObject member = members.getJsonObject(i);
              emails.add(member.getString("email"));
            }
          });

          if (emails.isEmpty()) {
            Render.ok(rc, new JsonArray(projects));
            return;
          }

          accountProxy.retrieve(emails, accr -> {
            if (accr.failed()) {
              log.error(String.format("[%s] Failed to retrieve account", accr.cause()));
              Render.internalServerError(rc);
            }

            HashMap<String, JsonObject> userMap = mapUsers(accr.result());
            Render.ok(rc, new JsonArray(squashProjectMembers(projects, userMap)));
          });
        }
      });
    } catch (NumberFormatException e) {
      Render.badRequest(rc);
    }
  }

  private List<JsonObject> squashProjectMembers(List<JsonObject> projects, HashMap<String, JsonObject> users) {
    for (JsonObject project : projects) {
      JsonArray members = new JsonArray();
      project.getJsonArray("members").forEach(m -> {
        JsonObject member = (JsonObject) m;
        if (users.containsKey(member.getString("email"))) {
          members.add(users.get(member.getString("email")));
        }
      });
      project.put("members", members);
    }

    return projects;
  }

  private HashMap<String, JsonObject> mapUsers(List<JsonObject> users) {
    HashMap<String, JsonObject> map = new HashMap();
    users.forEach(user -> map.put(user.getString("email"), user));
    return map;
  }

}
