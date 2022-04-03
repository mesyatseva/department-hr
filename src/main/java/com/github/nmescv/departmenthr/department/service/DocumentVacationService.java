package com.github.nmescv.departmenthr.department.service;

import com.github.nmescv.departmenthr.department.converter.DocumentVacationConverter;
import com.github.nmescv.departmenthr.department.dictionary.DocumentStatusDict;
import com.github.nmescv.departmenthr.department.dictionary.RoleDict;
import com.github.nmescv.departmenthr.department.dto.DocumentReassignmentDto;
import com.github.nmescv.departmenthr.department.dto.DocumentVacationDto;
import com.github.nmescv.departmenthr.department.entity.DocumentVacation;
import com.github.nmescv.departmenthr.department.entity.Employee;
import com.github.nmescv.departmenthr.department.repository.DocumentVacationRepository;
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

import static com.github.nmescv.departmenthr.department.dictionary.RoleDict.HR_ROLE;

@Slf4j
@Service
public class DocumentVacationService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final DocumentVacationRepository documentVacationRepository;
    private final DocumentVacationConverter documentVacationConverter;

    public DocumentVacationService(EmployeeRepository employeeRepository,
                                   UserRepository userRepository,
                                   DocumentVacationRepository documentVacationRepository,
                                   DocumentVacationConverter documentVacationConverter) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.documentVacationRepository = documentVacationRepository;
        this.documentVacationConverter = documentVacationConverter;
    }

    /**
     * EMPLOYEE - свои документы
     * BOSS - начальник может увидеть документы только своих подчиненных
     * HR - все документы
     *
     * @param username табельный номер сотрудника
     * @return список документов
     */
    public List<DocumentVacationDto> findAll(String username) {

        log.info("Vacation Documents: Поиск сотрудника");
        Employee employee = employeeRepository.findByTabelNumber(username);
        if (employee == null) {
            return null;
        }
        log.info("Vacation Documents: Сотрудник найден");
        log.info("Vacation Documents: Поиск аккаунта сотрудника");
        User user = userRepository.findByEmployee(employee);
        if (user == null) {
            return null;
        }
        log.info("Vacation Documents: Аккаунт сотрудника существует");
        log.info("Vacation Documents: Определяем роль сотрудника");
        for (Role role : user.getRoles()) {
            if (role.getName().equals(HR_ROLE)) {
                log.info("Vacation Documents: Роль - HR");
                return documentVacationRepository
                        .findAll()
                        .stream()
                        .map(documentVacationConverter::toDto)
                        .collect(Collectors.toList());
            }
        }

        for (Role role : user.getRoles()) {
            if (role.getName().equals(RoleDict.BOSS_ROLE)) {
                log.info("Vacation Documents: Роль - BOSS");
                return documentVacationRepository
                        .findAllByBoss(employee)
                        .stream()
                        .map(documentVacationConverter::toDto)
                        .collect(Collectors.toList());
            }
        }

        for (Role role : user.getRoles()) {
            if (role.getName().equals(RoleDict.EMPLOYEE_ROLE)) {
                log.info("Vacation Documents: Роль - EMPLOYEE");
                return documentVacationRepository
                        .findAllByEmployee(employee)
                        .stream()
                        .map(documentVacationConverter::toDto)
                        .collect(Collectors.toList());
            }
        }

        log.info("Vacation Documents: для данного сотрудника нет никаких документов");
        return null;
    }

    /**
     * ROLE: Сотрудник
     *
     * Сотрудник создает заявку на отпуск
     * @return документ с заявлением на отпуск, статус "Открыт"
     */
    public DocumentVacationDto createRequestForVacation(DocumentVacationDto dto, Long employeeId) {

        String orderNumber = UUID.randomUUID().toString();
        if (orderNumber.length() > 30) {
            orderNumber = orderNumber.substring(0, 30);
        }
        dto.setOrderNumber(orderNumber);
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
