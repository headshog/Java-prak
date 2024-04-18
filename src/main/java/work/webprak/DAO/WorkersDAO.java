package work.webprak.DAO;

import work.webprak.models.PostsHistory;
import work.webprak.models.Workers;

import java.util.List;

public interface WorkersDAO extends CommonDAO<Workers, Long> {
    // Search for all Workers by their Experience (for search at Workers page).
    List<Workers> getWorkersByExperience(Long exp);
    // Search for all Workers by their Name (for search at Workers page).
    List<Workers> getWorkersByName(String name);
    // Search for all (or which are working right now) Workers
    // from current Subdivision (for Subdivisions page subtables).
    List<Workers> getWorkersFromSubdivision(Long subd_id, boolean cur);
    // Search for all (or which are working right now) Workers
    // who holds current Post (for search at Workers page).
    List<Workers> getWorkersFromPosts(Long post_id, boolean cur);
    // Search for all pairs of Posts and Subdivisions that current
    // Worker ever held (for Workers page history subtables).
    List<PostsHistory> getWorkerHistory(Long worker_id);
}
