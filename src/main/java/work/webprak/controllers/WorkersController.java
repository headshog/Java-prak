package work.webprak.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import work.webprak.DAO.WorkersDAO;
import work.webprak.DAO.impl.PostsHistoryDAOImpl;
import work.webprak.DAO.impl.WorkersDAOimpl;
import work.webprak.DAO.PostsHistoryDAO;
import work.webprak.DAO.Pair;
import work.webprak.models.Posts;
import work.webprak.models.Subdivisions;
import work.webprak.models.Workers;

import java.util.List;

@Controller
public class WorkersController {
    @Autowired
    private final PostsHistoryDAO postsHistoryDAO = new PostsHistoryDAOImpl();

    @Autowired
    private final WorkersDAO workersDAO = new WorkersDAOimpl();

    @GetMapping("/workers")
    public String workersListPage(Model model) {
        List<Workers> workers = (List<Workers>)workersDAO.getAll();
        model.addAttribute("workers", workers);
        model.addAttribute("workersService", workersDAO);
        model.addAttribute("postshistoryService", postsHistoryDAO);
        return "workers";
    }

    @GetMapping("/worker")
    public String workerPage(@RequestParam(name = "workerId") Long workerId, Model model) {
        Workers worker = workersDAO.getById(workerId);

        if (worker == null) {
            model.addAttribute("error_msg", "В базе нет работника с заданным ID: " + workerId);
            return "errorPage";
        }

        List<Pair<Posts, Subdivisions>> workerHistory = workersDAO.getWorkerHistory(workerId);

        model.addAttribute("worker", worker);
        model.addAttribute("workerHistory", workerHistory);
        model.addAttribute("workersService", workersDAO);
        model.addAttribute("postshistoryService", postsHistoryDAO);
        return "worker";
    }

    @GetMapping("/editWorker")
    public String editWorkerPage(@RequestParam(name = "workerId") Long workerId, Model model) {
        Workers worker = workersDAO.getById(workerId);

        if (worker == null) {
            model.addAttribute("error_msg", "В базе нет работника с заданным ID: " + workerId);
            return "errorPage";
        }

        List<Pair<Posts, Subdivisions>> workerHistory = workersDAO.getWorkerHistory(workerId);

        model.addAttribute("worker", worker);
        model.addAttribute("workerHistory", workerHistory);
        model.addAttribute("workersService", workersDAO);
        model.addAttribute("postshistoryService", postsHistoryDAO);
        return "editWorker";
    }

    @PostMapping("/saveWorker")
    public String saveWorkerPage(@RequestParam(name = "workerId", required = false) Long workerId,
                                 @RequestParam(name = "name") String name,
                                 @RequestParam(name = "address") String address,
                                 @RequestParam(name = "graduation") String graduation,
                                 @RequestParam(name = "birthDate") String birthDate,
                                 @RequestParam(name = "experience") Long experience,
                                 Model model) {
        Workers worker;
        if (workerId == null) {
            worker = new Workers(name, address, graduation, experience, birthDate);
            workersDAO.save(worker);
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
        }
        return String.format("redirect:/worker?workerId=%d", worker.getId());
    }

    @PostMapping("/removeWorker")
    public String removeWorkerPage(@RequestParam(name = "personId") Long workerId) {
        workersDAO.deleteById(workerId);
        return "redirect:/workers";
    }
}
