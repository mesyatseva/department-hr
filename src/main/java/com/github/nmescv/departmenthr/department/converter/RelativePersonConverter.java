package com.github.nmescv.departmenthr.department.converter;

import com.github.nmescv.departmenthr.department.dto.RelativePersonDto;
import com.github.nmescv.departmenthr.department.entity.RelativePerson;
import com.github.nmescv.departmenthr.department.repository.EmployeeRepository;
import com.github.nmescv.departmenthr.department.repository.RelationshipRepository;
import lombok.Synchronized;
import org.springframework.stereotype.Component;

@Component
public class RelativePersonConverter implements Converter<RelativePerson, RelativePersonDto> {

    private final RelationshipRepository relationshipRepository;
    private final EmployeeRepository employeeRepository;

    public RelativePersonConverter(RelationshipRepository relationshipRepository,
                                   EmployeeRepository employeeRepository) {
        this.relationshipRepository = relationshipRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Synchronized
    public RelativePerson toEntity(RelativePersonDto dto) {

        if (dto == null) {
            return null;
        }

        RelativePerson entity = new RelativePerson();
        entity.setId(dto.getId());
        entity.setSurname(dto.getSurname());
        entity.setName(dto.getName());
        entity.setMiddleName(dto.getMiddleName());
        entity.setBirthday(dto.getBirthday());
        entity.setRelationship(relationshipRepository.findByName(dto.getRelationship()));
        entity.setEmployee(employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Отсутствует employee: id = " + dto.getEmployeeId())));
        return entity;
    }

    @Override
    @Synchronized
    public RelativePersonDto toDto(RelativePerson entity) {

        if (entity == null) {
            return null;
        }

        RelativePersonDto dto = new RelativePersonDto();
        dto.setId(entity.getId());
        dto.setSurname(entity.getSurname());
        dto.setName(entity.getName());
        dto.setMiddleName(entity.getMiddleName());
        dto.setBirthday(entity.getBirthday());
        dto.setRelationship(entity.getRelationship().getName());
        dto.setEmployeeId(entity.getEmployee().getId());
        return dto;
    }
}
