package com.github.nmescv.departmenthr.department.converter;

import com.github.nmescv.departmenthr.department.dto.DocumentDismissalDto;
import com.github.nmescv.departmenthr.department.entity.DocumentDismissal;
import com.github.nmescv.departmenthr.department.entity.Employee;
import com.github.nmescv.departmenthr.department.repository.DocumentStatusRepository;
import com.github.nmescv.departmenthr.department.repository.EmployeeRepository;
import com.github.nmescv.departmenthr.department.utils.EmployeeUtils;
import lombok.Synchronized;
import org.springframework.stereotype.Component;

@Component
public class DocumentDismissalConverter implements Converter<DocumentDismissal, DocumentDismissalDto> {

    private final DocumentStatusRepository documentStatusRepository;
    private final EmployeeRepository employeeRepository;

    public DocumentDismissalConverter(DocumentStatusRepository documentStatusRepository,
                                      EmployeeRepository employeeRepository) {
        this.documentStatusRepository = documentStatusRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Synchronized
    public DocumentDismissal toEntity(DocumentDismissalDto dto) {

        if (dto == null) {
            return null;
        }

        DocumentDismissal entity = new DocumentDismissal();
        entity.setId(dto.getId());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setOrderNumber(dto.getOrderNumber());

        Employee employee = employeeRepository.findById(dto.getEmployeeId()).orElse(null);

        if (employee == null) {
            return null;
        }

        Employee boss = employeeRepository.findById(dto.getBossId()).orElse(null);

        entity.setEmployee(employee);
        entity.setBoss(boss);


        if (dto.getHr() != null) {
            entity.setHr(employeeRepository.findById(dto.getHr()).orElse(null));
        }

        entity.setDocumentStatus(documentStatusRepository.findByName(dto.getDocumentStatus()));

        entity.setDepartment(employee.getDepartment());
        entity.setPosition(employee.getPosition());
        entity.setDismissalDate(dto.getDismissalDate());
        entity.setReason(dto.getReason());
        entity.setIsApproved(dto.getIsApproved());

        return entity;
    }

    @Override
    @Synchronized
    public DocumentDismissalDto toDto(DocumentDismissal entity) {

        if (entity == null) {
            return null;
        }

        DocumentDismissalDto dto = new DocumentDismissalDto();
        dto.setId(entity.getId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setOrderNumber(entity.getOrderNumber());
        dto.setEmployeeId(entity.getEmployee().getId());
        dto.setEmployeeFullName(EmployeeUtils.fullName(entity.getEmployee()));
        dto.setBossId(entity.getBoss().getId());
        dto.setBossFullName(EmployeeUtils.fullName(entity.getBoss()));
        dto.setEmployeeFullName(EmployeeUtils.fullName(entity.getEmployee()));

        if (entity.getHr() != null) {
            dto.setHr(entity.getHr().getId());
            dto.setHrFullName(EmployeeUtils.fullName(entity.getHr()));
        }

        dto.setDocumentStatus(entity.getDocumentStatus().getName());
        dto.setDepartment(entity.getDepartment().getName());
        dto.setPosition(entity.getPosition().getName());
        dto.setDismissalDate(entity.getDismissalDate());
        dto.setReason(entity.getReason());
        dto.setIsApproved(entity.getIsApproved());
        return dto;
    }
}
