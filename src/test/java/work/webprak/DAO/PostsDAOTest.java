package work.webprak.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import work.webprak.models.Posts;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations="classpath:application.properties")
public class PostsDAOTest {
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private PostsDAO postsDAO;

    @Test
    void testPostsByName() {
        List<Posts> posts = postsDAO.getPostsByName("reach");
        assertEquals(1, posts.size());
        assertEquals(1, posts.get(0).getId());

        posts = postsDAO.getPostsByName("fa");
        assertEquals(3, posts.size());
        assertEquals(4, posts.get(0).getId());
        assertEquals(5, posts.get(1).getId());
        assertEquals(7, posts.get(2).getId());

        posts = postsDAO.getPostsByName("{}");
        assertNull(posts);

        Posts elem = postsDAO.getById(10L);
        assertEquals(10, elem.getId());
    }

    @BeforeEach
    void beforeEach() {
        List<Posts> posts = new ArrayList<>();
        posts.add(new Posts("reach", "fall"));
        posts.add(new Posts("bulb", "distribute"));
        posts.add(new Posts("species", "amend"));
        posts.add(new Posts("family", "apply"));
        posts.add(new Posts("farii", "push"));
        posts.add(new Posts("humanity", "calculate"));
        posts.add(new Posts("fan", "react"));
        posts.add(new Posts("formulate", "attempt"));
        posts.add(new Posts("tourist", "bind"));
        posts.add(new Posts("urge", "rest"));
        postsDAO.saveCollection(posts);
    }

    @BeforeAll
    @AfterEach
    void refreshPostsTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery("TRUNCATE Posts RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE posts_id_seq RESTART WITH 1;").executeUpdate();
            session.getTransaction().commit();
        }
    }
}
