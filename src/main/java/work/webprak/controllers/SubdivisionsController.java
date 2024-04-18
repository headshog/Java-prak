package work.webprak.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import work.webprak.DAO.WorkersDAO;
import work.webprak.DAO.impl.InnerSubdivisionsDAOImpl;
import work.webprak.DAO.impl.PostsDAOImpl;
import work.webprak.DAO.impl.PostsHistoryDAOImpl;
import work.webprak.DAO.impl.SubdivisionsDAOImpl;
import work.webprak.DAO.impl.WorkersDAOimpl;
import work.webprak.DAO.InnerSubdivisionsDAO;
import work.webprak.DAO.PostsDAO;
import work.webprak.DAO.PostsHistoryDAO;
import work.webprak.DAO.SubdivisionsDAO;
import work.webprak.models.InnerSubdivisions;
import work.webprak.models.PostsHistory;
import work.webprak.models.Subdivisions;
import work.webprak.models.Workers;
import work.webprak.models.Posts;

import java.util.ArrayList;
import java.sql.Timestamp;
import java.util.List;

@Controller
public class SubdivisionsController {
    @Autowired
    private final PostsHistoryDAO postsHistoryDAO = new PostsHistoryDAOImpl();

    @Autowired
    private final WorkersDAO workersDAO = new WorkersDAOimpl();

    @Autowired
    private final PostsDAO postsDAO = new PostsDAOImpl();

    @Autowired
    private final SubdivisionsDAO subdivisionsDAO = new SubdivisionsDAOImpl();

    @Autowired
    private final InnerSubdivisionsDAO innerSubdivisionsDAO = new InnerSubdivisionsDAOImpl();

    @GetMapping("/subdivisions")
    public String subdivisions(Model model) {
        List<Subdivisions> subdivisions = (List<Subdivisions>)subdivisionsDAO.getAll();

        model.addAttribute("subdivisions", subdivisions);
        model.addAttribute("subdivisionsService", subdivisionsDAO);

        return "subdivisions";
    }

    @GetMapping("/subdivision")
    public String subdivision(@RequestParam(name = "subdivisionId") Long subdivisionId, Model model) {
        Subdivisions subdivision = subdivisionsDAO.getById(subdivisionId);

        if (subdivision == null) {
            model.addAttribute("error_msg", "В базе нет подразделения с заданным ID: " + subdivisionId);
            return "errorPage";
        }

        List<PostsHistory> postsHistory = subdivisionsDAO.getPostsHistoryFromSubdivision(subdivisionId);
        List<Subdivisions> innerSubdivisions = subdivisionsDAO.getInnerSubdivisions(subdivisionId);

        model.addAttribute("subdivision", subdivision);
        model.addAttribute("postsHistory", postsHistory);
        model.addAttribute("innerSubdivisions", innerSubdivisions);
        model.addAttribute("postshistoryService", postsHistoryDAO);
        model.addAttribute("subdivisionsService", subdivisionsDAO);

        return "subdivision";
    }

    @GetMapping("/editSubdivision")
    public String editSubdivision(@RequestParam(name = "subdivisionId") Long subdivisionId, Model model) {
        Subdivisions subdivision = subdivisionsDAO.getById(subdivisionId);

        if (subdivision == null) {
            model.addAttribute("error_msg", "В базе нет подразделения с заданным ID: " + subdivisionId);
            return "errorPage";
        }

        List<PostsHistory> postsHistory = subdivisionsDAO.getPostsHistoryFromSubdivision(subdivisionId);
        List<Subdivisions> innerSubdivisions = subdivisionsDAO.getInnerSubdivisions(subdivisionId);
        List<Workers> workers = (List<Workers>)workersDAO.getAll();
        List<Posts> posts = (List<Posts>)postsDAO.getAll();
        List<Subdivisions> subdivisions = (List<Subdivisions>)subdivisionsDAO.getAll();

        List<String> workersNames = new ArrayList<>();
        for (var it : workers)
            workersNames.add(it.getName());

        List<String> postsNames= new ArrayList<>();
        for (var it : posts)
            postsNames.add(it.getName());

        List<String> subdivisionsNames= new ArrayList<>();
        for (var it : subdivisions)
            subdivisionsNames.add(it.getName());

        model.addAttribute("workersNames", workersNames);
        model.addAttribute("postsNames", postsNames);
        model.addAttribute("subdivisionsNames", subdivisionsNames);
        model.addAttribute("subdivision", subdivision);
        model.addAttribute("postsHistory", postsHistory);
        model.addAttribute("innerSubdivisions", innerSubdivisions);
        model.addAttribute("postshistoryService", postsHistoryDAO);
        model.addAttribute("subdivisionsService", subdivisionsDAO);

        return "editSubdivision";
    }

