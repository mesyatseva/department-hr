package com.github.nmescv.departmenthr.department.service;

import com.github.nmescv.departmenthr.department.converter.DocumentDismissalConverter;
import com.github.nmescv.departmenthr.department.dictionary.DocumentStatusDict;
import com.github.nmescv.departmenthr.department.dictionary.RoleDict;
import com.github.nmescv.departmenthr.department.dto.DocumentDismissalDto;
import com.github.nmescv.departmenthr.department.dto.DocumentVacationDto;
import com.github.nmescv.departmenthr.department.entity.DocumentDismissal;
import com.github.nmescv.departmenthr.department.entity.DocumentVacation;
import com.github.nmescv.departmenthr.department.entity.Employee;
import com.github.nmescv.departmenthr.department.repository.DocumentDismissalRepository;
import com.github.nmescv.departmenthr.department.repository.EmployeeRepository;
import com.github.nmescv.departmenthr.security.entity.Role;
import com.github.nmescv.departmenthr.security.entity.User;
import com.github.nmescv.departmenthr.security.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.github.nmescv.departmenthr.department.dictionary.RoleDict.*;

@Slf4j
@Service
public class DocumentDismissalService {

    private final DocumentDismissalConverter documentDismissalConverter;
    private final DocumentDismissalRepository documentDismissalRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    public DocumentDismissalService(DocumentDismissalConverter documentDismissalConverter,
                                    DocumentDismissalRepository documentDismissalRepository,
                                    EmployeeRepository employeeRepository,
                                    UserRepository userRepository) {
        this.documentDismissalConverter = documentDismissalConverter;
        this.documentDismissalRepository = documentDismissalRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
    }

    /**
     * EMPLOYEE - свои документы
     * BOSS - начальник может увидеть документы только своих подчиненных
     * HR - все документы
     *
     * @param username табельный номер сотрудника
     * @return список документов
     */
    public List<DocumentDismissalDto> findAll(String username) {

        log.info("Dismissal Documents: Поиск сотрудника");
        Employee employee = employeeRepository.findByTabelNumber(username);
        if (employee == null) {
            return null;
        }
        log.info("Dismissal Documents: Сотрудник найден");
        log.info("Dismissal Documents: Поиск аккаунта сотрудника");
        User user = userRepository.findByEmployee(employee);
        if (user == null) {
            return null;
        }
        log.info("Dismissal Documents: Аккаунт сотрудника существует");
        log.info("Dismissal Documents: Определяем роль сотрудника");
        for (Role role : user.getRoles()) {
            if (role.getName().equals(HR_ROLE)) {
                log.info("Dismissal Documents: Роль - HR");
                return documentDismissalRepository
                        .findAll()
                        .stream()
                        .map(documentDismissalConverter::toDto)
                        .collect(Collectors.toList());
            }
        }

        for (Role role : user.getRoles()) {
            if (role.getName().equals(RoleDict.BOSS_ROLE)) {
                log.info("Dismissal Documents: Роль - BOSS");
                return documentDismissalRepository
                        .findAllByBoss(employee)
                        .stream()
                        .map(documentDismissalConverter::toDto)
                        .collect(Collectors.toList());
            }
        }

        for (Role role : user.getRoles()) {
            if (role.getName().equals(RoleDict.EMPLOYEE_ROLE)) {
                log.info("Dismissal Documents: Роль - EMPLOYEE");
                return documentDismissalRepository
                        .findAllByEmployee(employee)
                        .stream()
                        .map(documentDismissalConverter::toDto)
                        .collect(Collectors.toList());
            }
        }

        log.info("Dismissal Documents: для данного сотрудника нет никаких документов");
        return null;
    }

