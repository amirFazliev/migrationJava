package hm.repository;

import hm.model.Post;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;


@Repository
public class PostRepository {
    private final ConcurrentMap<Long, ConcurrentMap<Post, Boolean>> postRepoMap = new ConcurrentHashMap<>();
    private final AtomicLong numberPost = new AtomicLong(0);


    public List<Post> all() {
        List<Post> postList = new ArrayList<>();
        for (ConcurrentMap<Post, Boolean> value : postRepoMap.values()) {
            for (Post post : value.keySet()) {
                if (value.get(post).equals(true)) {
                    postList.add(post);
                }
            }
        }
        return postList;
    }

    public Optional<Post> getById(long id) {
        for (Post post : postRepoMap.get(id).keySet()) {
            if (postRepoMap.get(id).get(post).equals(true)) {
                return Optional.of(post);
            }
        }
        return Optional.empty();
    }

    public Post save(Post post) {
        ConcurrentMap<Post, Boolean> postBooleanConcurrentMap = new ConcurrentHashMap<>();
        postBooleanConcurrentMap.put(post, true);
        postRepoMap.put(numberPost.incrementAndGet(), postBooleanConcurrentMap);
        System.out.println("Post (id=" + numberPost.get() + ", content=" + post.getContent() + ")");
        return post;
    }

    public void removeById(long id) {
        for (Post post : postRepoMap.get(id).keySet()) {
            postRepoMap.get(id).put(post, false);
        }
    }

    public ConcurrentMap<Long, ConcurrentMap<Post, Boolean>> getPostRepoMap() {
        return postRepoMap;
    }
}

