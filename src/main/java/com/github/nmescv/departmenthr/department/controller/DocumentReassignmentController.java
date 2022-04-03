package com.github.nmescv.departmenthr.department.controller;

import com.github.nmescv.departmenthr.department.dto.DocumentDismissalDto;
import com.github.nmescv.departmenthr.department.dto.DocumentReassignmentDto;
import com.github.nmescv.departmenthr.department.repository.DepartmentRepository;
import com.github.nmescv.departmenthr.department.repository.EmployeeRepository;
import com.github.nmescv.departmenthr.department.repository.PositionRepository;
import com.github.nmescv.departmenthr.department.service.DocumentReassignmentService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.github.nmescv.departmenthr.department.dictionary.RoleDict.*;
import static com.github.nmescv.departmenthr.department.dictionary.RoleDict.HR_ROLE;

@Controller
@RequestMapping("/documents/reassignment")
public class DocumentReassignmentController {

    private final DocumentReassignmentService documentReassignmentService;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;

    public DocumentReassignmentController(DocumentReassignmentService documentReassignmentService,
                                          EmployeeRepository employeeRepository,
                                          DepartmentRepository departmentRepository,
                                          PositionRepository positionRepository) {
        this.documentReassignmentService = documentReassignmentService;
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
    }

    @GetMapping
    @Secured({EMPLOYEE_ROLE, BOSS_ROLE, HR_ROLE})
    public String findAllDocuments(Model model,
                                   Principal principal) {
        model.addAttribute("list", documentReassignmentService.findAll(principal.getName()));
        return "document_reassignment/all_documents";
    }

    @GetMapping("/new")
    @Secured(EMPLOYEE_ROLE)
    public String showReassignmentDocumentCreatingForm(Model model) {
        model.addAttribute("positionList", positionRepository.findAll());
        model.addAttribute("departmentList", departmentRepository.findAll());
        model.addAttribute("employeeList", employeeRepository.findAll());
        model.addAttribute("document", new DocumentReassignmentDto());
        return "document_reassignment/create_by_employee";
    }

    @PostMapping("/create")
    @Secured(EMPLOYEE_ROLE)
    public String createReassignmentDocumentRequestByEmployee(@ModelAttribute("document") DocumentReassignmentDto dto,
                                                              Principal principal) {
        Long employeeId = employeeRepository.findByTabelNumber(principal.getName()).getId();
        DocumentReassignmentDto createdDocument = documentReassignmentService.createRequestForReassignment(dto, employeeId);
        if (createdDocument == null) {
            return "document_reassignment/create_by_employee";
        }
        return "redirect:/documents/reassignment";
    }

//    @GetMapping("/{id}")
//    @Secured({EMPLOYEE_ROLE, BOSS_ROLE, HR_ROLE})
//    public String showById(@PathVariable("id") Long id) {
//        return "document_reassignment/document_profile";
//    }
//    @GetMapping
//    @Secured(BOSS_ROLE)
//    public String showReassignmentDocumentApprovalForm() {
//        return "document_reassignment/approval_by_boss";
//    }
//
//    @GetMapping
//    @Secured(BOSS_ROLE)
//    public String approveOrDeclineReassignmentOfEmployee() {
//        return "redirect:/document/reassignment";
//    }
//
//    @GetMapping
//    @Secured(HR_ROLE)
//    public String showFinalDocumentReassignmentForm() {
//        return "document_reassignment/close_by_hr";
//    }
//
//    @GetMapping
//    @Secured(HR_ROLE)
//    public String closeFinalDocumentReassignmentForm() {
//        return "redirect:/document/reassignment";
//    }
}
