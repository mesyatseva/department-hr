package com.github.nmescv.departmenthr.department.service;

import com.github.nmescv.departmenthr.department.converter.DocumentHiringConverter;
import com.github.nmescv.departmenthr.department.dictionary.DocumentStatusDict;
import com.github.nmescv.departmenthr.department.dto.DocumentHiringDto;
import com.github.nmescv.departmenthr.department.entity.DocumentHiring;
import com.github.nmescv.departmenthr.department.entity.DocumentReassignment;
import com.github.nmescv.departmenthr.department.repository.DocumentHiringRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class DocumentHiringService {

    private final DocumentHiringRepository documentHiringRepository;
    private final DocumentHiringConverter documentHiringConverter;

    public DocumentHiringService(DocumentHiringRepository documentHiringRepository, DocumentHiringConverter documentHiringConverter) {
        this.documentHiringRepository = documentHiringRepository;
        this.documentHiringConverter = documentHiringConverter;
    }


    /**
     * ROLE: HR
     *
     * Создает документ на прием на работу
     * @param dto - данные документа
     * @return созданный документ в статусе "Открыт"
     */
    public DocumentHiringDto createHiringDocument(DocumentHiringDto dto, Long hrId) {
        dto.setOrderNumber(UUID.randomUUID().toString());
        dto.setDocumentStatus(DocumentStatusDict.OPEN.getStatus());
        dto.setHr(hrId);
        dto.setCreatedAt(new Date());

        DocumentHiring entity = documentHiringConverter.toEntity(dto);
        DocumentHiring saved = documentHiringRepository.save(entity);
        return documentHiringConverter.toDto(saved);
    }

    /**
     * ROLE: Начальник
     *
     * Подтверждает документ на прием на работу
     * @param dto - данные документа
     * @return созданный документ в статусе "В процессе"
     */
    public DocumentHiringDto approveHiringDocument(DocumentHiringDto dto, Long bossId) {
        dto.setIsApproved(Boolean.FALSE);
        dto.setDocumentStatus(DocumentStatusDict.IN_PROCESS.getStatus());
        DocumentHiring entity = documentHiringConverter.toEntity(dto);
        DocumentHiring saved = documentHiringRepository.save(entity);
        return documentHiringConverter.toDto(saved);
    }

    /**
     * ROLE: Начальник
     *
     * Отклоняет документ на прием на работу
     * @param dto - данные документа
     * @return созданный документ в статусе "В процессе"
     */
    public DocumentHiringDto declineHiringDocument(DocumentHiringDto dto, Long bossId) {
        dto.setIsApproved(Boolean.TRUE);
        dto.setDocumentStatus(DocumentStatusDict.IN_PROCESS.getStatus());
        DocumentHiring entity = documentHiringConverter.toEntity(dto);
        DocumentHiring saved = documentHiringRepository.save(entity);
        return documentHiringConverter.toDto(saved);
    }

    /**
     * ROLE: HR
     *
     * Закрывает документ на прием на работу
     * @param dto - данные документа
     * @return созданный документ в статусе "Закрыт"
     */
    public DocumentHiringDto finishHiringDocument(DocumentHiringDto dto) {
        dto.setDocumentStatus(DocumentStatusDict.CLOSED.getStatus());
        DocumentHiring entity = documentHiringConverter.toEntity(dto);
        DocumentHiring saved = documentHiringRepository.save(entity);
        return documentHiringConverter.toDto(saved);
    }
}
