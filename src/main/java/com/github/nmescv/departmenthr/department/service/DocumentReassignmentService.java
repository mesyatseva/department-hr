package com.github.nmescv.departmenthr.department.service;

import com.github.nmescv.departmenthr.department.converter.DocumentReassignmentConverter;
import com.github.nmescv.departmenthr.department.dictionary.DocumentStatusDict;
import com.github.nmescv.departmenthr.department.dictionary.RoleDict;
import com.github.nmescv.departmenthr.department.dto.DocumentReassignmentDto;
import com.github.nmescv.departmenthr.department.entity.DocumentReassignment;
import com.github.nmescv.departmenthr.department.entity.Employee;
import com.github.nmescv.departmenthr.department.repository.DepartmentRepository;
import com.github.nmescv.departmenthr.department.repository.DocumentReassignmentRepository;
import com.github.nmescv.departmenthr.department.repository.EmployeeRepository;
import com.github.nmescv.departmenthr.department.repository.PositionRepository;
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
public class DocumentReassignmentService {

    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final DocumentReassignmentConverter documentReassignmentConverter;
    private final DocumentReassignmentRepository documentReassignmentRepository;

    public DocumentReassignmentService(PositionRepository positionRepository,
                                       DepartmentRepository departmentRepository,
                                       EmployeeRepository employeeRepository,
                                       UserRepository userRepository,
                                       DocumentReassignmentConverter documentReassignmentConverter,
                                       DocumentReassignmentRepository documentReassignmentRepository) {
        this.positionRepository = positionRepository;
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.documentReassignmentConverter = documentReassignmentConverter;
        this.documentReassignmentRepository = documentReassignmentRepository;
    }

    /**
     * EMPLOYEE - свои документы
     * BOSS - начальник может увидеть документы только своих подчиненных
     * HR - все документы
     *
     * @param username табельный номер сотрудника
     * @return список документов
     */
    public List<DocumentReassignmentDto> findAll(String username) {

        log.info("Reassignment Documents: Поиск сотрудника");
        Employee employee = employeeRepository.findByTabelNumber(username);
        if (employee == null) {
            return null;
        }
        log.info("Reassignment Documents: Сотрудник найден");
        log.info("Reassignment Documents: Поиск аккаунта сотрудника");
        User user = userRepository.findByEmployee(employee);
        if (user == null) {
            return null;
        }
        log.info("Reassignment Documents: Аккаунт сотрудника существует");
        log.info("Reassignment Documents: Определяем роль сотрудника");
        for (Role role : user.getRoles()) {
            if (role.getName().equals(HR_ROLE)) {
                log.info("Reassignment Documents: Роль - HR");
                return documentReassignmentRepository
                        .findAll()
                        .stream()
                        .map(documentReassignmentConverter::toDto)
                        .collect(Collectors.toList());
            }
        }

        for (Role role : user.getRoles()) {
            if (role.getName().equals(RoleDict.BOSS_ROLE)) {
                log.info("Reassignment Documents: Роль - BOSS");
                return documentReassignmentRepository
                        .findAllByBoss(employee)
                        .stream()
                        .map(documentReassignmentConverter::toDto)
                        .collect(Collectors.toList());
            }
        }

        for (Role role : user.getRoles()) {
            if (role.getName().equals(RoleDict.EMPLOYEE_ROLE)) {
                log.info("Reassignment Documents: Роль - EMPLOYEE");
                return documentReassignmentRepository
                        .findAllByEmployee(employee)
                        .stream()
                        .map(documentReassignmentConverter::toDto)
                        .collect(Collectors.toList());
            }
        }

        log.info("Reassignment Documents: для данного сотрудника нет никаких документов");
        return null;
    }

