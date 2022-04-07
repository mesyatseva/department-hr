package com.github.nmescv.departmenthr.department.converter;

import com.github.nmescv.departmenthr.department.dto.DocumentDismissalDto;
import com.github.nmescv.departmenthr.department.dto.DocumentVacationDto;
import com.github.nmescv.departmenthr.department.entity.DocumentReassignment;
import com.github.nmescv.departmenthr.department.entity.DocumentVacation;
import com.github.nmescv.departmenthr.department.entity.Employee;
import com.github.nmescv.departmenthr.department.repository.DocumentStatusRepository;
import com.github.nmescv.departmenthr.department.repository.EmployeeRepository;
import com.github.nmescv.departmenthr.department.utils.EmployeeUtils;
import lombok.Synchronized;
import org.springframework.stereotype.Component;

@Component
public class DocumentVacationConverter implements Converter<DocumentVacation, DocumentVacationDto> {

    private final EmployeeRepository employeeRepository;
    private final DocumentStatusRepository documentStatusRepository;

    public DocumentVacationConverter(EmployeeRepository employeeRepository,
                                     DocumentStatusRepository documentStatusRepository) {
        this.employeeRepository = employeeRepository;
        this.documentStatusRepository = documentStatusRepository;
    }

    @Override
    @Synchronized
    public DocumentVacation toEntity(DocumentVacationDto dto) {

        if (dto == null) {
            return null;
        }

        DocumentVacation entity = new DocumentVacation();
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

        entity.setStartAt(dto.getStartAt());
        entity.setEndAt(dto.getEndAt());
        entity.setVacationType(dto.getVacationType());

        entity.setIsApproved(dto.getIsApproved());
        return entity;
    }

    @Override
    @Synchronized
    public DocumentVacationDto toDto(DocumentVacation entity) {

        if (entity == null) {
            return null;
        }

        DocumentVacationDto dto = new DocumentVacationDto();
        dto.setId(entity.getId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setOrderNumber(entity.getOrderNumber());
        dto.setEmployeeId(entity.getEmployee().getId());
        dto.setEmployeeFullName(EmployeeUtils.fullName(entity.getEmployee()));

        dto.setBossId(entity.getBoss().getId());
        dto.setBossFullName(EmployeeUtils.fullName(entity.getBoss()));

        if (entity.getHr() != null) {
            dto.setHr(entity.getHr().getId());
            dto.setHrFullName(EmployeeUtils.fullName(entity.getHr()));
        }

        dto.setDocumentStatus(entity.getDocumentStatus().getName());
        dto.setDepartment(entity.getDepartment().getName());
        dto.setPosition(entity.getPosition().getName());
        dto.setStartAt(entity.getStartAt());
        dto.setEndAt(entity.getEndAt());
        dto.setVacationType(entity.getVacationType());
        dto.setIsApproved(entity.getIsApproved());
        return dto;
    }
}
