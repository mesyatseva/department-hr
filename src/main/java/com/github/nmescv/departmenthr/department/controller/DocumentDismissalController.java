package com.github.nmescv.departmenthr.department.controller;

import com.github.nmescv.departmenthr.department.service.DocumentDismissalService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/document/dismissal")
public class DocumentDismissalController {

    private final DocumentDismissalService documentDismissalService;

    public DocumentDismissalController(DocumentDismissalService documentDismissalService) {
        this.documentDismissalService = documentDismissalService;
    }
}
