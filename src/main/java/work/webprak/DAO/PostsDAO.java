package work.webprak.DAO;

import work.webprak.models.Posts;

import java.util.List;

public interface PostsDAO extends CommonDAO<Posts, Long> {
    // Search for Posts by Name(or substring of Name) on Posts page.
    List<Posts> getPostsByName(String name);
}