    @PostMapping("/addInnerSubdivisions")
    public String addInnerSubdivisions(@RequestParam(name = "subdivisionId") Long subdivisionId,
                                       @RequestParam(name = "subdivisionName") String subdivisionName,
                                       Model model) {
        Subdivisions subdivision = subdivisionsDAO.getById(subdivisionId);

        if (subdivision == null) {
            model.addAttribute("error_msg", "В базе нет подразделения с заданным ID: " + subdivisionId);
            return "errorPage";
        }

        InnerSubdivisions innerSubdivisions =
            new InnerSubdivisions(subdivisionsDAO.getById(subdivisionId),
                                  subdivisionsDAO.getSubdivisionsByName(subdivisionName).get(0));
        innerSubdivisionsDAO.save(innerSubdivisions);

        return String.format("redirect:/editSubdivision?subdivisionId=%d", subdivisionId);
    }

    @PostMapping("/addWorker")
    public String addWorker(@RequestParam(name = "subdivisionId") Long subdivisionId,
                            @RequestParam(name = "postName") String postName,
                            @RequestParam(name = "workerName") String workerName,
                            Model model) {
        Subdivisions subdivision = subdivisionsDAO.getById(subdivisionId);
        Workers worker = workersDAO.getWorkersByName(workerName).get(0);

        if (subdivision == null) {
            model.addAttribute("error_msg", "В базе нет подразделения с заданным ID: " + subdivisionId);
            return "errorPage";
        }

        if (postName.endsWith("subdivision director")) {
            subdivision.setDirector(worker);
            subdivisionsDAO.update(subdivision);
        }

        PostsHistory postHistory = new PostsHistory();
        postHistory.setPost(postsDAO.getPostsByName(postName).get(0));
        postHistory.setWorker(worker);
        postHistory.setSubdivision(subdivisionsDAO.getById(subdivisionId));
        postHistory.setWorkStart(new Timestamp(System.currentTimeMillis()));
        postHistory.setWorkEnd(null);
        postsHistoryDAO.save(postHistory);

        return String.format("redirect:/editSubdivision?subdivisionId=%d", subdivisionId);
    }

