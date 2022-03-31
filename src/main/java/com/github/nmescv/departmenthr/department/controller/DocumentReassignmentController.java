package com.github.nmescv.departmenthr.department.controller;

import com.github.nmescv.departmenthr.department.service.DocumentReassignmentService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.github.nmescv.departmenthr.department.dictionary.RoleDict.*;
import static com.github.nmescv.departmenthr.department.dictionary.RoleDict.HR_ROLE;

@Controller
@RequestMapping("/document/reassignment")
public class DocumentReassignmentController {

    private final DocumentReassignmentService documentReassignmentService;

    public DocumentReassignmentController(DocumentReassignmentService documentReassignmentService) {
        this.documentReassignmentService = documentReassignmentService;
    }

    @GetMapping
    @Secured({EMPLOYEE_ROLE, BOSS_ROLE, HR_ROLE})
    public String findAllDocuments() {
        return "document_reassignment/all_documents";
    }

    @GetMapping
    @Secured(EMPLOYEE_ROLE)
    public String showReassignmentDocumentCreatingForm() {
        return "document_reassignment/create_by_employee";
    }

    @GetMapping("/{id}")
    @Secured({EMPLOYEE_ROLE, BOSS_ROLE, HR_ROLE})
    public String showById(@PathVariable("id") Long id) {
        return "document_reassignment/document_profile";
    }

    @PostMapping
    @Secured(EMPLOYEE_ROLE)
    public String createReassignmentDocumentRequestByEmployee() {
        return "redirect:/document/reassignment";
    }

    @GetMapping
    @Secured(BOSS_ROLE)
    public String showReassignmentDocumentApprovalForm() {
        return "document_reassignment/approval_by_boss";
    }

    @GetMapping
    @Secured(BOSS_ROLE)
    public String approveOrDeclineReassignmentOfEmployee() {
        return "redirect:/document/reassignment";
    }

    @GetMapping
    @Secured(HR_ROLE)
    public String showFinalDocumentReassignmentForm() {
        return "document_reassignment/close_by_hr";
    }

    @GetMapping
    @Secured(HR_ROLE)
    public String closeFinalDocumentReassignmentForm() {
        return "redirect:/document/reassignment";
    }
}
