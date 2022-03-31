package com.github.nmescv.departmenthr.department.controller;

import com.github.nmescv.departmenthr.department.dto.DocumentHiringDto;
import com.github.nmescv.departmenthr.department.service.DocumentHiringService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

import static com.github.nmescv.departmenthr.department.dictionary.RoleDict.*;
import static com.github.nmescv.departmenthr.department.dictionary.RoleDict.HR_ROLE;

@Controller
@RequestMapping("/document/hiring")
public class DocumentHiringController {

    private final DocumentHiringService documentHiringService;

    public DocumentHiringController(DocumentHiringService documentHiringService) {
        this.documentHiringService = documentHiringService;
    }

    @GetMapping
    public String findAllDocuments(Model model,
                                   Principal principal) {

        String username = principal.getName();
        List<DocumentHiringDto> list = documentHiringService.findAll(username);
        model.addAttribute("list", list);
        return "document_hiring/all_documents";
    }

    @GetMapping
    @Secured(HR_ROLE)
    public String showHiringDocumentCreatingForm() {
        return "document_hiring/create_by_hr";
    }

    @PostMapping
    @Secured(HR_ROLE)
    public String createHiringDocumentRequestByHR() {
        return "redirect:/document/hiring";
    }

    @GetMapping("/{id}")
    @Secured({EMPLOYEE_ROLE, BOSS_ROLE, HR_ROLE})
    public String showById(@PathVariable("id") Long id) {
        return "document_hiring/document_profile";
    }

    @GetMapping
    @Secured(BOSS_ROLE)
    public String showHiringDocumentApprovalForm() {
        return "document_hiring/approval_by_boss";
    }

    @GetMapping
    @Secured(BOSS_ROLE)
    public String approveOrDeclineHiringOfEmployee() {
        return "redirect:/document/hiring";
    }

    @GetMapping
    @Secured(HR_ROLE)
    public String showFinalDocumentHiringForm() {
        return "document_hiring/close_by_hr";
    }

    @GetMapping
    @Secured(HR_ROLE)
    public String closeFinalDocumentHiringForm() {
        return "redirect:/document/hiring";
    }
}
