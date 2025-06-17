package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
public class PostRepository {
  private final Map<Long, Post> storage = new ConcurrentHashMap<>();
  private final AtomicLong idCounter = new AtomicLong(1);

  public List<Post> all() {
    return new ArrayList<>(storage.values());
  }

  public Optional<Post> getById(long id) {
    return Optional.ofNullable(storage.get(id));
  }

  public Post save(Post post) {
    if (post.getId() == 0) {
      long newId = idCounter.getAndIncrement();
      Post newPost = new Post(newId, post.getContent());
      storage.put(newId, newPost);
      return newPost;
    } else {
      if (!storage.containsKey(post.getId())) {
        throw new NotFoundException("Post with ID " + post.getId() + " not found");
      }
      storage.put(post.getId(), post);
      return post;
    }
  }

  public void removeById(long id) {
    if (!storage.containsKey(id)) {
      throw new NotFoundException("Post with ID " + id + " not found");
    }
    storage.remove(id);
  }
}