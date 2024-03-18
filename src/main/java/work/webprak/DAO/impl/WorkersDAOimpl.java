package work.webprak.DAO.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import work.webprak.DAO.PostsHistoryDAO;
import work.webprak.DAO.WorkersDAO;
import work.webprak.DAO.Pair;
import work.webprak.models.Workers;
import work.webprak.models.Subdivisions;
import work.webprak.models.Posts;

import java.util.List;

@Repository
public class WorkersDAOimpl extends CommonDAOImpl<Workers, Long> implements WorkersDAO {
    public WorkersDAOimpl() {
        super(Workers.class);
    }

    @Autowired
    private PostsHistoryDAO posts_history = new PostsHistoryDAOImpl();

    @Override
    public List<Workers> getWorkersByExperience(Long exp) {
        try (Session session = sessionFactory.openSession()) {
            Query<Workers> query = session.createQuery("FROM Workers WHERE Experience = :num", Workers.class)
                    .setParameter("num", exp);
            return query.getResultList().size() == 0 ? null : query.getResultList();
        }
    }

    @Override
    public List<Workers> getWorkersByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Workers> query = session.createQuery("FROM Workers WHERE Name LIKE :name", Workers.class)
                    .setParameter("name", likeExpr(name));
            return query.getResultList().size() == 0 ? null : query.getResultList();
        }
    }

    @Override
    public List<Pair<Posts, Subdivisions>> getWorkerHistory(Long worker_id) {
        return posts_history.getWorkerHistory(worker_id);
    }

    @Override
    public List<Workers> getWorkersFromSubdivision(Long subd_id, boolean cur) {
        return posts_history.getWorkersFromSubdivision(subd_id, cur);
    }

    @Override
    public List<Workers> getWorkersFromPosts(Long post_id, boolean cur) {
        return posts_history.getWorkersFromPosts(post_id, cur);
    }

    private String likeExpr(String param) {
        return "%" + param + "%";
    }
}
