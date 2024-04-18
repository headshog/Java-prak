package work.webprak.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import work.webprak.models.Subdivisions;
import work.webprak.models.Workers;
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
public class WorkersDAOTest {
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
        List<Workers> workersSubd = workersDAO.getWorkersFromSubdivision(3L, false);
        assertEquals(2, workersSubd.size());
        assertEquals(3, workersSubd.get(0).getId());
        assertEquals(2, workersSubd.get(1).getId());

        workersSubd = workersDAO.getWorkersFromSubdivision(3L, true);
        assertEquals(1, workersSubd.size());
        assertEquals(2, workersSubd.get(0).getId());
    }

    @Test
    void testWorkersPosts() {
        List<Workers> workersPosts = workersDAO.getWorkersFromPosts(3L, false);
        assertEquals(2, workersPosts.size());
        assertEquals(3, workersPosts.get(0).getId());
        assertEquals(2, workersPosts.get(1).getId());

        workersPosts = workersDAO.getWorkersFromPosts(3L, true);
        assertEquals(1, workersPosts.size());
        assertEquals(2, workersPosts.get(0).getId());
    }

    @Test
    void testWorkersHistory() {
        List<Workers> workersNull = workersDAO.getWorkersByName("{}");
        assertNull(workersNull);

        List<Workers> workerToryList = workersDAO.getWorkersByName("Tori");
        assertEquals(1, workerToryList.size());

        Workers workerTory = workerToryList.get(0);
        List<PostsHistory> workerToryHistory = workersDAO.getWorkerHistory(workerTory.getId());
        assertEquals(2, workerToryHistory.size());
        assertEquals(1, workerToryHistory.get(0).getPost().getId());
        assertEquals(1, workerToryHistory.get(0).getSubdivision().getId());
        assertEquals(4, workerToryHistory.get(1).getPost().getId());
        assertEquals(4, workerToryHistory.get(1).getSubdivision().getId());
    }

    @Test
    void testUpdateDelete() {
        List<Workers> expUpWorkers = workersDAO.getWorkersByExperience(17L);
        for (Workers expUpWorker : expUpWorkers) {
            Long exp = expUpWorker.getExperience();
            expUpWorker.setExperience(exp + 1);
            workersDAO.update(expUpWorker);
        }

        expUpWorkers = workersDAO.getWorkersByExperience(0L);
        assertNull(expUpWorkers);

        Workers firedWorker = workersDAO.getById(1L);
        workersDAO.delete(firedWorker);
        assertNull(workersDAO.getById(1L));

        workersDAO.deleteById(2L);
        assertNull(workersDAO.getById(2L));
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
        subdivisions.add(new Subdivisions(2L, subdivisionsDAO.getById(1L), workersDAO.getById(2L), "library"));
        subdivisions.add(new Subdivisions(3L, subdivisionsDAO.getById(2L), workersDAO.getById(3L), "sir"));
        subdivisions.add(new Subdivisions(4L, subdivisionsDAO.getById(2L), workersDAO.getById(4L), "passion"));
        subdivisionsDAO.saveCollection(subdivisions);

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
            session.createSQLQuery("TRUNCATE Subdivisions RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE subdivisions_id_seq RESTART WITH 1;").executeUpdate();
            session.getTransaction().commit();
        }
    }
}
