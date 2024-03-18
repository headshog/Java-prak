package work.webprak.DAO.impl;

import org.springframework.stereotype.Repository;

import work.webprak.DAO.Pair;
import work.webprak.DAO.PostsHistoryDAO;
import work.webprak.models.Workers;
import work.webprak.models.PostsHistory;
import work.webprak.models.Subdivisions;
import work.webprak.models.Posts;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class PostsHistoryDAOImpl extends CommonDAOImpl<PostsHistory, Long> implements PostsHistoryDAO {
    public PostsHistoryDAOImpl() {
        super(PostsHistory.class);
    }

    @Override
    public List<Workers> getWorkersFromSubdivision(Long subd_id, boolean cur) {
        List<Workers> ret = new ArrayList<>();
        for (PostsHistory posts_hist : getAll()) {
            if (Objects.equals(posts_hist.getSubdivision().getId(), subd_id) &&
                (!cur || (Objects.equals(posts_hist.getWork_end(), null)))) {
                ret.add(posts_hist.getWorker());
            }
        }
        return ret;
    }

    @Override
    public List<Workers> getWorkersFromPosts(Long post_id, boolean cur) {
        List<Workers> ret = new ArrayList<>();
        for (PostsHistory posts_hist : getAll()) {
            if (Objects.equals(posts_hist.getPost().getId(), post_id) &&
                (!cur || (Objects.equals(posts_hist.getWork_end(), null)))) {
                ret.add(posts_hist.getWorker());
            }
        }
        return ret;
    }

    @Override
    public List<Pair<Posts, Subdivisions>> getWorkerHistory(Long worker_id) {
        List<Pair<Posts, Subdivisions>> ret = new ArrayList<>();
        for (PostsHistory posts_hist : getAll()) {
            if (Objects.equals(posts_hist.getWorker().getId(), worker_id)) {
                ret.add(new Pair<Posts, Subdivisions>(posts_hist.getPost(), posts_hist.getSubdivision()));
            }
        }
        return ret;
    }
}
