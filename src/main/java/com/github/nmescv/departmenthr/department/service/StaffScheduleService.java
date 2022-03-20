package com.github.nmescv.departmenthr.department.service;

import com.github.nmescv.departmenthr.department.converter.StaffScheduleConverter;
import com.github.nmescv.departmenthr.department.dto.StaffScheduleDto;
import com.github.nmescv.departmenthr.department.entity.Department;
import com.github.nmescv.departmenthr.department.entity.StaffSchedule;
import com.github.nmescv.departmenthr.department.repository.DepartmentRepository;
import com.github.nmescv.departmenthr.department.repository.StaffScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffScheduleService {

    private final StaffScheduleConverter staffScheduleConverter;
    private final StaffScheduleRepository staffScheduleRepository;
    private final DepartmentRepository departmentRepository;

    public StaffScheduleService(StaffScheduleConverter staffScheduleConverter,
                                StaffScheduleRepository staffScheduleRepository,
                                DepartmentRepository departmentRepository) {
        this.staffScheduleConverter = staffScheduleConverter;
        this.staffScheduleRepository = staffScheduleRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<StaffScheduleDto> findAllStaffSchedule() {

        List<StaffSchedule> list = staffScheduleRepository.findAll();
        if (list.size() == 0) {
            return null;
        }

        return list
                .stream()
                .map(staffScheduleConverter::toDto)
                .collect(Collectors.toList());
    }

    public List<StaffScheduleDto> findAllStaffScheduleByDepartmentId(Long departmentId) {

        Department department = departmentRepository.findById(departmentId).orElse(null);
        if (department == null) {
            return null;
        }

        List<StaffSchedule> list = staffScheduleRepository.findAllByDepartment(department);
        return list
                .stream()
                .map(staffScheduleConverter::toDto)
                .collect(Collectors.toList());
    }

    public StaffScheduleDto findStaffScheduleById(Long id) {
        StaffSchedule staffSchedule = staffScheduleRepository.findById(id).orElse(null);
        if (staffSchedule == null) {
            return null;
        }
        return staffScheduleConverter.toDto(staffSchedule);
    }

    public StaffScheduleDto createScheduleStaff(StaffScheduleDto dto) {
        StaffSchedule staffSchedule = staffScheduleConverter.toEntity(dto);
        StaffSchedule savedStaffSchedule = staffScheduleRepository.save(staffSchedule);
        return staffScheduleConverter.toDto(savedStaffSchedule);
    }
}