    @PostMapping("/saveSubdivision")
    public String saveSubdivision(@RequestParam(name = "subdivisionId", required = false) Long subdivisionId,
                                      @RequestParam(name = "headSubdName", required = false) String headSubdName,
                                      @RequestParam(name = "directorName", required = false) String directorName,
                                      @RequestParam(name = "subdivisionName") String name,
                                      Model model) {
        Subdivisions subdivision;
        if (subdivisionId == null) {
            Subdivisions newHeadSubdivision = null;
            if (headSubdName != "")
                newHeadSubdivision = subdivisionsDAO.getSubdivisionsByName(headSubdName).get(0);
            Workers newDirector = null;
            if (directorName != "")
                newDirector = workersDAO.getWorkersByName(directorName).get(0);

            subdivision = new Subdivisions(name);
            subdivision.setHeadSubdivision(newHeadSubdivision);
            subdivision.setDirector(newDirector);
            subdivisionsDAO.save(subdivision);

            if (newHeadSubdivision != null) {
                InnerSubdivisions innerSubdivision = new InnerSubdivisions();
                innerSubdivision.setInner_subdivision(subdivision);
                innerSubdivision.setMain_subdivision(newHeadSubdivision);
                innerSubdivisionsDAO.save(innerSubdivision);
            }

            if (newDirector != null) {
                PostsHistory directorHistory = new PostsHistory();
                directorHistory.setPost(postsDAO.getPostsByName("subdivision director").get(0));
                directorHistory.setSubdivision(subdivision);
                directorHistory.setWorker(newDirector);
                directorHistory.setWorkStart(new Timestamp(System.currentTimeMillis()));
                directorHistory.setWorkEnd(null);
                postsHistoryDAO.save(directorHistory);
            }

            return "redirect:/subdivisions";
        } else {
            subdivision = subdivisionsDAO.getById(subdivisionId);

            if (subdivision == null) {
                model.addAttribute("error_msg", "В базе нет должности с заданным ID: " + subdivisionId);
                return "errorPage";
            }

            Subdivisions newHeadSubdivision = null;
            if (headSubdName != "")
                newHeadSubdivision = subdivisionsDAO.getSubdivisionsByName(headSubdName).get(0);
            Workers newDirector = null;
            if (directorName != "")
                newDirector = workersDAO.getWorkersByName(directorName).get(0);

            subdivision.setHeadSubdivision(subdivisionsDAO.getSubdivisionsByName(headSubdName).get(0));
            subdivision.setDirector(workersDAO.getWorkersByName(directorName).get(0));
            subdivision.setName(name);
            subdivisionsDAO.update(subdivision);

            if (newHeadSubdivision != null) {
                InnerSubdivisions innerSubdivision = new InnerSubdivisions();
                innerSubdivision.setInner_subdivision(subdivision);
                innerSubdivision.setMain_subdivision(newHeadSubdivision);
                innerSubdivisionsDAO.save(innerSubdivision);
            }

            if (newDirector != null) {
                PostsHistory directorHistory = new PostsHistory();
                directorHistory.setPost(postsDAO.getPostsByName("subdivision director").get(0));
                directorHistory.setSubdivision(subdivision);
                directorHistory.setWorker(newDirector);
                directorHistory.setWorkStart(new Timestamp(System.currentTimeMillis()));
                directorHistory.setWorkEnd(null);
                postsHistoryDAO.save(directorHistory);
            }

            return String.format("redirect:/subdivision?subdivisionId=%d", subdivisionId);
        }
    }

    @PostMapping("/removeSubdivision")
    public String removeSubdivision(@RequestParam(name = "subdivisionId") Long subdivisionId) {
        List<PostsHistory> postsHistory = subdivisionsDAO.getPostsHistoryFromSubdivision(subdivisionId);
        for (PostsHistory postHistory : postsHistory) {
            postHistory.setWorkEnd(new Timestamp(System.currentTimeMillis()));
            postsHistoryDAO.update(postHistory);
        }
        subdivisionsDAO.deleteById(subdivisionId);
        return "redirect:/subdivisions";
    }

    @PostMapping("/removeInnerSubdivision")
    public String removeInnerSubdivision(@RequestParam(name = "subdivisionId") Long subdivisionId,
                                         @RequestParam(name = "innerSubdivisionId") Long innerSubdivisionId) {
        innerSubdivisionsDAO.deleteById(innerSubdivisionsDAO.getPairId(subdivisionId, innerSubdivisionId));
        return String.format("redirect:/editSubdivision?subdivisionId=%d", subdivisionId);
    }

    @PostMapping("/removeSubdivisionWorkers")
    public String removeSubdivisionWorkers(@RequestParam(name = "subdivisionId") Long subdivisionId,
                                           @RequestParam(name = "postHistoryId") Long postHistoryId) {
        PostsHistory postHistory = postsHistoryDAO.getById(postHistoryId);
        if (postHistory.getPost().getName().endsWith("subdivision director")) {
            postHistory.getSubdivision().setDirector(null);
            subdivisionsDAO.update(postHistory.getSubdivision());
        }
        postHistory.setWorkEnd(new Timestamp(System.currentTimeMillis()));
        postsHistoryDAO.update(postHistory);
        return String.format("redirect:/editSubdivision?subdivisionId=%d", subdivisionId);
    }
}
