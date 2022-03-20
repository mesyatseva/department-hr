package com.github.nmescv.departmenthr.department.controller;

import com.github.nmescv.departmenthr.department.repository.EmployeeRepository;
import com.github.nmescv.departmenthr.security.account.Account;
import com.github.nmescv.departmenthr.security.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping
public class AuthController {

//    @GetMapping("/auth/login")
//    public String loginPage() {
//        log.info("Login page");
//        return "login/login";
//    }

    private final EmployeeRepository employeeRepository;

    public AuthController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/auth/login") //<.>
    public String login(@AuthenticationPrincipal UserDetails userDetails) { //<.>
        if (userDetails == null) { //<.>
            System.out.println("Hi");
            return "login/login"; //<.>
        } else {
            return "redirect:/layout"; //<.>
        }
    }

    @GetMapping("/layout")
    public String layout(@AuthenticationPrincipal UserDetails userDetails) {
        log.info(userDetails.toString());
        log.info(userDetails.getAuthorities().toString());
        return "layout/layout";
    }

    @GetMapping("/page")
    public String page() {
        return "layout/page";
    }

}
