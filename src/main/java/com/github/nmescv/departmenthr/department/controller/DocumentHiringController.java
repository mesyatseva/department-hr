package com.github.nmescv.departmenthr.department.controller;

import com.github.nmescv.departmenthr.department.dto.DocumentHiringDto;
import com.github.nmescv.departmenthr.department.entity.Department;
import com.github.nmescv.departmenthr.department.entity.Position;
import com.github.nmescv.departmenthr.department.repository.DepartmentRepository;
import com.github.nmescv.departmenthr.department.repository.EmployeeRepository;
import com.github.nmescv.departmenthr.department.repository.PositionRepository;
import com.github.nmescv.departmenthr.department.service.DocumentHiringService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.DocumentName;
import java.security.Principal;
import java.util.Collections;
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
        DocumentHiringDto createdDocument = documentHiringService.createHiringDraftStep1(dto, hrId);
        if (createdDocument == null) {
            return "document_hiring/create_by_hr";
        }
        return "redirect:/documents/hiring/";
    }

    @PostMapping("/create/draft")
    @Secured(HR_ROLE)
    public String createHiringDraft(@ModelAttribute("document") DocumentHiringDto dto,
                                                  Principal principal) {

        Long hrId = employeeRepository.findByTabelNumber(principal.getName()).getId();
        DocumentHiringDto createdDocument = documentHiringService.createHiringDraftStep1(dto, hrId);
        if (createdDocument == null) {
            return "document_hiring/create_by_hr";
        }
        return "redirect:/documents/hiring/" + createdDocument.getId() + "/create/department";
    }

    @GetMapping("/{id}/create/department")
    @Secured(HR_ROLE)
    public String fillDepartmentHiringForm(Model model,
                                           Principal principal,
                                           @PathVariable("id") Long id) {
        model.addAttribute("document", documentHiringService.showById(id, principal.getName()));
        model.addAttribute("departmentList", departmentRepository.findAll());
        return "document_hiring/create_by_hr_department";
    }

    @PostMapping("/{id}/create/department")
    @Secured(HR_ROLE)
    public String fillDepartmentHiring(@PathVariable("id") Long id,
                                       @ModelAttribute("document") DocumentHiringDto document,
                                       Principal principal) {
        DocumentHiringDto dto = documentHiringService.showById(id, principal.getName());
        DocumentHiringDto createdDocument = documentHiringService.fillDepartmentDraftStep2(dto, document.getDepartment());
        return "redirect:/documents/hiring/" + createdDocument.getId() + "/create/final";
    }

    @GetMapping("/{id}/create/final")
    @Secured(HR_ROLE)
    public String fillPositionHiringForm(Model model,
                                         Principal principal,
                                         @PathVariable("id") Long id) {
        DocumentHiringDto dto = documentHiringService.showById(id, principal.getName());
        Department department = departmentRepository.findByName(dto.getDepartment());
        model.addAttribute("document", dto);

        Position position = positionRepository.findByIdy(1L);
        log.info(position.toString());
        log.info(department.getId().toString());
        List<Position> positionList = positionRepository.findAllByDepartment(department.getId());
        log.info(positionList.toString());

        model.addAttribute("positionList", positionList);
        return "document_hiring/create_by_hr_final";
    }

    @PostMapping("/{id}/create/final")
    @Secured(HR_ROLE)
    public String publishFinal(@PathVariable("id") Long id,
                               @ModelAttribute("document") DocumentHiringDto document,
                               Principal principal) {
        DocumentHiringDto dto = documentHiringService.showById(id, principal.getName());
        DocumentHiringDto createdDocument = documentHiringService.publishAfterFillingPosition(dto, document.getPosition());
        return "redirect:/documents/hiring/" + createdDocument.getId();
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
    public String closeDocument(Model model,
                                @PathVariable("id") Long id,
                                Principal principal) {
        DocumentHiringDto documentHiringDto = documentHiringService.closeDocument(id, principal.getName());
        model.addAttribute("document", documentHiringDto);
        return "redirect:/documents/hiring/" + id;
    }
}
