package com.github.nmescv.departmenthr.department.service;

import com.github.nmescv.departmenthr.department.converter.DocumentDismissalConverter;
import com.github.nmescv.departmenthr.department.dictionary.DocumentStatusDict;
import com.github.nmescv.departmenthr.department.dictionary.RoleDict;
import com.github.nmescv.departmenthr.department.dto.DocumentDismissalDto;
import com.github.nmescv.departmenthr.department.entity.DocumentDismissal;
import com.github.nmescv.departmenthr.department.entity.Employee;
import com.github.nmescv.departmenthr.department.repository.DocumentDismissalRepository;
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
     * ROLE: Сотрудник
     *
     * Сотрудник, желающийся уволиться, создает документ на увольнение
     * @return документ с заявлением на увольнение
     */
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
