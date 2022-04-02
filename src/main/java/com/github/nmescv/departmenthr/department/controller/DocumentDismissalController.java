package com.github.nmescv.departmenthr.department.controller;

import com.github.nmescv.departmenthr.department.dto.DocumentDismissalDto;
import com.github.nmescv.departmenthr.department.dto.DocumentHiringDto;
import com.github.nmescv.departmenthr.department.repository.DepartmentRepository;
import com.github.nmescv.departmenthr.department.repository.EmployeeRepository;
import com.github.nmescv.departmenthr.department.repository.PositionRepository;
import com.github.nmescv.departmenthr.department.service.DocumentDismissalService;
import com.github.nmescv.departmenthr.department.service.DocumentHiringService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.github.nmescv.departmenthr.department.dictionary.RoleDict.*;
import static com.github.nmescv.departmenthr.department.dictionary.RoleDict.HR_ROLE;

@Controller
@RequestMapping("/documents/dismissal")
public class DocumentDismissalController {

    private final DocumentDismissalService documentDismissalService;
    private final DocumentHiringService documentHiringService;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;

    public DocumentDismissalController(DocumentDismissalService documentDismissalService,
                                       DocumentHiringService documentHiringService,
                                       EmployeeRepository employeeRepository,
                                       DepartmentRepository departmentRepository,
                                       PositionRepository positionRepository) {
        this.documentDismissalService = documentDismissalService;
        this.documentHiringService = documentHiringService;
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
    }

    @GetMapping
    @Secured({EMPLOYEE_ROLE, BOSS_ROLE, HR_ROLE})
    public String findAllDocuments() {
        return "document_dismissal/all_documents";
    }

    @GetMapping("/new")
    @Secured(EMPLOYEE_ROLE)
    public String showDismissalDocumentCreatingForm(Model model) {
        model.addAttribute("positionList", positionRepository.findAll());
        model.addAttribute("departmentList", departmentRepository.findAll());
        model.addAttribute("employeeList", employeeRepository.findAll());
        model.addAttribute("document", new DocumentDismissalDto());
        return "document_dismissal/create_by_employee";
    }

    @GetMapping("/{id}")
    @Secured({EMPLOYEE_ROLE, BOSS_ROLE, HR_ROLE})
    public String showById(@PathVariable("id") Long id) {
        return "document_dismissal/document_profile";
    }

    @PostMapping("/create")
    @Secured(EMPLOYEE_ROLE)
    public String createDismissalDocumentRequestByEmployee() {
        return "redirect:/document/dismissal";
    }

//    @GetMapping
//    @Secured(BOSS_ROLE)
//    public String showDismissalDocumentApprovalForm() {
//        return "document_dismissal/approval_by_boss";
//    }
//
//    @GetMapping
//    @Secured(BOSS_ROLE)
//    public String approveOrDeclineDismissalOfEmployee() {
//        return "redirect:/document/dismissal";
//    }
//
//    @GetMapping
//    @Secured(HR_ROLE)
//    public String showFinalDocumentDismissalForm() {
//        return "document_dismissal/close_by_hr";
//    }
//
//    @GetMapping
//    @Secured(HR_ROLE)
//    public String closeFinalDocumentDismissalForm() {
//        return "redirect:/document/dismissal";
//    }
}
