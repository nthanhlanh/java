package vn.unitech.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("message", "Welcome to Spring Boot with JSP and JSTL!");
        System.out.println("222222222222");
        return "index"; // maps to src/main/webapp/WEB-INF/views/index.jsp
    }

    @GetMapping("/test")
    public String test(Model model) {
        System.out.println("testtesttesttest");
        model.addAttribute("message", "Welcome to Spring Boot with JSP and JSTL!");
        return "page/test"; // maps to src/main/webapp/WEB-INF/views/index.jsp
    }
}
