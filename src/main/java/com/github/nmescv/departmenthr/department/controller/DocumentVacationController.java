package com.github.nmescv.departmenthr.department.controller;

import com.github.nmescv.departmenthr.department.dto.DocumentReassignmentDto;
import com.github.nmescv.departmenthr.department.dto.DocumentVacationDto;
import com.github.nmescv.departmenthr.department.repository.DepartmentRepository;
import com.github.nmescv.departmenthr.department.repository.EmployeeRepository;
import com.github.nmescv.departmenthr.department.repository.PositionRepository;
import com.github.nmescv.departmenthr.department.service.DocumentVacationService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.github.nmescv.departmenthr.department.dictionary.RoleDict.*;

@Controller
@RequestMapping("/documents/vacation")
public class DocumentVacationController {

    private final DocumentVacationService documentVacationService;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;

    public DocumentVacationController(DocumentVacationService documentVacationService,
                                      EmployeeRepository employeeRepository,
                                      DepartmentRepository departmentRepository,
                                      PositionRepository positionRepository) {
        this.documentVacationService = documentVacationService;
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
    }

    @GetMapping
    @Secured({EMPLOYEE_ROLE, BOSS_ROLE, HR_ROLE})
    public String findAllDocuments(Model model,
                                   Principal principal) {
        model.addAttribute("list", documentVacationService.findAll(principal.getName()));
        return "document_vacation/all_documents";
    }
        ;

    @GetMapping("/new")
    @Secured(EMPLOYEE_ROLE)
    public String showVacationDocumentCreatingForm(Model model) {
        model.addAttribute("positionList", positionRepository.findAll());
        model.addAttribute("departmentList", departmentRepository.findAll());
        model.addAttribute("employeeList", employeeRepository.findAll());
        model.addAttribute("document", new DocumentVacationDto());
        return "document_vacation/create_by_employee";
    }

    @PostMapping("/create")
    @Secured(EMPLOYEE_ROLE)
    public String createVacationDocumentRequestByEmployee(@ModelAttribute("document") DocumentVacationDto dto,
                                                          Principal principal) {
        Long employeeId = employeeRepository.findByTabelNumber(principal.getName()).getId();
        DocumentVacationDto createdDocument = documentVacationService.createRequestForVacation(dto, employeeId);
        if (createdDocument == null) {
            return "document_vacation/create_by_employee";
        }
        return "redirect:/documents/vacation";
    }

    @GetMapping("/{id}")
    @Secured({EMPLOYEE_ROLE, BOSS_ROLE, HR_ROLE})
    public String showById(@PathVariable("id") Long id) {
        return "document_vacation/document_profile";
    }

//    @GetMapping
//    @Secured(BOSS_ROLE)
//    public String showVacationDocumentApprovalForm() {
//        return "document_vacation/approval_by_boss";
//    }
//
//    @GetMapping
//    @Secured(BOSS_ROLE)
//    public String approveOrDeclineVacationOfEmployee() {
//        return "redirect:/document/vacation";
//    }
//
//    @GetMapping
//    @Secured(HR_ROLE)
//    public String showFinalDocumentVacationForm() {
//        return "document_vacation/close_by_hr";
//    }
//
//    @GetMapping
//    @Secured(HR_ROLE)
//    public String closeFinalDocumentVacationForm() {
//        return "redirect:/document/vacation";
//    }
}
