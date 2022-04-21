package com.github.nmescv.departmenthr.department.controller;

import com.github.nmescv.departmenthr.department.dictionary.EditMode;
import com.github.nmescv.departmenthr.department.dto.EmployeeDto;
import com.github.nmescv.departmenthr.department.repository.*;
import com.github.nmescv.departmenthr.department.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final CountryRepository countryRepository;
    private final PositionRepository positionRepository;
    private final EducationRepository educationRepository;
    private final UniversityRepository universityRepository;
    private final DepartmentRepository departmentRepository;
    private final SpecialityRepository specialityRepository;

    public EmployeeController(EmployeeService employeeService,
                              CountryRepository countryRepository,
                              PositionRepository positionRepository,
                              EducationRepository educationRepository,
                              UniversityRepository universityRepository,
                              DepartmentRepository departmentRepository,
                              SpecialityRepository specialityRepository) {
        this.employeeService = employeeService;
        this.countryRepository = countryRepository;
        this.positionRepository = positionRepository;
        this.educationRepository = educationRepository;
        this.universityRepository = universityRepository;
        this.departmentRepository = departmentRepository;
        this.specialityRepository = specialityRepository;
    }

    @GetMapping
    public String list(Model model, Principal principal) {
        log.info(principal.getName());
        model.addAttribute("list", employeeService.findAll());
        return "employee/list";
    }

    @GetMapping("/new")
    @Secured("ROLE_HR")
    public String creatingEmployeeForm(Model model) {
        model.addAttribute("departmentList", departmentRepository.findAll());
        model.addAttribute("positionList", positionRepository.findAll());
        model.addAttribute("countryList", countryRepository.findAll());
        model.addAttribute("universityList", universityRepository.findAll());
        model.addAttribute("educationList", educationRepository.findAll());
        model.addAttribute("specialityList", specialityRepository.findAll());

        model.addAttribute("employee", new EmployeeDto());
        model.addAttribute("editMode", EditMode.CREATE);
        return "employee/new";
    }

    @PostMapping("/create")
    @Secured("ROLE_HR")
    public String createEmployee(Model model,
                                 @Valid @ModelAttribute("employee") EmployeeDto employeeDto,
                                 BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("departmentList", departmentRepository.findAll());
            model.addAttribute("positionList", positionRepository.findAll());
            model.addAttribute("countryList", countryRepository.findAll());
            model.addAttribute("universityList", universityRepository.findAll());
            model.addAttribute("educationList", educationRepository.findAll());
            model.addAttribute("specialityList", specialityRepository.findAll());
            return "employee/new";
        }

        EmployeeDto savedEmployee = employeeService.createNewEmployee(employeeDto);
        if (savedEmployee == null) {
            return "redirect:/employees/new";
        }
        return "redirect:/employees/" +  savedEmployee.getId();
    }

    @GetMapping("/{id}")
    public String showProfile(Model model, @PathVariable("id") Long id) {
        EmployeeDto employee = employeeService.showById(id);
        if (employee == null) {
            String notFound = "Отсутствует сотрудник с номером - " + id;
            model.addAttribute("notFound", notFound);
        }
        model.addAttribute("employee", employee);
        return "employee/profile";
    }
}
