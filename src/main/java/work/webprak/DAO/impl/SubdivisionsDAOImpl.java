package work.webprak.DAO.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import work.webprak.DAO.PostsHistoryDAO;
import work.webprak.DAO.SubdivisionsDAO;
import work.webprak.DAO.InnerSubdivisionsDAO;
import work.webprak.models.Workers;
import work.webprak.models.PostsHistory;
import work.webprak.models.Subdivisions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class SubdivisionsDAOImpl extends CommonDAOImpl<Subdivisions, Long> implements SubdivisionsDAO {
    public SubdivisionsDAOImpl(){
        super(Subdivisions.class);
    }

    @Autowired
    private PostsHistoryDAO posts_history = new PostsHistoryDAOImpl();

    @Autowired
    private InnerSubdivisionsDAO inner_subd = new InnerSubdivisionsDAOImpl();

    @Override
    public List<Subdivisions> getSubdivisionsByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Subdivisions> query = session.createQuery("FROM Subdivisions WHERE Name LIKE :name", Subdivisions.class)
                    .setParameter("name", likeExpr(name));
            return query.getResultList().size() == 0 ? null : query.getResultList();
        }
    }

    @Override
    public List<Subdivisions> getSubdivisionsByDirector(Long director_id) {
        List<Subdivisions> ret = new ArrayList<>();
        for (Subdivisions subd : getAll()) {
            if (Objects.equals(subd.getDirector().getId(), director_id)) {
                ret.add(subd);
            }
        }
        return ret;
    }

    @Override
    public List<Workers> getWorkersFromSubdivision(Long subd_id) {
        return posts_history.getWorkersFromSubdivision(subd_id, true);
    }

    @Override
    public List<PostsHistory> getPostsHistoryFromSubdivision(Long subd_id) {
        return posts_history.getPostsHistoryFromSubdivision(subd_id, true);
    }

    @Override
    public List<Subdivisions> getInnerSubdivisions(Long subd_id) {
        return inner_subd.getInnerSubdivisions(subd_id);
    }

    private String likeExpr(String param) {
        return "%" + param + "%";
    }
}
