package com.github.nmescv.departmenthr.department.controller;

import com.github.nmescv.departmenthr.department.dto.EmployeeDto;
import com.github.nmescv.departmenthr.department.dto.RelativePersonDto;
import com.github.nmescv.departmenthr.department.entity.RelativePerson;
import com.github.nmescv.departmenthr.department.repository.RelationshipRepository;
import com.github.nmescv.departmenthr.department.service.RelativePersonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping
public class RelativePersonController {

    private final RelationshipRepository relationshipRepository;
    private final RelativePersonService relativePersonService;

    public RelativePersonController(RelationshipRepository relationshipRepository,
                                    RelativePersonService relativePersonService) {
        this.relationshipRepository = relationshipRepository;
        this.relativePersonService = relativePersonService;
    }

    @GetMapping("/employee/{employeeId}/relative")
    public String showRelativePersonListByEmployeeId(Model model, @PathVariable("employeeId") Long employeeId) {
        List<RelativePersonDto> list = relativePersonService.showRelativePersonListByEmployeeId(employeeId);
        if (list == null) {
            String notFound = "Отсутствует информация о родственниках у сотрудника - " + employeeId;
            model.addAttribute("notFound", notFound);
        }
        model.addAttribute("list", list);
        model.addAttribute("employeeId", employeeId);
        return "relative_person/relative_person_list";
    }

    @GetMapping("/relative/{personId}")
    public String showRelativePersonById(Model model, @PathVariable("personId") Long personId) {
        RelativePersonDto person = relativePersonService.getRelativePersonById(personId);
        if (person == null) {
            String notFound = "Отсутствует информация о родственнике - " + personId;
            model.addAttribute("notFound", notFound);
        }
        model.addAttribute("person", person);
        return "relative_person/relative_person";
    }

    @GetMapping("/employee/{employeeId}/relative/new")
    public String creatingFormRelativePerson(Model model, @PathVariable("employeeId") Long employeeId) {
        model.addAttribute("relationshipList", relationshipRepository.findAll());
        RelativePersonDto relativePersonDto = new RelativePersonDto();
        relativePersonDto.setEmployeeId(employeeId);
        model.addAttribute("employeeId", employeeId);
        model.addAttribute("person", relativePersonDto);
        return "relative_person/new";
    }

    @PostMapping("/employee/{employeeId}/relative/create")
    public String creatingRelativePerson(@PathVariable("employeeId") Long employeeId,
                                         @ModelAttribute("person") RelativePersonDto dto) {

        RelativePersonDto savedPerson = relativePersonService.createRelativePerson(dto, employeeId);
        if (savedPerson == null) {
            return "redirect:/employee/" + employeeId + "/relative/new";
        }
        return "redirect:/relative/" + savedPerson.getId();
    }
}