    /**
     * Поиск документа по идентификатору документа
     * EMPLOYEE - видит только свои документы
     * BOSS - видит свои документы + документы своих подчиненных
     * HR - видит все документы
     *
     * @param documentId идентификатор документа
     * @param username   табельный номер пользователя
     * @return информация о документе
     */
    public DocumentDismissalDto showById(Long documentId, String username) {

        DocumentDismissal document = documentDismissalRepository.findById(documentId).orElse(null);
        if (document == null) {
            return null;
        }

        log.info("Dismissal Documents - поиск документа: Поиск сотрудника");
        Employee employee = employeeRepository.findByTabelNumber(username);
        if (employee == null) {
            return null;
        }

        log.info("Dismissal Documents - поиск документа: Сотрудник найден");
        log.info("Dismissal Documents - поиск документа: Поиск аккаунта сотрудника");
        User user = userRepository.findByEmployee(employee);
        if (user == null) {
            return null;
        }
        log.info("Dismissal Documents - поиск документа: Аккаунт сотрудника существует");
        log.info("Dismissal Documents - поиск документа: Определяем роль сотрудника");

        for (Role role : user.getRoles()) {
            if (role.getName().equals(HR_ROLE)) {
                log.info("Dismissal Documents - поиск документа: Роль - HR");
                log.info(document.getDocumentStatus().getName());
                return documentDismissalConverter.toDto(document);
            }

            if (role.getName().equals(BOSS_ROLE)) {
                log.info("Dismissal Documents - поиск документа: Роль - BOSS");
                if (document.getBoss().getTabelNumber().equals(username)) {
                    log.info(document.getDocumentStatus().getName());
                    return documentDismissalConverter.toDto(document);
                }
            }

            if (role.getName().equals(EMPLOYEE_ROLE)) {
                log.info("Dismissal Documents - поиск документа: Роль - EMPLOYEE");
                if (document.getEmployee().getTabelNumber().equals(username)) {
                    log.info(document.getDocumentStatus().getName());
                    return documentDismissalConverter.toDto(document);
                }
            }
        }

        return null;
    }

    /**
     * ROLE: Сотрудник
     * <p>
     * Сотрудник, желающийся уволиться, создает документ на увольнение
     *
     * @return документ с заявлением на увольнение
     */
    @Transactional
    public DocumentDismissalDto createRequestToDismiss(DocumentDismissalDto dto, Long employeeId) {
        String orderNumber = UUID.randomUUID().toString();
        if (orderNumber.length() > 30) {
            orderNumber = orderNumber.substring(0, 30);
        }

        dto.setOrderNumber(orderNumber);
        dto.setDocumentStatus(DocumentStatusDict.OPEN.getStatus());
        dto.setEmployeeId(employeeId);
        dto.setCreatedAt(new Date());

        DocumentDismissal entity = documentDismissalConverter.toEntity(dto);
        DocumentDismissal saved = documentDismissalRepository.save(entity);
        return documentDismissalConverter.toDto(saved);
    }

    /**
     * ROLE: Начальник
     * <p>
     * Начальник подтверждает увольнение сотрудника
     *
     * @return документ с "подписью" от начальника с согласием на увольнение
     */
    @Transactional
    public DocumentDismissalDto approveDismiss(Long id, String username) {
        DocumentDismissalDto dto = showById(id, username);
        dto.setIsApproved(Boolean.TRUE);
        dto.setDocumentStatus(DocumentStatusDict.IN_PROCESS.getStatus());
        DocumentDismissal entity = documentDismissalConverter.toEntity(dto);
        DocumentDismissal saved = documentDismissalRepository.save(entity);
        return documentDismissalConverter.toDto(saved);

    }

    /**
     * ROLE: Начальник
     *
     * @return документ с отклонением на увольнение
     */
    @Transactional
    public DocumentDismissalDto declineDismiss(Long id, String username) {
        DocumentDismissalDto dto = showById(id, username);
        dto.setIsApproved(Boolean.FALSE);
        dto.setDocumentStatus(DocumentStatusDict.IN_PROCESS.getStatus());
        DocumentDismissal entity = documentDismissalConverter.toEntity(dto);
        DocumentDismissal saved = documentDismissalRepository.save(entity);
        return documentDismissalConverter.toDto(saved);
    }

    /**
     * ROLE: HR
     * <p>
     * Завершает оформление документа
     *
     * @return оформленый документ
     */
    @Transactional
    public DocumentDismissalDto closeDocument(Long id, String username) {
        DocumentDismissalDto dto = showById(id, username);
        dto.setDocumentStatus(DocumentStatusDict.CLOSED.getStatus());
        dto.setHr(employeeRepository.findByTabelNumber(username).getId());
        DocumentDismissal entity = documentDismissalConverter.toEntity(dto);
        DocumentDismissal saved = documentDismissalRepository.save(entity);
        return documentDismissalConverter.toDto(saved);
    }

}
