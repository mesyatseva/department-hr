package com.github.nmescv.departmenthr.department.converter;

import com.github.nmescv.departmenthr.department.dto.StaffScheduleDto;
import com.github.nmescv.departmenthr.department.entity.StaffSchedule;
import com.github.nmescv.departmenthr.department.repository.DepartmentRepository;
import com.github.nmescv.departmenthr.department.repository.PositionRepository;
import lombok.Synchronized;
import org.springframework.stereotype.Component;

@Component
public class StaffScheduleConverter implements Converter<StaffSchedule, StaffScheduleDto> {

    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;

    public StaffScheduleConverter(DepartmentRepository departmentRepository, PositionRepository positionRepository) {
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
    }

    @Override
    @Synchronized
    public StaffSchedule toEntity(StaffScheduleDto dto) {

        if (dto == null) {
            return null;
        }

        StaffSchedule entity = new StaffSchedule();
        entity.setId(dto.getId());
        entity.setDepartment(departmentRepository.findByName(dto.getDepartment()));
        entity.setPosition(positionRepository.findByName(dto.getPosition()));
        entity.setEmployeeNumber(dto.getEmployeeNumber());
        entity.setMinimalSalary(dto.getMinimalSalary());
        entity.setMaximalSalary(dto.getMaximalSalary());
        return entity;
    }

    @Override
    @Synchronized
    public StaffScheduleDto toDto(StaffSchedule entity) {

        if (entity == null) {
            return null;
        }

        StaffScheduleDto dto = new StaffScheduleDto();
        dto.setId(entity.getId());
        dto.setDepartment(entity.getDepartment().getName());
        dto.setPosition(entity.getPosition().getName());
        dto.setEmployeeNumber(entity.getEmployeeNumber());
        dto.setMinimalSalary(entity.getMinimalSalary());
        dto.setMaximalSalary(entity.getMaximalSalary());
        return dto;
    }
}
