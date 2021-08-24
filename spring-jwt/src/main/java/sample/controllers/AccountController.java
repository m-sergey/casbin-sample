package sample.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @RequestMapping("/accounts")
    public String list() {
        return "private";
    }
}
