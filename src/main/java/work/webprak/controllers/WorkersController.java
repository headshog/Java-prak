package work.webprak.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import work.webprak.DAO.WorkersDAO;
import work.webprak.DAO.impl.PostsDAOImpl;
import work.webprak.DAO.impl.PostsHistoryDAOImpl;
import work.webprak.DAO.impl.SubdivisionsDAOImpl;
import work.webprak.DAO.impl.WorkersDAOimpl;
import work.webprak.DAO.PostsDAO;
import work.webprak.DAO.PostsHistoryDAO;
import work.webprak.DAO.SubdivisionsDAO;
import work.webprak.models.PostsHistory;
import work.webprak.models.Subdivisions;
import work.webprak.models.Workers;
import work.webprak.models.Posts;

import java.util.ArrayList;
import java.sql.Timestamp;
import java.util.List;

@Controller
public class WorkersController {
    @Autowired
    private final PostsHistoryDAO postsHistoryDAO = new PostsHistoryDAOImpl();

    @Autowired
    private final WorkersDAO workersDAO = new WorkersDAOimpl();

    @Autowired
    private final PostsDAO postsDAO = new PostsDAOImpl();

    @Autowired
    private final SubdivisionsDAO subdivisionsDAO = new SubdivisionsDAOImpl();

    @GetMapping("/workers")
    public String workers(Model model) {
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
        model.addAttribute("workers", workers);
        model.addAttribute("workersService", workersDAO);

        return "workers";
    }

    @PostMapping("/getWorkersByPost")
    public String getWorkersByPost(@RequestParam(name = "postName") String postName,
                                   Model model) {
        List<Workers> workers = workersDAO.getWorkersFromPosts(postsDAO.getPostsByName(postName).get(0).getId(), true);
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
        model.addAttribute("workers", workers);
        model.addAttribute("workersService", workersDAO);

        return "workers";
    }

    @PostMapping("/getWorkersBySubdivision")
    public String getWorkersBySubdivision(@RequestParam(name = "subdivisionName") String subdivisionName,
                                          Model model) {
        List<Workers> workers = workersDAO.getWorkersFromSubdivision(
            subdivisionsDAO.getSubdivisionsByName(subdivisionName).get(0).getId(), true);
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
        model.addAttribute("workers", workers);
        model.addAttribute("workersService", workersDAO);

        return "workers";
    }

    @GetMapping("/worker")
    public String worker(@RequestParam(name = "workerId") Long workerId, Model model) {
        Workers worker = workersDAO.getById(workerId);

        if (worker == null) {
            model.addAttribute("error_msg", "В базе нет работника с заданным ID: " + workerId);
            return "errorPage";
        }

        List<PostsHistory> workerHistory = workersDAO.getWorkerHistory(workerId);

        model.addAttribute("worker", worker);
        model.addAttribute("workerHistory", workerHistory);
        model.addAttribute("workersService", workersDAO);
        model.addAttribute("postsHistoryService", postsHistoryDAO);

        return "worker";
    }

    @GetMapping("/editWorker")
    public String editWorker(@RequestParam(name = "workerId") Long workerId, Model model) {
        Workers worker = workersDAO.getById(workerId);

        if (worker == null) {
            model.addAttribute("error_msg", "В базе нет работника с заданным ID: " + workerId);
            return "errorPage";
        }

        List<PostsHistory> workerHistory = workersDAO.getWorkerHistory(workerId);
        List<Posts> posts = (List<Posts>)postsDAO.getAll();
        List<Subdivisions> subdivisions = (List<Subdivisions>)subdivisionsDAO.getAll();

        List<String> postsNames= new ArrayList<>();
        for (var it : posts)
            postsNames.add(it.getName());

        List<String> subdivisionsNames= new ArrayList<>();
        for (var it : subdivisions)
            subdivisionsNames.add(it.getName());

        model.addAttribute("postsNames", postsNames);
        model.addAttribute("subdivisionsNames", subdivisionsNames);
        model.addAttribute("worker", worker);
        model.addAttribute("workerHistory", workerHistory);
        model.addAttribute("workersService", workersDAO);
        model.addAttribute("postsHistoryService", postsHistoryDAO);

        return "editWorker";
    }

