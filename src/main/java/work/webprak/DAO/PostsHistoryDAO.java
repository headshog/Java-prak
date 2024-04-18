package work.webprak.DAO;

import work.webprak.models.PostsHistory;
import work.webprak.models.Workers;

import java.util.List;

public interface PostsHistoryDAO extends CommonDAO<PostsHistory, Long> {
    // Search for all (or which are working right now) Workers
    // from current Subdivision (for Subdivisions page subtables).
    List<Workers> getWorkersFromSubdivision(Long subd_id, boolean cur);
    // Search for all (or which are working right now) PostHistory entries
    // from current Subdivision (for Subdivisions page subtables).
    List<PostsHistory> getPostsHistoryFromSubdivision(Long subd_id, boolean cur);
    // Search for all (or which are working right now) Workers
    // who holds current Post (for search at Workers page).
    List<Workers> getWorkersFromPosts(Long post_id, boolean cur);
    // Search for all pairs of Posts and Subdivisions that current
    // Worker ever held (for Workers page history subtables).
    List<PostsHistory> getWorkerHistory(Long worker_id);
}
