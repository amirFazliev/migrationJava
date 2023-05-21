package hm.service;

import hm.exception.NotFoundException;
import hm.model.Post;
import hm.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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
    return repository.getById(id).orElseThrow(NotFoundException::new);
  }

  public Post save(Post post) {
    if (checkPost(post)) {
      System.out.println("Одинаковые модели Post (id=" +post.getId() + ", content=" + post.getContent() + ") - ошибка - не сохранено");
      return null;
    }
    post.setId(numberForSave.incrementAndGet());
    return repository.save(post);
  }

  public void removeById(long id) {
    System.out.println("id " + id);
    if (checkId(id)) {
      repository.removeById(id);
      System.out.println("Id " + id + " удачно удален");
    } else {
      throw new NotFoundException("Ошибка удаления id -> id " + id + " не существует");
    }
  }

  private boolean checkPost (Post post) {
    for (Post value : repository.all()) {
      if (value.equals(post)) {
        return true;
      } else {
        if (value.getId()!=post.getId() && (value.getContent().equals(post.getContent()))) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean checkId (long id) {
    for (Long aLong : repository.getPostRepoMap().keySet()) {
      if (aLong.equals(id)) {
        return true;
      }
    }
    return false;
  }
}

