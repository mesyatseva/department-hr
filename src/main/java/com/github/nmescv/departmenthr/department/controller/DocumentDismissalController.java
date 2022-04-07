package com.github.nmescv.departmenthr.department.controller;

import com.github.nmescv.departmenthr.department.dto.DocumentDismissalDto;
import com.github.nmescv.departmenthr.department.dto.StructureDto;
import com.github.nmescv.departmenthr.department.repository.DepartmentRepository;
import com.github.nmescv.departmenthr.department.repository.EmployeeRepository;
import com.github.nmescv.departmenthr.department.repository.PositionRepository;
import com.github.nmescv.departmenthr.department.service.DocumentDismissalService;
import com.github.nmescv.departmenthr.department.service.StructureService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.github.nmescv.departmenthr.department.dictionary.RoleDict.*;

@Controller
@RequestMapping("/documents/dismissal")
public class DocumentDismissalController {

    private final StructureService structureService;
    private final DocumentDismissalService documentDismissalService;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;

    public DocumentDismissalController(StructureService structureService,
                                       DocumentDismissalService documentDismissalService,
                                       EmployeeRepository employeeRepository,
                                       DepartmentRepository departmentRepository,
                                       PositionRepository positionRepository) {
        this.structureService = structureService;
        this.documentDismissalService = documentDismissalService;
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
    }

    @GetMapping
    @Secured({EMPLOYEE_ROLE, BOSS_ROLE, HR_ROLE})
    public String findAllDocuments(Model model,
                                   Principal principal) {
        model.addAttribute("list", documentDismissalService.findAll(principal.getName()));
        return "document_dismissal/all_documents";
    }

    @GetMapping("/new")
    @Secured(EMPLOYEE_ROLE)
    public String showDismissalDocumentCreatingForm(Model model,
                                                    Principal principal) {

        StructureDto structure = structureService.makeStructureOfDepartment(principal.getName());
        model.addAttribute("positionList", positionRepository.findAll());
        model.addAttribute("departmentList", departmentRepository.findAll());
        model.addAttribute("employeeList", employeeRepository.findAll());
        model.addAttribute("structure", structure);

        DocumentDismissalDto documentDismissalDto = new DocumentDismissalDto();
        documentDismissalDto.setBossId(structure.getBossId());
        documentDismissalDto.setDepartment(structure.getDepartment());
        documentDismissalDto.setPosition(structure.getPosition());

        model.addAttribute("document", documentDismissalDto);
        return "document_dismissal/create_by_employee";
    }

    @PostMapping("/create")
    @Secured(EMPLOYEE_ROLE)
    public String createDismissalDocumentRequestByEmployee(@ModelAttribute("document") DocumentDismissalDto dto,
                                                           Principal principal) {

        DocumentDismissalDto createdDocument = documentDismissalService.createRequestToDismiss(dto, principal.getName());
        if (createdDocument == null) {
            return "document_dismissal/create_by_employee";
        }
        return "redirect:/documents/dismissal";
    }

    @GetMapping("/{id}")
    @Secured({EMPLOYEE_ROLE, BOSS_ROLE, HR_ROLE})
    public String showById(Model model,
                           @PathVariable("id") Long id,
                           Principal principal) {
        DocumentDismissalDto documentDismissalDto = documentDismissalService.showById(id, principal.getName());
        if (documentDismissalDto == null) {
            String notFound = "Отсутствует документ на отпуск с номером - " + id;
            model.addAttribute("notFound", notFound);
        }
        model.addAttribute("document", documentDismissalDto);
        return "document_dismissal/document_profile";
    }


    @GetMapping("/{id}/approve")
    @Secured(BOSS_ROLE)
    public String approveByBoss(Model model,
                                @PathVariable("id") Long id,
                                Principal principal) {
        DocumentDismissalDto documentDismissalDto = documentDismissalService.approveDismiss(id, principal.getName());
        model.addAttribute("document", documentDismissalDto);
        return "redirect:/documents/dismissal/" + id;
    }


    @GetMapping("/{id}/decline")
    @Secured(BOSS_ROLE)
    public String declineByBoss(Model model,
                                @PathVariable("id") Long id,
                                Principal principal) {
        DocumentDismissalDto documentDismissalDto = documentDismissalService.declineDismiss(id, principal.getName());
        model.addAttribute("document", documentDismissalDto);
        return "redirect:/documents/dismissal/" + id;
    }


    @GetMapping("/{id}/close")
    @Secured(HR_ROLE)
    public String showFinalDocumentVacationForm(Model model,
                                                @PathVariable("id") Long id,
                                                Principal principal) {
        DocumentDismissalDto documentDismissalDto = documentDismissalService.closeDocument(id, principal.getName());
        model.addAttribute("document", documentDismissalDto);
        return "redirect:/documents/dismissal/" + id;
    }
}
