package sample.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {

    @RequestMapping("/profile")
    public String info() {
        return "private";
    }
}
