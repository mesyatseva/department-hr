package com.github.nmescv.departmenthr.department.controller;

import com.github.nmescv.departmenthr.department.service.DocumentVacationService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.github.nmescv.departmenthr.department.dictionary.RoleDict.*;

@Controller
@RequestMapping("/document/vacation")
public class DocumentVacationController {

    private final DocumentVacationService documentVacationService;

    public DocumentVacationController(DocumentVacationService documentVacationService) {
        this.documentVacationService = documentVacationService;
    }

    @GetMapping
    @Secured({EMPLOYEE_ROLE, BOSS_ROLE, HR_ROLE})
    public String findAllDocuments() {
        return "document_vacation/all_documents";
    }

    @GetMapping
    @Secured(EMPLOYEE_ROLE)
    public String showVacationDocumentCreatingForm() {
        return "document_vacation/create_by_employee";
    }

    @PostMapping
    @Secured(EMPLOYEE_ROLE)
    public String createVacationDocumentRequestByEmployee() {
        return "redirect:/document/vacation";
    }

    @GetMapping("/{id}")
    @Secured({EMPLOYEE_ROLE, BOSS_ROLE, HR_ROLE})
    public String showById(@PathVariable("id") Long id) {
        return "document_vacation/document_profile";
    }

    @GetMapping
    @Secured(BOSS_ROLE)
    public String showVacationDocumentApprovalForm() {
        return "document_vacation/approval_by_boss";
    }

    @GetMapping
    @Secured(BOSS_ROLE)
    public String approveOrDeclineVacationOfEmployee() {
        return "redirect:/document/vacation";
    }

    @GetMapping
    @Secured(HR_ROLE)
    public String showFinalDocumentVacationForm() {
        return "document_vacation/close_by_hr";
    }

    @GetMapping
    @Secured(HR_ROLE)
    public String closeFinalDocumentVacationForm() {
        return "redirect:/document/vacation";
    }
}
