package hm.service;

import hm.exception.NotFoundException;
import hm.model.Post;
import hm.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PostService {
  private final PostRepository repository;
  private final AtomicLong numberForSave = new AtomicLong(0);


  public PostService(PostRepository repository) {
    this.repository = repository;
  }

  public List<Post> all() {
    return repository.all();
  }

  public Post getById(long id) {
    try {
      return repository.getById(id).get();
    } catch (NotFoundException exception) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "id " + id + " not found", exception);
    }
  }

  public Post save(Post post) {
    if (checkPost(post)) {
      System.out.println("Одинаковые модели Post (content=" + post.getContent() + ") - ошибка - не сохранено");
      return null;
    }
    post.setId(numberForSave.incrementAndGet());
    return repository.save(post);
  }

  public void removeById(long id) {
    if (repository.getPostRepoMap().containsKey(id) && repository.getPostRepoMap().get(id).containsValue(true)) {
      repository.removeById(id);
      System.out.println("Id " + id + " удачно удален");
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ошибка удаления id -> id " + id, new NotFoundException());
    }
  }

  private boolean checkPost (Post post) {
    for (ConcurrentMap<Post, Boolean> valueMap : repository.getPostRepoMap().values()) {
      for (Post value : valueMap.keySet()) {
        if (value.equals(post)) {
          return true;
        } else {
          if (value.getContent().equals(post.getContent())) {
            return true;
          }
        }
      }
    }
    return false;
  }

}