    /**
     * ROLE: Сотрудник
     *
     * Сотрудник создает заявку на перевод
     * @return документ с заявлением на отпуск, статус "Открыт"
     */
    @Transactional
    public DocumentReassignmentDto createDraftRequest(DocumentReassignmentDto dto, String username) {

        String orderNumber = UUID.randomUUID().toString();
        if (orderNumber.length() > 30) {
            orderNumber = orderNumber.substring(0, 30);
        }

        dto.setOrderNumber(orderNumber);
        dto.setDocumentStatus(DocumentStatusDict.DRAFT.getStatus());
        dto.setEmployeeId(employeeRepository.findByTabelNumber(username).getId());
        dto.setCreatedAt(new Date());

        DocumentReassignment entity = documentReassignmentConverter.toEntity(dto);
        DocumentReassignment saved = documentReassignmentRepository.save(entity);
        return documentReassignmentConverter.toDto(saved);
    }

    @Transactional
    public DocumentReassignmentDto publishRequest(DocumentReassignmentDto dto, String position) {

        DocumentReassignment documentReassignment = documentReassignmentRepository.findById(dto.getId()).orElse(null);
        if (documentReassignment == null) {
            return null;
        }

        dto.setBossId(departmentRepository.findByName(dto.getNewDepartment()).getBoss().getId());
        dto.setDocumentStatus(DocumentStatusDict.OPEN.getStatus());
        dto.setNewPosition(position);
        DocumentReassignment entity = documentReassignmentConverter.toEntity(dto);
        DocumentReassignment saved = documentReassignmentRepository.save(entity);
        return documentReassignmentConverter.toDto(saved);
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
    public DocumentReassignmentDto showById(Long documentId, String username) {

        DocumentReassignment document = documentReassignmentRepository.findById(documentId).orElse(null);
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
                return documentReassignmentConverter.toDto(document);
            }

            if (role.getName().equals(BOSS_ROLE)) {
                log.info("Dismissal Documents - поиск документа: Роль - BOSS");
                if (document.getBoss().getTabelNumber().equals(username)) {
                    log.info(document.getDocumentStatus().getName());
                    return documentReassignmentConverter.toDto(document);
                }
            }

            if (role.getName().equals(EMPLOYEE_ROLE)) {
                log.info("Dismissal Documents - поиск документа: Роль - EMPLOYEE");
                if (document.getEmployee().getTabelNumber().equals(username)) {
                    log.info(document.getDocumentStatus().getName());
                    return documentReassignmentConverter.toDto(document);
                }
            }
        }

        return null;
    }

    /**
     * ROLE: Начальник
     *
     * Начальник подтверждает перевод сотрудника
     * @return согласованный документ, статус "В процессе"
     */
    @Transactional
    public DocumentReassignmentDto approveReassignment(Long id, String username) {
        DocumentReassignmentDto dto = showById(id, username);
        dto.setIsApproved(Boolean.TRUE);
        dto.setDocumentStatus(DocumentStatusDict.IN_PROCESS.getStatus());

        Employee employee = employeeRepository.findById(dto.getEmployeeId()).orElse(null);
        assert employee != null;
        employee.setDepartment(departmentRepository.findByName(dto.getDepartment()));
        employee.setPosition(positionRepository.findByName(dto.getPosition()));
        employeeRepository.save(employee);
        DocumentReassignment entity = documentReassignmentConverter.toEntity(dto);
        DocumentReassignment saved = documentReassignmentRepository.save(entity);
        return documentReassignmentConverter.toDto(saved);
    }

    /**
     * ROLE: Начальник
     * @return отклоненный документ, статус "В процессе"
     */
    @Transactional
    public DocumentReassignmentDto declineReassignment(Long id, String username) {
        DocumentReassignmentDto dto = showById(id, username);
        dto.setIsApproved(Boolean.FALSE);
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
    @Transactional
    public DocumentReassignmentDto closeDocument(Long id, String username) {
        DocumentReassignmentDto dto = showById(id, username);
        dto.setDocumentStatus(DocumentStatusDict.CLOSED.getStatus());
        dto.setHr(employeeRepository.findByTabelNumber(username).getId());
        DocumentReassignment entity = documentReassignmentConverter.toEntity(dto);
        DocumentReassignment saved = documentReassignmentRepository.save(entity);
        return documentReassignmentConverter.toDto(saved);
    }
}
