package com.github.nmescv.departmenthr.department.controller;

import com.github.nmescv.departmenthr.security.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/accounts")
public class AccountController {

    private final UserRepository userRepository;

    public AccountController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String findAll(Model model) {
        return null;
    }

    @GetMapping("/new")
    public String accountCreatingForm(Model model) {
        return null;
    }

    @PostMapping("/create")
    public String createAccount() {
        return null;
    }
}
