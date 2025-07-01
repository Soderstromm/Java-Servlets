package ru.netology.servlet;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.netology.controller.PostController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Reader;

public class MainServlet extends HttpServlet {
  private PostController controller;

  @Override
  public void init() {
    ApplicationContext springContext = new ClassPathXmlApplicationContext("/WEB-INF/spring-config.xml");
    controller = springContext.getBean(PostController.class);
  }
  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();
      if (method.equals("GET") && path.equals("/api/posts")) {
        controller.all(resp);
        return;
      }
      if (method.equals("GET") && path.matches("/api/posts/\\d+")) {
        final var id = extractIdFromPath(path);
        controller.getById(id, resp);
        return;
      }
      if (method.equals("POST") && path.equals("/api/posts")) {
        Reader reader = req.getReader();
        controller.save(reader, resp);
        return;
      }
      if (method.equals("DELETE") && path.matches("/api/posts/\\d+")) {
        final var id = extractIdFromPath(path);
        controller.removeById(id, resp);
        return;
      }
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
  private long extractIdFromPath(String path) {
    String[] parts = path.split("/");
    return Long.parseLong(parts[parts.length - 1]);
  }
}