    @PostMapping("/addPostHistory")
    public String addPostHistory(@RequestParam(name = "workerId") Long workerId,
                                 @RequestParam(name = "postName") String postName,
                                 @RequestParam(name = "subdivisionName") String subdivisionName,
                                 @RequestParam(name = "workStart") String workStart,
                                 @RequestParam(name = "workEnd", defaultValue = "abc") String workEnd,
                                 Model model) {
        Workers worker = workersDAO.getById(workerId);
        Subdivisions subdivision = subdivisionsDAO.getSubdivisionsByName(subdivisionName).get(0);

        if (worker == null) {
            model.addAttribute("error_msg", "В базе нет работника с заданным ID: " + workerId);
            return "errorPage";
        }

        PostsHistory postsHistory = new PostsHistory();
        postsHistory.setPost(postsDAO.getPostsByName(postName).get(0));
        postsHistory.setSubdivision(subdivision);
        postsHistory.setWorker(worker);
        postsHistory.setWorkStart(Timestamp.valueOf(workStart + " 00:00:00"));
        if (workEnd.endsWith("abc"))
            postsHistory.setWorkEnd(null);
        else
            postsHistory.setWorkEnd(Timestamp.valueOf(workEnd + " 00:00:00"));
        postsHistoryDAO.save(postsHistory);

        if (postName.endsWith("subdivision director")) {
            subdivision.setDirector(worker);
            subdivisionsDAO.update(subdivision);
        }

        return String.format("redirect:/editWorker?workerId=%d", workerId);
    }

    @PostMapping("/saveWorker")
    public String saveWorker(@RequestParam(name = "workerId", required = false) Long workerId,
                                 @RequestParam(name = "workerName") String name,
                                 @RequestParam(name = "workerAddress") String address,
                                 @RequestParam(name = "workerGraduation") String graduation,
                                 @RequestParam(name = "workerBirthDate") String birthDate,
                                 @RequestParam(name = "workerExperience", defaultValue = "0") Long experience,
                                 Model model) {
        Workers worker;
        if (workerId == null) {
            worker = new Workers(name, address, graduation, experience, birthDate);
            workersDAO.save(worker);
            return "redirect:/workers";
        } else {
            worker = workersDAO.getById(workerId);

            if (worker == null) {
                model.addAttribute("error_msg", "В базе нет работника с заданным ID: " + workerId);
                return "errorPage";
            }

            worker.setName(name);
            worker.setAddress(address);
            worker.setBirthDate(birthDate);
            worker.setGraduation(graduation);
            worker.setExperience(experience);
            workersDAO.update(worker);

            return String.format("redirect:/worker?workerId=%d", workerId);
        }
    }

    @PostMapping("/removeWorker")
    public String removeWorker(@RequestParam(name = "workerId") Long workerId) {
        workersDAO.deleteById(workerId);
        return "redirect:/workers";
    }

    @PostMapping("/removePostHistory")
    public String removePostHistory(@RequestParam(name = "workerId") Long workerId,
                                    @RequestParam(name = "postHistoryId") Long postHistoryId) {
        PostsHistory postHistory = postsHistoryDAO.getById(postHistoryId);
        if (postHistory.getPost().getName().endsWith("subdivision director") &&
            postHistory.getSubdivision() != null) {
            postHistory.getSubdivision().setDirector(null);
            subdivisionsDAO.update(postHistory.getSubdivision());
        }
        postsHistoryDAO.delete(postHistory);
        return String.format("redirect:/editWorker?workerId=%d", workerId);
    }
}
