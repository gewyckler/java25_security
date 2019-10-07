package pl.javagda25.spring.security.securitytemplate.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/")
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
    @GetMapping("/tylkodlakozakow")
    public String indexl() {
        return "index";
    }

    @GetMapping("/login")
    public String indexZLogowaniem() {
        return "login-form";
    }
}
