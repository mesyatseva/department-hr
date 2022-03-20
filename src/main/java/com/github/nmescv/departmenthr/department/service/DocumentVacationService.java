package com.github.nmescv.departmenthr.department.service;

import com.github.nmescv.departmenthr.department.converter.DocumentVacationConverter;
import com.github.nmescv.departmenthr.department.dictionary.DocumentStatusDict;
import com.github.nmescv.departmenthr.department.dto.DocumentVacationDto;
import com.github.nmescv.departmenthr.department.entity.DocumentVacation;
import com.github.nmescv.departmenthr.department.repository.DocumentVacationRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class DocumentVacationService {

    private final DocumentVacationRepository documentVacationRepository;
    private final DocumentVacationConverter documentVacationConverter;

    public DocumentVacationService(DocumentVacationRepository documentVacationRepository, DocumentVacationConverter documentVacationConverter) {
        this.documentVacationRepository = documentVacationRepository;
        this.documentVacationConverter = documentVacationConverter;
    }

    /**
     * ROLE: Сотрудник
     *
     * Сотрудник создает заявку на отпуск
     * @return документ с заявлением на отпуск, статус "Открыт"
     */
    public DocumentVacationDto createRequestForVacation(DocumentVacationDto dto, Long employeeId) {
        dto.setOrderNumber(UUID.randomUUID().toString());
        dto.setDocumentStatus(DocumentStatusDict.OPEN.getStatus());
        dto.setEmployeeId(employeeId);
        dto.setCreatedAt(new Date());

        DocumentVacation entity = documentVacationConverter.toEntity(dto);
        DocumentVacation saved = documentVacationRepository.save(entity);
        return documentVacationConverter.toDto(saved);
    }

    /**
     * ROLE: Начальник
     *
     * Начальник подтверждает отпуск сотрудника
     * @return согласованный документ, статус "В процессе"
     */
    public DocumentVacationDto approveVacation(DocumentVacationDto dto, Long bossId) {
        dto.setIsApproved(Boolean.FALSE);
        dto.setDocumentStatus(DocumentStatusDict.IN_PROCESS.getStatus());
        DocumentVacation entity = documentVacationConverter.toEntity(dto);
        DocumentVacation saved = documentVacationRepository.save(entity);
        return documentVacationConverter.toDto(saved);
    }

    /**
     * ROLE: Начальник
     * @return отклоненный документ, статус "В процессе"
     */
    public DocumentVacationDto declineVacation(DocumentVacationDto dto, Long bossId) {
        dto.setIsApproved(Boolean.TRUE);
        dto.setDocumentStatus(DocumentStatusDict.IN_PROCESS.getStatus());
        DocumentVacation entity = documentVacationConverter.toEntity(dto);
        DocumentVacation saved = documentVacationRepository.save(entity);
        return documentVacationConverter.toDto(saved);
    }

    /**
     * ROLE: HR
     *
     * Завершает оформление документа
     * @return оформленный документ, статус "Закрыт"
     */
    public DocumentVacationDto endFormingVacationDocument(DocumentVacationDto dto, Long hrId) {
        dto.setDocumentStatus(DocumentStatusDict.CLOSED.getStatus());
        dto.setHr(hrId);
        DocumentVacation entity = documentVacationConverter.toEntity(dto);
        DocumentVacation saved = documentVacationRepository.save(entity);
        return documentVacationConverter.toDto(saved);
    }
}
