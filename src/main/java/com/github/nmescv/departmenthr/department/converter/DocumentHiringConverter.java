package com.github.nmescv.departmenthr.department.converter;

import com.github.nmescv.departmenthr.department.dto.DocumentHiringDto;
import com.github.nmescv.departmenthr.department.entity.DocumentHiring;
import com.github.nmescv.departmenthr.department.entity.Employee;
import com.github.nmescv.departmenthr.department.repository.DocumentStatusRepository;
import com.github.nmescv.departmenthr.department.repository.EmployeeRepository;
import lombok.Synchronized;
import org.springframework.stereotype.Component;

@Component
public class DocumentHiringConverter implements Converter<DocumentHiring, DocumentHiringDto> {

    private final EmployeeRepository employeeRepository;
    private final DocumentStatusRepository documentStatusRepository;

    public DocumentHiringConverter(EmployeeRepository employeeRepository,
                                   DocumentStatusRepository documentStatusRepository) {
        this.employeeRepository = employeeRepository;
        this.documentStatusRepository = documentStatusRepository;
    }

    @Override
    @Synchronized
    public DocumentHiring toEntity(DocumentHiringDto dto) {

        if (dto == null) {
            return null;
        }

        DocumentHiring entity = new DocumentHiring();
        entity.setId(dto.getId());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setOrderNumber(dto.getOrderNumber());

        Employee employee = employeeRepository.findById(dto.getBossId()).orElse(null);

        if (employee == null) {
            return null;
        }

        Employee boss = employeeRepository.findById(dto.getBossId()).orElse(null);

        entity.setEmployee(employee);

        if (boss != null) {
            entity.setBoss(boss);
        }

        if (dto.getHr() != null) {
            entity.setHr(employeeRepository.findById(dto.getHr()).orElse(null));
        }

        entity.setDocumentStatus(documentStatusRepository.findByName(dto.getDocumentStatus()));

        entity.setDepartment(employee.getDepartment());
        entity.setPosition(employee.getPosition());

        entity.setHiringDate(dto.getHiringDate());
        entity.setSalary(dto.getSalary());
        entity.setIsApproved(dto.getIsApproved());
        return entity;
    }

    @Override
    @Synchronized
    public DocumentHiringDto toDto(DocumentHiring entity) {

        if (entity == null) {
            return null;
        }

        DocumentHiringDto dto = new DocumentHiringDto();
        dto.setId(entity.getId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setOrderNumber(entity.getOrderNumber());
        dto.setEmployeeId(entity.getEmployee().getId());
        dto.setBossId(entity.getBoss().getId());

        if (entity.getHr() != null) {
            dto.setHr(entity.getHr().getId());
        }

        dto.setDocumentStatus(entity.getDocumentStatus().getName());
        dto.setDepartment(entity.getDepartment().getName());
        dto.setPosition(entity.getPosition().getName());
        dto.setHiringDate(entity.getHiringDate());
        dto.setSalary(entity.getSalary());
        dto.setIsApproved(entity.getIsApproved());
        return dto;
    }
}
