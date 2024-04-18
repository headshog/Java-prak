package work.webprak.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import work.webprak.DAO.PostsDAO;
import work.webprak.models.Posts;
import work.webprak.DAO.impl.PostsDAOImpl;

import java.util.List;

@Controller
public class PostsController {
    @Autowired
    private final PostsDAO postsDAO = new PostsDAOImpl();

    @GetMapping("/posts")
    public String posts(Model model) {
        List<Posts> posts = (List<Posts>)postsDAO.getAll();

        model.addAttribute("posts", posts);
        model.addAttribute("postsService", postsDAO);

        return "posts";
    }

    @GetMapping("/post")
    public String post(@RequestParam(name = "postId") Long postId, Model model) {
        Posts post = postsDAO.getById(postId);

        if (post == null) {
            model.addAttribute("error_msg", "В базе нет должности с заданным ID: " + postId);
            return "errorPage";
        }

        model.addAttribute("post", post);
        model.addAttribute("postsService", postsDAO);

        return "post";
    }

    @GetMapping("/editPost")
    public String editPost(@RequestParam(name = "postId") Long postId, Model model) {
        Posts post = postsDAO.getById(postId);

        if (post == null) {
            model.addAttribute("error_msg", "В базе нет должности с заданным ID: " + postId);
            return "errorPage";
        }

        model.addAttribute("post", post);
        model.addAttribute("postsService", postsDAO);

        return "editPost";
    }

    @PostMapping("/savePost")
    public String savePost(@RequestParam(name = "postId", required = false) Long postId,
                               @RequestParam(name = "postName") String name,
                               @RequestParam(name = "postDescription") String responsibilities,
                               Model model) {
        Posts post;
        if (postId == null) {
            post = new Posts(name, responsibilities);
            postsDAO.save(post);
            return "redirect:/posts";
        } else {
            post = postsDAO.getById(postId);

            if (post == null) {
                model.addAttribute("error_msg", "В базе нет должности с заданным ID: " + postId);
                return "errorPage";
            }

            post.setName(name);
            post.setResponsibilities(responsibilities);
            postsDAO.update(post);

            return String.format("redirect:/post?postId=%d", post.getId());
        }
    }

    @PostMapping("/removePost")
    public String removePost(@RequestParam(name = "postId") Long postId) {
        postsDAO.deleteById(postId);
        return "redirect:/posts";
    }
}
