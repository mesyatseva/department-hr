package com.github.nmescv.departmenthr.department.controller;

import com.github.nmescv.departmenthr.department.service.DocumentDismissalService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.github.nmescv.departmenthr.department.dictionary.RoleDict.*;
import static com.github.nmescv.departmenthr.department.dictionary.RoleDict.HR_ROLE;

@Controller
@RequestMapping("/document/dismissal")
public class DocumentDismissalController {

    private final DocumentDismissalService documentDismissalService;

    public DocumentDismissalController(DocumentDismissalService documentDismissalService) {
        this.documentDismissalService = documentDismissalService;
    }

    @GetMapping
    @Secured({EMPLOYEE_ROLE, BOSS_ROLE, HR_ROLE})
    public String findAllDocuments() {
        return "document_dismissal/all_documents";
    }

    @GetMapping
    @Secured(EMPLOYEE_ROLE)
    public String showDismissalDocumentCreatingForm() {
        return "document_dismissal/create_by_employee";
    }

    @GetMapping("/{id}")
    @Secured({EMPLOYEE_ROLE, BOSS_ROLE, HR_ROLE})
    public String showById(@PathVariable("id") Long id) {
        return "document_dismissal/document_profile";
    }

    @PostMapping
    @Secured(EMPLOYEE_ROLE)
    public String createDismissalDocumentRequestByEmployee() {
        return "redirect:/document/dismissal";
    }

    @GetMapping
    @Secured(BOSS_ROLE)
    public String showDismissalDocumentApprovalForm() {
        return "document_dismissal/approval_by_boss";
    }

    @GetMapping
    @Secured(BOSS_ROLE)
    public String approveOrDeclineDismissalOfEmployee() {
        return "redirect:/document/dismissal";
    }

    @GetMapping
    @Secured(HR_ROLE)
    public String showFinalDocumentDismissalForm() {
        return "document_dismissal/close_by_hr";
    }

    @GetMapping
    @Secured(HR_ROLE)
    public String closeFinalDocumentDismissalForm() {
        return "redirect:/document/dismissal";
    }
}
