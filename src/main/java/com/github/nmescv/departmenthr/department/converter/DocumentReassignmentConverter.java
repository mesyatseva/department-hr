package com.github.nmescv.departmenthr.department.converter;

import com.github.nmescv.departmenthr.department.dto.DocumentReassignmentDto;
import com.github.nmescv.departmenthr.department.entity.DocumentReassignment;
import com.github.nmescv.departmenthr.department.entity.Employee;
import com.github.nmescv.departmenthr.department.repository.DepartmentRepository;
import com.github.nmescv.departmenthr.department.repository.DocumentStatusRepository;
import com.github.nmescv.departmenthr.department.repository.EmployeeRepository;
import com.github.nmescv.departmenthr.department.repository.PositionRepository;
import lombok.Synchronized;
import org.springframework.stereotype.Component;

@Component
public class DocumentReassignmentConverter implements Converter<DocumentReassignment, DocumentReassignmentDto> {

    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final DocumentStatusRepository documentStatusRepository;
    private final EmployeeRepository employeeRepository;

    public DocumentReassignmentConverter(DepartmentRepository departmentRepository,
                                         PositionRepository positionRepository,
                                         DocumentStatusRepository documentStatusRepository,
                                         EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
        this.documentStatusRepository = documentStatusRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Synchronized
    public DocumentReassignment toEntity(DocumentReassignmentDto dto) {

        if (dto == null) {
            return null;
        }

        DocumentReassignment entity = new DocumentReassignment();
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
        entity.setReassignmentDate(dto.getReassignmentDate());
        entity.setNewPosition(positionRepository.findByName(dto.getNewPosition()));
        entity.setNewDepartment(departmentRepository.findByName(dto.getNewDepartment()));
        entity.setSalary(dto.getSalary());
        entity.setIsApproved(dto.getIsApproved());
        return entity;
    }

    @Override
    @Synchronized
    public DocumentReassignmentDto toDto(DocumentReassignment entity) {

        if (entity == null) {
            return null;
        }

        DocumentReassignmentDto dto = new DocumentReassignmentDto();
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
        dto.setReassignmentDate(entity.getReassignmentDate());
        dto.setNewPosition(entity.getNewPosition().getName());
        dto.setNewDepartment(entity.getNewDepartment().getName());
        dto.setSalary(entity.getSalary());
        dto.setIsApproved(entity.getIsApproved());
        return dto;
    }
}
