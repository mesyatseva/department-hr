package com.github.nmescv.departmenthr.department.service;

import com.github.nmescv.departmenthr.department.converter.DocumentReassignmentConverter;
import com.github.nmescv.departmenthr.department.dictionary.DocumentStatusDict;
import com.github.nmescv.departmenthr.department.dto.DocumentReassignmentDto;
import com.github.nmescv.departmenthr.department.entity.DocumentReassignment;
import com.github.nmescv.departmenthr.department.repository.DocumentReassignmentRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class DocumentReassignmentService {

    private final DocumentReassignmentConverter documentReassignmentConverter;
    private final DocumentReassignmentRepository documentReassignmentRepository;

    public DocumentReassignmentService(DocumentReassignmentConverter documentReassignmentConverter,
                                       DocumentReassignmentRepository documentReassignmentRepository) {
        this.documentReassignmentConverter = documentReassignmentConverter;
        this.documentReassignmentRepository = documentReassignmentRepository;
    }

    /**
     * ROLE: Сотрудник
     *
     * Сотрудник создает заявку на перевод
     * @return документ с заявлением на отпуск, статус "Открыт"
     */
    public DocumentReassignmentDto createRequestForReassignment(DocumentReassignmentDto dto, Long employeeId) {
        dto.setOrderNumber(UUID.randomUUID().toString());
        dto.setDocumentStatus(DocumentStatusDict.OPEN.getStatus());
        dto.setEmployeeId(employeeId);
        dto.setCreatedAt(new Date());

        DocumentReassignment entity = documentReassignmentConverter.toEntity(dto);
        DocumentReassignment saved = documentReassignmentRepository.save(entity);
        return documentReassignmentConverter.toDto(saved);
    }

    /**
     * ROLE: Начальник
     *
     * Начальник подтверждает перевод сотрудника
     * @return согласованный документ, статус "В процессе"
     */
    public DocumentReassignmentDto approveReassignment(DocumentReassignmentDto dto, Long bossId) {
        dto.setIsApproved(Boolean.FALSE);
        dto.setDocumentStatus(DocumentStatusDict.IN_PROCESS.getStatus());
        DocumentReassignment entity = documentReassignmentConverter.toEntity(dto);
        DocumentReassignment saved = documentReassignmentRepository.save(entity);
        return documentReassignmentConverter.toDto(saved);
    }

    /**
     * ROLE: Начальник
     * @return отклоненный документ, статус "В процессе"
     */
    public DocumentReassignmentDto declineReassignment(DocumentReassignmentDto dto, Long bossId) {
        dto.setIsApproved(Boolean.TRUE);
        dto.setDocumentStatus(DocumentStatusDict.IN_PROCESS.getStatus());
        DocumentReassignment entity = documentReassignmentConverter.toEntity(dto);
        DocumentReassignment saved = documentReassignmentRepository.save(entity);
        return documentReassignmentConverter.toDto(saved);

    }

    /**
     * ROLE: HR
     *
     * Завершает оформление документа
     * @return оформленный документ, статус "Закрыт"
     */
    public DocumentReassignmentDto endFormingVacationDocument(DocumentReassignmentDto dto, Long hrId) {
        dto.setDocumentStatus(DocumentStatusDict.CLOSED.getStatus());
        dto.setHr(hrId);
        DocumentReassignment entity = documentReassignmentConverter.toEntity(dto);
        DocumentReassignment saved = documentReassignmentRepository.save(entity);
        return documentReassignmentConverter.toDto(saved);
    }
}
