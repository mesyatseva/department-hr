package com.github.nmescv.departmenthr.department.service;

import com.github.nmescv.departmenthr.department.converter.DocumentHiringConverter;
import com.github.nmescv.departmenthr.department.dictionary.DocumentStatusDict;
import com.github.nmescv.departmenthr.department.dictionary.RoleDict;
import com.github.nmescv.departmenthr.department.dto.DocumentHiringDto;
import com.github.nmescv.departmenthr.department.entity.DocumentHiring;
import com.github.nmescv.departmenthr.department.entity.Employee;
import com.github.nmescv.departmenthr.department.repository.DocumentHiringRepository;
import com.github.nmescv.departmenthr.department.repository.EmployeeRepository;
import com.github.nmescv.departmenthr.security.entity.Role;
import com.github.nmescv.departmenthr.security.entity.User;
import com.github.nmescv.departmenthr.security.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@Service
public class DocumentHiringService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final DocumentHiringRepository documentHiringRepository;
    private final DocumentHiringConverter documentHiringConverter;

    public DocumentHiringService(EmployeeRepository employeeRepository, UserRepository userRepository,
                                 DocumentHiringRepository documentHiringRepository,
                                 DocumentHiringConverter documentHiringConverter) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.documentHiringRepository = documentHiringRepository;
        this.documentHiringConverter = documentHiringConverter;
    }

    /**
     * HR - все документы
     * BOSS - начальник может увидеть документы только своих подчиненных
     *
     * @param username табельный номер сотрудника
     * @return список документов
     */
    public List<DocumentHiringDto> findAll(String username) {

        Employee employee = employeeRepository.findByTabelNumber(username);
        if (employee == null) {
            return null;
        }

        User user = userRepository.findByEmployee(employee);
        if (user == null) {
            return null;
        }

        for (Role role : user.getRoles()) {
            if (role.getName().equals(RoleDict.HR_ROLE)) {
                return documentHiringRepository
                        .findAll()
                        .stream()
                        .map(documentHiringConverter::toDto)
                        .collect(Collectors.toList());
            }
        }

        for (Role role : user.getRoles()) {
            if (role.getName().equals(RoleDict.BOSS_ROLE)) {
                return documentHiringRepository
                        .findAllByBoss(employee)
                        .stream()
                        .map(documentHiringConverter::toDto)
                        .collect(Collectors.toList());
            }
        }

        return null;
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
