package work.webprak.DAO.impl;

import org.springframework.stereotype.Repository;

import work.webprak.DAO.PostsHistoryDAO;
import work.webprak.models.Workers;
import work.webprak.models.PostsHistory;

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
                (!cur || (Objects.equals(posts_hist.getWorkEnd(), null)))) {
                ret.add(posts_hist.getWorker());
            }
        }
        return ret;
    }

    @Override
    public List<PostsHistory> getPostsHistoryFromSubdivision(Long subd_id, boolean cur) {
        List<PostsHistory> ret = new ArrayList<>();
        for (PostsHistory posts_hist : getAll()) {
            if (posts_hist.getSubdivision() != null &&
                Objects.equals(posts_hist.getSubdivision().getId(), subd_id) &&
                (!cur || (Objects.equals(posts_hist.getWorkEnd(), null)))) {
                ret.add(posts_hist);
            }
        }
        return ret;
    }

    @Override
    public List<Workers> getWorkersFromPosts(Long post_id, boolean cur) {
        List<Workers> ret = new ArrayList<>();
        for (PostsHistory posts_hist : getAll()) {
            if (Objects.equals(posts_hist.getPost().getId(), post_id) &&
                (!cur || (Objects.equals(posts_hist.getWorkEnd(), null)))) {
                ret.add(posts_hist.getWorker());
            }
        }
        return ret;
    }

    @Override
    public List<PostsHistory> getWorkerHistory(Long worker_id) {
        List<PostsHistory> ret = new ArrayList<>();
        for (PostsHistory posts_hist : getAll()) {
            if (Objects.equals(posts_hist.getWorker().getId(), worker_id)) {
                ret.add(posts_hist);
            }
        }
        return ret;
    }
}
