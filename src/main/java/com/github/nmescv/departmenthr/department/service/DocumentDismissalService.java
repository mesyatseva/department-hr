package com.github.nmescv.departmenthr.department.service;

import com.github.nmescv.departmenthr.department.converter.DocumentDismissalConverter;
import com.github.nmescv.departmenthr.department.dictionary.DocumentStatusDict;
import com.github.nmescv.departmenthr.department.dto.DocumentDismissalDto;
import com.github.nmescv.departmenthr.department.entity.DocumentDismissal;
import com.github.nmescv.departmenthr.department.repository.DocumentDismissalRepository;
import com.github.nmescv.departmenthr.department.repository.DocumentStatusRepository;
import com.github.nmescv.departmenthr.department.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class DocumentDismissalService {

    private final DocumentDismissalConverter documentDismissalConverter;
    private final DocumentDismissalRepository documentDismissalRepository;

    public DocumentDismissalService(DocumentDismissalConverter documentDismissalConverter,
                                    DocumentDismissalRepository documentDismissalRepository) {
        this.documentDismissalConverter = documentDismissalConverter;
        this.documentDismissalRepository = documentDismissalRepository;
    }

    /**
     * ROLE: Сотрудник
     *
     * Сотрудник, желающийся уволиться, создает документ на увольнение
     * @return документ с заявлением на увольнение
     */
    public DocumentDismissalDto createRequestToDismiss(DocumentDismissalDto dto, Long employeeId) {

        dto.setOrderNumber(UUID.randomUUID().toString());
        dto.setDocumentStatus(DocumentStatusDict.OPEN.getStatus());
        dto.setEmployeeId(employeeId);
        dto.setCreatedAt(new Date());

        DocumentDismissal entity = documentDismissalConverter.toEntity(dto);
        DocumentDismissal saved = documentDismissalRepository.save(entity);
        return documentDismissalConverter.toDto(saved);
    }

    /**
     * ROLE: Начальник
     *
     * Начальник подтверждает увольнение сотрудника
     * @return документ с "подписью" от начальника с согласием на увольнение
     */
    public DocumentDismissalDto approveDismiss(DocumentDismissalDto dto) {
        dto.setIsApproved(Boolean.FALSE);
        dto.setDocumentStatus(DocumentStatusDict.IN_PROCESS.getStatus());
        DocumentDismissal entity = documentDismissalConverter.toEntity(dto);
        DocumentDismissal saved = documentDismissalRepository.save(entity);
        return documentDismissalConverter.toDto(saved);

    }

    /**
     * ROLE: Начальник
     * @return документ с отклонением на увольнение
     */
    public DocumentDismissalDto declineDismiss(DocumentDismissalDto dto) {
        dto.setIsApproved(Boolean.TRUE);
        dto.setDocumentStatus(DocumentStatusDict.IN_PROCESS.getStatus());
        DocumentDismissal entity = documentDismissalConverter.toEntity(dto);
        DocumentDismissal saved = documentDismissalRepository.save(entity);
        return documentDismissalConverter.toDto(saved);
    }

    /**
     * ROLE: HR
     *
     * Завершает оформление документа
     * @return оформленый документ
     */
    public DocumentDismissalDto endFormingDocument(DocumentDismissalDto dto, Long hrId) {
        dto.setDocumentStatus(DocumentStatusDict.CLOSED.getStatus());
        dto.setHr(hrId);
        DocumentDismissal entity = documentDismissalConverter.toEntity(dto);
        DocumentDismissal saved = documentDismissalRepository.save(entity);
        return documentDismissalConverter.toDto(saved);
    }

}
