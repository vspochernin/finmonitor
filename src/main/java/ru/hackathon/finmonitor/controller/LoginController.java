package ru.hackathon.finmonitor.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
public class LoginController {


    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @RequestMapping("/loginSuccess")
    public String loginSuccess() {
        return "redirect:/";
    }

    @RequestMapping("/logoutSuccess")
    public String logoutSuccess() {
        return "redirect:/login?logout";
    }
}