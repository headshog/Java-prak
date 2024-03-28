package work.webprak.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import work.webprak.DAO.WorkersDAO;
import work.webprak.DAO.impl.PostsHistoryDAOImpl;
import work.webprak.DAO.impl.SubdivisionsDAOImpl;
import work.webprak.DAO.impl.WorkersDAOimpl;
import work.webprak.DAO.PostsHistoryDAO;
import work.webprak.DAO.SubdivisionsDAO;
import work.webprak.models.Workers;
import work.webprak.models.Subdivisions;

import java.util.List;

@Controller
public class SubdivisionsController {
    @Autowired
    private final PostsHistoryDAO postsHistoryDAO = new PostsHistoryDAOImpl();

    @Autowired
    private final WorkersDAO workersDAO = new WorkersDAOimpl();

    @Autowired
    private final SubdivisionsDAO subdivisionsDAO = new SubdivisionsDAOImpl();

    @GetMapping("/subdivisions")
    public String subdivisionsListPage(Model model) {
        List<Subdivisions> subdivisions = (List<Subdivisions>)subdivisionsDAO.getAll();
        model.addAttribute("subdivisions", subdivisions);
        model.addAttribute("workersService", workersDAO);
        model.addAttribute("postshistoryService", postsHistoryDAO);
        model.addAttribute("subdivisionsService", subdivisionsDAO);
        return "subdivisions";
    }

    @GetMapping("/subdivisionWorkers")
    public String subdivisionWorkersPage(@RequestParam(name = "subdivisionId") Long subdivisionId, Model model) {
        Subdivisions subdivision = subdivisionsDAO.getById(subdivisionId);

        if (subdivision == null) {
            model.addAttribute("error_msg", "В базе нет подразделения с заданным ID: " + subdivisionId);
            return "errorPage";
        }

        List<Workers> workers = subdivisionsDAO.getWorkersFromSubdivision(subdivisionId);

        model.addAttribute("subdivision", subdivision);
        model.addAttribute("workers", workers);
        model.addAttribute("workersService", workersDAO);
        model.addAttribute("postshistoryService", postsHistoryDAO);
        model.addAttribute("subdivisionsService", subdivisionsDAO);
        return "subdivisionWorkers";
    }

    @GetMapping("/subdivisionInner")
    public String subdivisionInnerPage(@RequestParam(name = "subdivisionId") Long subdivisionId, Model model) {
        Subdivisions subdivision = subdivisionsDAO.getById(subdivisionId);

        if (subdivision == null) {
            model.addAttribute("error_msg", "В базе нет подразделения с заданным ID: " + subdivisionId);
            return "errorPage";
        }

        List<Subdivisions> innerSubdivisions = subdivisionsDAO.getInnerSubdivisions(subdivisionId);

        model.addAttribute("subdivision", subdivision);
        model.addAttribute("innerSubdivisions", innerSubdivisions);
        model.addAttribute("workersService", workersDAO);
        model.addAttribute("postshistoryService", postsHistoryDAO);
        model.addAttribute("subdivisionsService", subdivisionsDAO);
        return "subdivisionInner";
    }

    @GetMapping("/editSubdivisionWorkers")
    public String editSubdivisionWorkersPage(@RequestParam(name = "subdivisionId") Long subdivisionId, Model model) {
        Subdivisions subdivision = subdivisionsDAO.getById(subdivisionId);

        if (subdivision == null) {
            model.addAttribute("error_msg", "В базе нет подразделения с заданным ID: " + subdivisionId);
            return "errorPage";
        }

        model.addAttribute("worker", subdivision);
        model.addAttribute("workersService", workersDAO);
        model.addAttribute("postshistoryService", postsHistoryDAO);
        return "editWorker";
    }

    @PostMapping("/saveWorker")
    public String saveWorkerPage(@RequestParam(name = "personId") Long workerId,
                                 @RequestParam(name = "name") String name,
                                 @RequestParam(name = "address") String address,
                                 @RequestParam(name = "graduation") String graduation,
                                 @RequestParam(name = "birthDate") String birthDate,
                                 @RequestParam(name = "experience") Long experience,
                                 Model model) {
        Workers worker = workersDAO.getById(workerId);
        if (worker != null) {
            worker.setName(name);
            worker.setAddress(address);
            worker.setBirthDate(birthDate);
            worker.setGraduation(graduation);
            worker.setExperience(experience);
        } else {
            worker = new Workers(workerId, name, address, graduation, experience, birthDate);
        }
        return String.format("redirect:/worker?workerId=%d", worker.getId());
    }

    @PostMapping("/removeWorker")
    public String removeWorkerPage(@RequestParam(name = "personId") Long workerId) {
        workersDAO.deleteById(workerId);
        return "redirect:/workers";
    }
}
