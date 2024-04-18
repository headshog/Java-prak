package work.webprak.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomePageController {
    @RequestMapping(value = { "/", "/index"})
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/workers" )
    public String workers() {
        return "workers";
    }

    @RequestMapping(value = "/posts")
    public String posts() {
        return "posts";
    }

    @RequestMapping(value = "/subdivisions")
    public String subdivisions() {
        return "subdivisions";
    }
}
