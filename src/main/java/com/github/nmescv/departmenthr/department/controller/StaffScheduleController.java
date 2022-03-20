package com.github.nmescv.departmenthr.department.controller;

import com.github.nmescv.departmenthr.department.dto.StaffScheduleDto;
import com.github.nmescv.departmenthr.department.entity.Department;
import com.github.nmescv.departmenthr.department.repository.DepartmentRepository;
import com.github.nmescv.departmenthr.department.repository.PositionRepository;
import com.github.nmescv.departmenthr.department.service.StaffScheduleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/staff")
public class StaffScheduleController {

    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final StaffScheduleService staffScheduleService;

    public StaffScheduleController(DepartmentRepository departmentRepository,
                                   PositionRepository positionRepository,
                                   StaffScheduleService staffScheduleService) {
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
        this.staffScheduleService = staffScheduleService;
    }

    @GetMapping("/all")
    public String findAllStaffSchedule(Model model) {
        List<StaffScheduleDto> list = staffScheduleService.findAllStaffSchedule();
        if (list == null || list.size() == 0) {
            String notFound = "Отсутствуют данные о штатных расписаниях";
            model.addAttribute("notFound", notFound);
        }
        model.addAttribute("list", list);
        return "staff/staff_list";
    }

    @GetMapping("/department/{departmentId}")
    public String findAllStaffScheduleByDepartment(@PathVariable("departmentId") Long departmentId, Model model) {

        Department department = departmentRepository.findById(departmentId).orElse(null);
        if (department == null) {
            String notFound = "Отсутствует департамент id = " + departmentId;
            model.addAttribute("notFound", notFound);
            return "staff/staff_list_by_department";
        }

        List<StaffScheduleDto> list = staffScheduleService.findAllStaffScheduleByDepartmentId(departmentId);
        if (list == null || list.size() == 0) {
            String notFound = "У подразделения - id = " + department.getName()  + " отсутствует штатное расписание";
            model.addAttribute("notFound", notFound);
        }
        model.addAttribute("list", list);
        model.addAttribute("department", department.getName());
        return "staff/staff_list_by_department";
    }

    @GetMapping("/{staffId}")
    public String findStaffScheduleById(Model model, @PathVariable("staffId") Long id) {
        StaffScheduleDto staff = staffScheduleService.findStaffScheduleById(id);
        if (staff == null) {
            String notFound = "Отсутствует информация о штатном расписании - id = " + staff;
            model.addAttribute("notFound", notFound);
        }
        model.addAttribute("staff", staff);
        return "staff/profile";
    }

    @GetMapping("/new")
    public String creatingFormStaff(Model model, StaffScheduleDto staffScheduleDto) {
        model.addAttribute("departmentList", departmentRepository.findAll());
        model.addAttribute("positionList", positionRepository.findAll());
        model.addAttribute("staff", staffScheduleDto);
        return "staff/new";
    }

    @PostMapping("/create")
    public String createStaff(@ModelAttribute("staff") StaffScheduleDto dto) {
        StaffScheduleDto savedStaff = staffScheduleService.createScheduleStaff(dto);
        if (savedStaff == null) {
            return "redirect:/staff/new";
        }
        return "redirect:/staff/" + savedStaff.getId();
    }
}
