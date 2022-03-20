package com.github.nmescv.departmenthr.department.controller;

import com.github.nmescv.departmenthr.department.service.DocumentHiringService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/document/hiring/")
public class DocumentHiringController {

    private final DocumentHiringService documentHiringService;

    public DocumentHiringController(DocumentHiringService documentHiringService) {
        this.documentHiringService = documentHiringService;
    }
}
