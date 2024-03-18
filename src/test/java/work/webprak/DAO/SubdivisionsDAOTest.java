package work.webprak.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import work.webprak.models.Subdivisions;
import work.webprak.models.Workers;
import work.webprak.models.InnerSubdivisions;
import work.webprak.models.PostsHistory;
import work.webprak.models.Posts;

import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations="classpath:application.properties")
public class SubdivisionsDAOTest {
    @Autowired
    private InnerSubdivisionsDAO innersubdivisionsDAO;
    @Autowired
    private WorkersDAO workersDAO;
    @Autowired
    private PostsDAO postsDAO;
    @Autowired
    private PostsHistoryDAO postshistoryDAO;
    @Autowired
    private SubdivisionsDAO subdivisionsDAO;
    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void testWorkersSubdivision() {
        List<Workers> workersSubd = subdivisionsDAO.getWorkersFromSubdivision(3L);
        assertEquals(1, workersSubd.size());
        assertEquals(2, workersSubd.get(0).getId());
    }

    @Test
    void testSubdivisionNameDirector() {
        List<Subdivisions> subdivisionsName = subdivisionsDAO.getSubdivisionsByName("library");
        assertEquals(1, subdivisionsName.size());
        assertEquals(2, subdivisionsName.get(0).getId());

        subdivisionsName = subdivisionsDAO.getSubdivisionsByName("{}");
        assertNull(subdivisionsName);

        subdivisionsName = subdivisionsDAO.getSubdivisionsByDirector(3L);
        assertEquals(2, subdivisionsName.size());
        assertEquals(2, subdivisionsName.get(0).getId());
        assertEquals(3, subdivisionsName.get(1).getId());
    }

    @Test
    void testInnerSubdivisions() {
        List<Subdivisions> innersubdivisions = subdivisionsDAO.getInnerSubdivisions(2L);
        assertEquals(2, innersubdivisions.size());
        assertEquals(3, innersubdivisions.get(0).getId());
        assertEquals(4, innersubdivisions.get(1).getId());

        innersubdivisions = subdivisionsDAO.getInnerSubdivisions(1L);
        assertEquals(1, innersubdivisions.size());
        assertEquals(2, innersubdivisions.get(0).getId());

        innersubdivisions = subdivisionsDAO.getInnerSubdivisions(3L);
        assertEquals(0, innersubdivisions.size());
    }

    @BeforeEach
    void beforeEach() {
        List<Workers> workers = new ArrayList<>();
        workers.add(new Workers("Tori Pena", "9255 Mayfield Ave. Rahway, NJ 07065", "ABCD", (Long)17L, "1975-12-26"));
        workers.add(new Workers("Lucia Vincent", "72 SW. Myers Ave. Menasha, WI 54952", "ABCD", 18L, "1983-10-20"));
        workers.add(new Workers("Melvin Galloway", "31 Lakeshore St. Elizabethtown, PA 17022", "ABCD", 19L, "1988-01-08"));
        workers.add(new Workers("Corey Mueller", "186 Prospect Street Muskogee, OK 74403", "ABCD", 20L, "1988-03-08"));
        workersDAO.saveCollection(workers);

        List<Posts> posts = new ArrayList<>();
        posts.add(new Posts("reach", "fall"));
        posts.add(new Posts("bulb", "distribute"));
        posts.add(new Posts("species", "amend"));
        posts.add(new Posts("family", "apply"));
        postsDAO.saveCollection(posts);

        List<Subdivisions> subdivisions = new ArrayList<>();
        subdivisions.add(new Subdivisions(1L, null, workersDAO.getById(1L), "interaction"));
        subdivisions.add(new Subdivisions(2L, subdivisionsDAO.getById(1L), workersDAO.getById(3L), "library"));
        subdivisions.add(new Subdivisions(3L, subdivisionsDAO.getById(2L), workersDAO.getById(3L), "sir"));
        subdivisions.add(new Subdivisions(4L, subdivisionsDAO.getById(2L), workersDAO.getById(4L), "passion"));
        subdivisionsDAO.saveCollection(subdivisions);

        List<InnerSubdivisions> innersubdivisions = new ArrayList<>();
        innersubdivisions.add(new InnerSubdivisions(subdivisionsDAO.getById(1L), subdivisionsDAO.getById(2L)));
        innersubdivisions.add(new InnerSubdivisions(subdivisionsDAO.getById(2L), subdivisionsDAO.getById(3L)));
        innersubdivisions.add(new InnerSubdivisions(subdivisionsDAO.getById(2L), subdivisionsDAO.getById(4L)));
        innersubdivisionsDAO.saveCollection(innersubdivisions);

        String date1 = "2022-01-19 00:00:00", date2 = "2023-02-19 00:00:00";
        List<PostsHistory> postshistory = new ArrayList<>();
        postshistory.add(new PostsHistory(1L, workersDAO.getById(1L), postsDAO.getById(1L), subdivisionsDAO.getById(1L), Timestamp.valueOf(date1), Timestamp.valueOf(date2)));
        postshistory.add(new PostsHistory(2L, workersDAO.getById(2L), postsDAO.getById(2L), subdivisionsDAO.getById(2L), Timestamp.valueOf(date1), Timestamp.valueOf(date2)));
        postshistory.add(new PostsHistory(3L, workersDAO.getById(3L), postsDAO.getById(3L), subdivisionsDAO.getById(3L), Timestamp.valueOf(date1), Timestamp.valueOf(date2)));
        postshistory.add(new PostsHistory(4L, workersDAO.getById(4L), postsDAO.getById(4L), subdivisionsDAO.getById(4L), Timestamp.valueOf(date1), Timestamp.valueOf(date2)));
        postshistory.add(new PostsHistory(5L, workersDAO.getById(1L), postsDAO.getById(4L), subdivisionsDAO.getById(4L), Timestamp.valueOf(date1), null));
        postshistory.add(new PostsHistory(6L, workersDAO.getById(2L), postsDAO.getById(3L), subdivisionsDAO.getById(3L), Timestamp.valueOf(date1), null));
        postshistory.add(new PostsHistory(7L, workersDAO.getById(3L), postsDAO.getById(2L), subdivisionsDAO.getById(2L), Timestamp.valueOf(date1), null));
        postshistory.add(new PostsHistory(8L, workersDAO.getById(4L), postsDAO.getById(1L), subdivisionsDAO.getById(1L), Timestamp.valueOf(date1), null));
        postshistoryDAO.saveCollection(postshistory);
    }

    @BeforeAll
    @AfterEach
    void refresh_all_tables() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery("TRUNCATE Workers RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE workers_id_seq RESTART WITH 1;").executeUpdate();
            session.createSQLQuery("TRUNCATE Posts RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE posts_id_seq RESTART WITH 1;").executeUpdate();
            session.createSQLQuery("TRUNCATE PostsHistory RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE postshistory_id_seq RESTART WITH 1;").executeUpdate();
            session.createSQLQuery("TRUNCATE InnerSubdivisions RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE innersubdivisions_id_seq RESTART WITH 1;").executeUpdate();
            session.createSQLQuery("TRUNCATE Subdivisions RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE subdivisions_id_seq RESTART WITH 1;").executeUpdate();
            session.getTransaction().commit();
        }
    }
}
