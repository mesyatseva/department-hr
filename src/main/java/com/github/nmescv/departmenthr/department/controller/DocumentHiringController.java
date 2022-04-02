package com.github.nmescv.departmenthr.department.controller;

import com.github.nmescv.departmenthr.department.dto.DocumentHiringDto;
import com.github.nmescv.departmenthr.department.repository.DepartmentRepository;
import com.github.nmescv.departmenthr.department.repository.EmployeeRepository;
import com.github.nmescv.departmenthr.department.repository.PositionRepository;
import com.github.nmescv.departmenthr.department.service.DocumentHiringService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
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

@Slf4j
@Controller
@RequestMapping("/documents/hiring")
public class DocumentHiringController {

    private final DocumentHiringService documentHiringService;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;

    public DocumentHiringController(DocumentHiringService documentHiringService,
                                    EmployeeRepository employeeRepository,
                                    DepartmentRepository departmentRepository,
                                    PositionRepository positionRepository) {
        this.documentHiringService = documentHiringService;
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
    }

    @GetMapping
    public String findAllDocuments(Model model,
                                   Principal principal) {
        String username = principal.getName();
        log.info(username);
        List<DocumentHiringDto> list = documentHiringService.findAll(username);
        model.addAttribute("list", list);
        return "document_hiring/all_documents";
    }

    @GetMapping("/new")
    @Secured(HR_ROLE)
    public String showHiringDocumentCreatingForm(Model model) {
        model.addAttribute("positionList", positionRepository.findAll());
        model.addAttribute("departmentList", departmentRepository.findAll());
        model.addAttribute("employeeList", employeeRepository.findAll());
        model.addAttribute("document", new DocumentHiringDto());
        return "document_hiring/create_by_hr";
    }

    @PostMapping("/create")
    @Secured(HR_ROLE)
    public String createHiringDocumentRequestByHR() {
        return "redirect:/documents/hiring";
    }

    @GetMapping("/{id}")
    @Secured({EMPLOYEE_ROLE, BOSS_ROLE, HR_ROLE})
    public String showById(@PathVariable("id") Long id) {
        return "document_hiring/document_profile";
    }

//    @GetMapping
//    @Secured(BOSS_ROLE)
//    public String showHiringDocumentApprovalForm() {
//        return "document_hiring/approval_by_boss";
//    }
//
//    @GetMapping
//    @Secured(BOSS_ROLE)
//    public String approveOrDeclineHiringOfEmployee() {
//        return "redirect:/document/hiring";
//    }
//
//    @GetMapping
//    @Secured(HR_ROLE)
//    public String showFinalDocumentHiringForm() {
//        return "document_hiring/close_by_hr";
//    }
//
//    @GetMapping
//    @Secured(HR_ROLE)
//    public String closeFinalDocumentHiringForm() {
//        return "redirect:/document/hiring";
//    }
}
