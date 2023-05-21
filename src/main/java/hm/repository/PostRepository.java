package hm.repository;

import hm.exception.NotFoundException;
import hm.model.Post;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
@Repository
public class PostRepository {

  private final ConcurrentMap<Long, Post> postRepoMap = new ConcurrentHashMap<>();
  private final AtomicLong numberPost = new AtomicLong(0);

  public List<Post> all() {
    return new ArrayList<>(postRepoMap.values());
  }

  public Optional<Post> getById(long id) {
    return Optional.ofNullable(postRepoMap.get(id));
  }

  public Post save(Post post) {
    postRepoMap.put(numberPost.incrementAndGet(), post);
    System.out.println("Post (id=" +post.getId() + ", content=" + post.getContent() + ")");
    return post;
  }

  public void removeById(long id) {
    try {
      postRepoMap.remove(id);
    } catch (NotFoundException e) {
      throw new NotFoundException("Ошибка удаления id -> id " + id + " не существует");
    }
  }
  public ConcurrentMap<Long, Post>  getPostRepoMap() {
    return postRepoMap;
  }
}

