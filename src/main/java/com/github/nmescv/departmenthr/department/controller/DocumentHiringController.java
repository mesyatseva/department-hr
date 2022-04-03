package com.github.nmescv.departmenthr.department.controller;

import com.github.nmescv.departmenthr.department.dto.DocumentHiringDto;
import com.github.nmescv.departmenthr.department.dto.DocumentReassignmentDto;
import com.github.nmescv.departmenthr.department.dto.EmployeeDto;
import com.github.nmescv.departmenthr.department.repository.DepartmentRepository;
import com.github.nmescv.departmenthr.department.repository.EmployeeRepository;
import com.github.nmescv.departmenthr.department.repository.PositionRepository;
import com.github.nmescv.departmenthr.department.service.DocumentHiringService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    @Secured(HR_ROLE)
    public String findAllDocuments(Model model,
                                   Principal principal) {
        String username = principal.getName();
        log.info(username);
        List<DocumentHiringDto> list = documentHiringService.findAll(username);
        log.info(list.toString());
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
    public String createHiringDocumentRequestByHR(@ModelAttribute("document") DocumentHiringDto dto,
                                                  Principal principal) {

        Long hrId = employeeRepository.findByTabelNumber(principal.getName()).getId();
        DocumentHiringDto createdDocument = documentHiringService.createHiringDocument(dto, hrId);
        if (createdDocument == null) {
            return "document_hiring/create_by_hr";
        }
        return "redirect:/documents/hiring";
    }

    @GetMapping("/{id}")
    @Secured({EMPLOYEE_ROLE, BOSS_ROLE, HR_ROLE})
    public String showById(Model model,
                           @PathVariable("id") Long id,
                           Principal principal) {
        DocumentHiringDto documentHiringDto = documentHiringService.showById(id, principal.getName());
        if (documentHiringDto == null) {
            String notFound = "Отсутствует документ на отпуск с номером - " + id;
            model.addAttribute("notFound", notFound);
        }
        model.addAttribute("document", documentHiringDto);
        return "document_hiring/document_profile";
    }

    @GetMapping("/{id}/approve")
    @Secured(BOSS_ROLE)
    public String approveByBoss(Model model,
                                @PathVariable("id") Long id,
                                Principal principal) {
        DocumentHiringDto documentHiringDto = documentHiringService.approveHiring(id, principal.getName());
        model.addAttribute("document", documentHiringDto);
        return "redirect:/documents/hiring/" + id;
    }


    @GetMapping("/{id}/decline")
    @Secured(BOSS_ROLE)
    public String declineByBoss(Model model,
                                @PathVariable("id") Long id,
                                Principal principal) {
        DocumentHiringDto documentHiringDto = documentHiringService.declineHiring(id, principal.getName());
        model.addAttribute("document", documentHiringDto);
        return "redirect:/documents/hiring/" + id;
    }


    @GetMapping("/{id}/close")
    @Secured(HR_ROLE)
    public String showFinalDocumentVacationForm(Model model,
                                                @PathVariable("id") Long id,
                                                Principal principal) {
        DocumentHiringDto documentHiringDto = documentHiringService.closeDocument(id, principal.getName());
        model.addAttribute("document", documentHiringDto);
        return "redirect:/documents/hiring/" + id;
    }
}
