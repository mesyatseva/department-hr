package com.github.nmescv.departmenthr.department.controller;

import com.github.nmescv.departmenthr.department.dto.DocumentHiringDto;
import com.github.nmescv.departmenthr.department.dto.DocumentReassignmentDto;
import com.github.nmescv.departmenthr.department.dto.StructureDto;
import com.github.nmescv.departmenthr.department.entity.Department;
import com.github.nmescv.departmenthr.department.repository.DepartmentRepository;
import com.github.nmescv.departmenthr.department.repository.EmployeeRepository;
import com.github.nmescv.departmenthr.department.repository.PositionRepository;
import com.github.nmescv.departmenthr.department.service.DocumentReassignmentService;
import com.github.nmescv.departmenthr.department.service.StructureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.github.nmescv.departmenthr.department.dictionary.RoleDict.*;

@Slf4j
@Controller
@RequestMapping("/documents/reassignment")
public class DocumentReassignmentController {

    private final StructureService structureService;
    private final DocumentReassignmentService documentReassignmentService;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;

    public DocumentReassignmentController(StructureService structureService,
                                          DocumentReassignmentService documentReassignmentService,
                                          EmployeeRepository employeeRepository,
                                          DepartmentRepository departmentRepository,
                                          PositionRepository positionRepository) {
        this.structureService = structureService;
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
    public String showReassignmentDocumentCreatingForm(Model model,
                                                       Principal principal) {

        StructureDto structure = structureService.makeStructureOfDepartment(principal.getName());
        model.addAttribute("departmentList", departmentRepository.findAll());
        model.addAttribute("structure", structure);

        DocumentReassignmentDto reassignmentDto = new DocumentReassignmentDto();
        reassignmentDto.setDepartment(structure.getDepartment());
        reassignmentDto.setPosition(structure.getPosition());

        model.addAttribute("document", reassignmentDto);
        return "document_reassignment/create_by_employee_draft";
    }

    @PostMapping("/draft")
    @Secured(EMPLOYEE_ROLE)
    public String createDraftRequestByEmployee(@ModelAttribute("document") DocumentReassignmentDto dto,
                                                              Principal principal) {
        log.info(dto.toString());
        DocumentReassignmentDto draft = documentReassignmentService.createDraftRequest(dto, principal.getName());
        if (draft == null) {
            return "document_reassignment/create_by_employee_draft";
        }
        return "redirect:/documents/reassignment/" + draft.getId() + "/publish";
    }


    @GetMapping("/{id}/publish")
    @Secured(EMPLOYEE_ROLE)
    public String publishReassignmentForm(Model model,
                                          Principal principal,
                                          @PathVariable("id") Long id) {
        StructureDto structure = structureService.makeStructureOfDepartment(principal.getName());
        DocumentReassignmentDto reassignmentDto = documentReassignmentService.showById(id, principal.getName());
        Department department = departmentRepository.findByName(reassignmentDto.getNewDepartment());
        model.addAttribute("positionList", positionRepository.findAllByDepartment(department.getId()));
        model.addAttribute("structure", structure);
        model.addAttribute("document", reassignmentDto);
        return "document_reassignment/create_by_employee_final";
    }

    @PostMapping("/{id}/publish")
    @Secured(EMPLOYEE_ROLE)
    public String publishReassignment(@PathVariable("id") Long id,
                                      @ModelAttribute("document") DocumentReassignmentDto document,
                                      Principal principal) {
        DocumentReassignmentDto dto = documentReassignmentService.showById(id, principal.getName());
        DocumentReassignmentDto createdDocument = documentReassignmentService.publishRequest(dto, document.getNewPosition());
        if (createdDocument == null) {
            return "document_reassignment/create_by_employee_final";
        }
        return "redirect:/documents/reassignment/" + createdDocument.getId();
    }

    @GetMapping("/{id}")
    @Secured({EMPLOYEE_ROLE, BOSS_ROLE, HR_ROLE})
    public String showById(Model model,
                           @PathVariable("id") Long id,
                           Principal principal) {
        DocumentReassignmentDto documentReassignmentDto = documentReassignmentService.showById(id, principal.getName());
        if (documentReassignmentDto == null) {
            String notFound = "Отсутствует документ на отпуск с номером - " + id;
            model.addAttribute("notFound", notFound);
        }
        model.addAttribute("document", documentReassignmentDto);
        return "document_reassignment/document_profile";
    }

    @GetMapping("/{id}/approve")
    @Secured(BOSS_ROLE)
    public String approveByBoss(Model model,
                                @PathVariable("id") Long id,
                                Principal principal) {
        DocumentReassignmentDto documentReassignmentDto = documentReassignmentService.approveReassignment(id, principal.getName());
        model.addAttribute("document", documentReassignmentDto);
        return "redirect:/documents/reassignment/" + id;
    }


    @GetMapping("/{id}/decline")
    @Secured(BOSS_ROLE)
    public String declineByBoss(Model model,
                                @PathVariable("id") Long id,
                                Principal principal) {
        DocumentReassignmentDto documentReassignmentDto = documentReassignmentService.declineReassignment(id, principal.getName());
        model.addAttribute("document", documentReassignmentDto);
        return "redirect:/documents/reassignment/" + id;
    }


    @GetMapping("/{id}/close")
    @Secured(HR_ROLE)
    public String showFinalDocumentVacationForm(Model model,
                                                @PathVariable("id") Long id,
                                                Principal principal) {
        DocumentReassignmentDto documentReassignmentDto = documentReassignmentService.closeDocument(id, principal.getName());
        model.addAttribute("document", documentReassignmentDto);
        return "redirect:/documents/reassignment/" + id;
    }
}
