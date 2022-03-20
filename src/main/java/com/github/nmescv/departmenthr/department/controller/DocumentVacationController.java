package com.github.nmescv.departmenthr.department.controller;

import com.github.nmescv.departmenthr.department.service.DocumentVacationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/document/vacation")
public class DocumentVacationController {

    private final DocumentVacationService documentVacationService;

    public DocumentVacationController(DocumentVacationService documentVacationService) {
        this.documentVacationService = documentVacationService;
    }
}
