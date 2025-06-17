package ru.netology.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.netology.model.Post;
import ru.netology.service.PostService;
import ru.netology.exception.NotFoundException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;
import java.io.PrintWriter;

@Controller
public class PostController {
  public static final String APPLICATION_JSON = "application/json";
  private final PostService service;
  private static final Gson gson = new Gson();
  @Autowired
  public PostController(PostService service) {
    this.service = service;
  }

  public void all(HttpServletResponse response) throws IOException {
    writeJson(response, service.all());
  }

  public void getById(long id, HttpServletResponse response) {
    try {
      writeJson(response, service.getById(id));
    } catch (NotFoundException e) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (IOException e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  public void save(Reader body, HttpServletResponse response) throws IOException {
    try {
      Post post = gson.fromJson(body, Post.class);
      writeJson(response, service.save(post));
    } catch (NotFoundException e) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
  }

  public void removeById(long id, HttpServletResponse response) {
    try {
      service.removeById(id);
      response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    } catch (NotFoundException e) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
  }
  private void writeJson(HttpServletResponse response, Object data) throws IOException {
    response.setContentType(APPLICATION_JSON);
    PrintWriter writer = response.getWriter();
    writer.print(gson.toJson(data));
    writer.flush();
  }
}