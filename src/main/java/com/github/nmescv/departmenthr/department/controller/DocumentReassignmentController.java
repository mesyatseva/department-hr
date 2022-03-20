package com.github.nmescv.departmenthr.department.controller;

import com.github.nmescv.departmenthr.department.service.DocumentReassignmentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/document/reassignment")
public class DocumentReassignmentController {

    private final DocumentReassignmentService documentReassignmentService;

    public DocumentReassignmentController(DocumentReassignmentService documentReassignmentService) {
        this.documentReassignmentService = documentReassignmentService;
    }
}
