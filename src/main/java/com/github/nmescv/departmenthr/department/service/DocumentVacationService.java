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
import lombok.val;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.github.nmescv.departmenthr.department.dictionary.RoleDict.*;

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

        log.info("Vacation Documents - поиск всех документов: Поиск сотрудника");
        Employee employee = employeeRepository.findByTabelNumber(username);
        if (employee == null) {
            return null;
        }
        log.info("Vacation Documents - поиск всех документов: Сотрудник найден");
        log.info("Vacation Documents - поиск всех документов: Поиск аккаунта сотрудника");
        User user = userRepository.findByEmployee(employee);
        if (user == null) {
            return null;
        }
        log.info("Vacation Documents - поиск всех документов: Аккаунт сотрудника существует");
        log.info("Vacation Documents - поиск всех документов: Определяем роль сотрудника");
        for (Role role : user.getRoles()) {
            if (role.getName().equals(HR_ROLE)) {
                log.info("Vacation Documents - поиск всех документов: Роль - HR");
                return documentVacationRepository
                        .findAll()
                        .stream()
                        .map(documentVacationConverter::toDto)
                        .collect(Collectors.toList());
            }
        }

        for (Role role : user.getRoles()) {
            if (role.getName().equals(BOSS_ROLE)) {
                log.info("Vacation Documents - поиск всех документов: Роль - BOSS");
                val list = documentVacationRepository
                        .findAllByBoss(employee)
                        .stream()
                        .map(documentVacationConverter::toDto)
                        .collect(Collectors.toList());
                log.info("Vacation Documents - поиск всех документов: Роль - BOSS: {}", list.toString());
                return list;
            }
        }

        for (Role role : user.getRoles()) {
            if (role.getName().equals(RoleDict.EMPLOYEE_ROLE)) {
                log.info("Vacation Documents - поиск всех документов: Роль - EMPLOYEE");
                return documentVacationRepository
                        .findAllByEmployee(employee)
                        .stream()
                        .map(documentVacationConverter::toDto)
                        .collect(Collectors.toList());
            }
        }

        log.info("Vacation Documents - поиск всех документов: для данного сотрудника нет никаких документов");
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
     * Поиск документа по идентификатору документа
     * EMPLOYEE - видит только свои документы
     * BOSS - видит свои документы + документы своих подчиненных
     * HR - видит все документы
     *
     * @param documentId идентификатор документа
     * @param username табельный номер пользователя
     * @return информация о документе
     */
    public DocumentVacationDto showById(Long documentId, String username) {

        DocumentVacation documentVacation = documentVacationRepository.findById(documentId).orElse(null);
        if (documentVacation == null) {
            return null;
        }

        log.info("Vacation Documents - поиск документа: Поиск сотрудника");
        Employee employee = employeeRepository.findByTabelNumber(username);
        if (employee == null) {
            return null;
        }

        log.info("Vacation Documents - поиск документа: Сотрудник найден");
        log.info("Vacation Documents - поиск документа: Поиск аккаунта сотрудника");
        User user = userRepository.findByEmployee(employee);
        if (user == null) {
            return null;
        }
        log.info("Vacation Documents - поиск документа: Аккаунт сотрудника существует");
        log.info("Vacation Documents - поиск документа: Определяем роль сотрудника");

        for (Role role : user.getRoles()) {
            if (role.getName().equals(HR_ROLE)) {
                log.info("Vacation Documents - поиск документа: Роль - HR");
                log.info(documentVacation.getDocumentStatus().getName());
                return documentVacationConverter.toDto(documentVacation);
            }

            if (role.getName().equals(BOSS_ROLE)) {
                log.info("Vacation Documents - поиск документа: Роль - BOSS");
                if (documentVacation.getBoss().getTabelNumber().equals(username)) {
                    log.info(documentVacation.getDocumentStatus().getName());
                    return documentVacationConverter.toDto(documentVacation);
                }
            }

            if (role.getName().equals(EMPLOYEE_ROLE)) {
                log.info("Vacation Documents - поиск документа: Роль - EMPLOYEE");
                if (documentVacation.getEmployee().getTabelNumber().equals(username)) {
                    log.info(documentVacation.getDocumentStatus().getName());
                    return documentVacationConverter.toDto(documentVacation);
                }
            }
        }

        return null;
    }

    /**
     * ROLE: Начальник
     *
     * Начальник подтверждает отпуск сотрудника
     * @return согласованный документ, статус "В процессе"
     */
    public DocumentVacationDto approveVacation(Long id, String username) {
        DocumentVacationDto dto = showById(id, username);
        dto.setIsApproved(Boolean.TRUE);
        dto.setDocumentStatus(DocumentStatusDict.IN_PROCESS.getStatus());
        DocumentVacation entity = documentVacationConverter.toEntity(dto);
        DocumentVacation saved = documentVacationRepository.save(entity);
        return documentVacationConverter.toDto(saved);
    }

    /**
     * ROLE: Начальник
     * @return отклоненный документ, статус "В процессе"
     */
    public DocumentVacationDto declineVacation(Long id, String username) {
        DocumentVacationDto dto = showById(id, username);
        dto.setIsApproved(Boolean.FALSE);
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
    public DocumentVacationDto closeDocument(Long id, String username) {
        DocumentVacationDto dto = showById(id, username);
        dto.setHr(employeeRepository.findByTabelNumber(username).getId());
        dto.setDocumentStatus(DocumentStatusDict.CLOSED.getStatus());
        DocumentVacation entity = documentVacationConverter.toEntity(dto);
        DocumentVacation saved = documentVacationRepository.save(entity);
        return documentVacationConverter.toDto(saved);
    }
}
