package work.webprak.DAO.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import work.webprak.DAO.PostsDAO;
import work.webprak.models.Posts;

import java.util.List;

@Repository
public class PostsDAOImpl extends CommonDAOImpl<Posts, Long> implements PostsDAO {
    public PostsDAOImpl(){
        super(Posts.class);
    }

    @Override
    public List<Posts> getPostsByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Posts> query = session.createQuery("FROM Posts WHERE Name LIKE :name", Posts.class)
                    .setParameter("name", likeExpr(name));
            return query.getResultList().size() == 0 ? null : query.getResultList();
        }
    }

    private String likeExpr(String param) {
        return "%" + param + "%";
    }
}
