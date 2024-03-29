package com.github.nmescv.departmenthr.department.service;

import com.github.nmescv.departmenthr.department.converter.DocumentHiringConverter;
import com.github.nmescv.departmenthr.department.dictionary.DocumentStatusDict;
import com.github.nmescv.departmenthr.department.dictionary.RoleDict;
import com.github.nmescv.departmenthr.department.dto.DocumentHiringDto;
import com.github.nmescv.departmenthr.department.entity.Department;
import com.github.nmescv.departmenthr.department.entity.DocumentHiring;
import com.github.nmescv.departmenthr.department.entity.Employee;
import com.github.nmescv.departmenthr.department.repository.DepartmentRepository;
import com.github.nmescv.departmenthr.department.repository.DocumentHiringRepository;
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
public class DocumentHiringService {

    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final DocumentHiringRepository documentHiringRepository;
    private final DocumentHiringConverter documentHiringConverter;

    public DocumentHiringService(PositionRepository positionRepository,
                                 DepartmentRepository departmentRepository,
                                 EmployeeRepository employeeRepository,
                                 UserRepository userRepository,
                                 DocumentHiringRepository documentHiringRepository,
                                 DocumentHiringConverter documentHiringConverter) {
        this.positionRepository = positionRepository;
        this.departmentRepository = departmentRepository;
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

        log.info("Hiring Documents: Поиск сотрудника");
        Employee employee = employeeRepository.findByTabelNumber(username);
        if (employee == null) {
            return null;
        }
        log.info("Hiring Documents: Сотрудник найден");
        log.info("Hiring Documents: Поиск аккаунта сотрудника");
        User user = userRepository.findByEmployee(employee);
        if (user == null) {
            return null;
        }
        log.info("Hiring Documents: Аккаунт сотрудника существует");
        log.info("Hiring Documents: Определяем роль сотрудника");
        for (Role role : user.getRoles()) {
            if (role.getName().equals(RoleDict.HR_ROLE)) {
                log.info("Hiring Documents: Роль - HR");
                return documentHiringRepository
                        .findAll()
                        .stream()
                        .map(documentHiringConverter::toDto)
                        .collect(Collectors.toList());
            }
        }

        for (Role role : user.getRoles()) {
            if (role.getName().equals(RoleDict.BOSS_ROLE)) {
                log.info("Hiring Documents: Роль - BOSS");
                return documentHiringRepository
                        .findAllByBoss(employee)
                        .stream()
                        .map(documentHiringConverter::toDto)
                        .collect(Collectors.toList());
            }
        }

        log.info("Hiring Documents: для данного сотрудника нет никаких документов");
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
    public DocumentHiringDto showById(Long documentId, String username) {

        DocumentHiring document = documentHiringRepository.findById(documentId).orElse(null);
        if (document == null) {
            return null;
        }

        log.info("Hiring Documents - поиск документа: Поиск сотрудника");
        Employee employee = employeeRepository.findByTabelNumber(username);
        if (employee == null) {
            return null;
        }

        log.info("Hiring Documents - поиск документа: Сотрудник найден");
        log.info("Hiring Documents - поиск документа: Поиск аккаунта сотрудника");
        User user = userRepository.findByEmployee(employee);
        if (user == null) {
            return null;
        }
        log.info("Hiring Documents - поиск документа: Аккаунт сотрудника существует");
        log.info("Hiring Documents - поиск документа: Определяем роль сотрудника");

        for (Role role : user.getRoles()) {
            if (role.getName().equals(HR_ROLE)) {
                log.info("Hiring Documents - поиск документа: Роль - HR");
                log.info(document.getDocumentStatus().getName());
                return documentHiringConverter.toDto(document);
            }
        }

        for (Role role : user.getRoles()) {
            if (role.getName().equals(BOSS_ROLE)) {
                log.info("Hiring Documents - поиск документа: Роль - BOSS");
                if (document.getBoss().getTabelNumber().equals(username)) {
                    log.info(document.getDocumentStatus().getName());
                    return documentHiringConverter.toDto(document);
                }
            }
        }

        for (Role role : user.getRoles()) {
            if (role.getName().equals(EMPLOYEE_ROLE)) {
                log.info("Hiring Documents - поиск документа: Роль - EMPLOYEE");
                if (document.getEmployee().getTabelNumber().equals(username)) {
                    log.info(document.getDocumentStatus().getName());
                    return documentHiringConverter.toDto(document);
                }
            }
        }

        return null;
    }


    /**
     * ROLE: HR
     * <p>
     * Создает документ на прием на работу
     *
     * @param dto - данные документа
     * @return созданный документ в статусе "Открыт"
     */
    @Transactional
    public DocumentHiringDto createHiringDraftStep1(DocumentHiringDto dto, Long hrId) {
        String orderNumber = UUID.randomUUID().toString();
        if (orderNumber.length() > 30) {
            orderNumber = orderNumber.substring(0, 30);
        }
        dto.setOrderNumber(orderNumber);
        dto.setDocumentStatus(DocumentStatusDict.DRAFT.getStatus());
        dto.setHr(hrId);
        dto.setCreatedAt(new Date());

        DocumentHiring entity = documentHiringConverter.toEntity(dto);
        DocumentHiring saved = documentHiringRepository.save(entity);
        return documentHiringConverter.toDto(saved);
    }

    /**
     * ROLE: HR
     * Заполняем подразделение (статус - Черновик)
     *
     * @param dto данные для обновления
     * @return обновленный документ
     */
    @Transactional
    public DocumentHiringDto fillDepartmentDraftStep2(DocumentHiringDto dto, String department) {
        DocumentHiring documentHiring = documentHiringRepository.findById(dto.getId()).orElse(null);
        if (documentHiring == null) {
            return null;
        }
        Employee employee = documentHiring.getEmployee();
        DocumentHiring entity = documentHiringConverter.toEntity(dto);

        log.info(department);
        Department departmentEntity = departmentRepository.findByName(department);
        log.info(departmentEntity.toString());

        employee.setDepartment(departmentEntity);
        entity.setDepartment(departmentEntity);
        entity.setBoss(departmentEntity
                .getBoss());
        employeeRepository.save(employee);
        DocumentHiring saved = documentHiringRepository.save(entity);
        return documentHiringConverter.toDto(saved);
    }

    /**
     * ROLE: HR
     * Заполняем должность (статус - Черновик) и публикуем документ
     *
     * @param dto данные для обновления
     * @return обновленный документ
     */
    @Transactional
    public DocumentHiringDto publishAfterFillingPosition(DocumentHiringDto dto, String position) {
        DocumentHiring documentHiring = documentHiringRepository.findById(dto.getId()).orElse(null);
        if (documentHiring == null) {
            return null;
        }
        Employee employee = documentHiring.getEmployee();
        dto.setPosition(position);
        dto.setDocumentStatus(DocumentStatusDict.OPEN.getStatus());
        DocumentHiring entity = documentHiringConverter.toEntity(dto);
        employee.setPosition(positionRepository.findByName(position));
        employeeRepository.save(employee);
        DocumentHiring saved = documentHiringRepository.save(entity);
        return documentHiringConverter.toDto(saved);
    }

    /**
     * ROLE: Начальник
     * <p>
     * Подтверждает документ на прием на работу
     *
     * @param id - данные документа
     * @return созданный документ в статусе "В процессе"
     */
    @Transactional
    public DocumentHiringDto approveHiring(Long id, String username) {
        DocumentHiringDto dto = showById(id, username);
        dto.setIsApproved(Boolean.TRUE);
        dto.setDocumentStatus(DocumentStatusDict.IN_PROCESS.getStatus());
        DocumentHiring entity = documentHiringConverter.toEntity(dto);
        DocumentHiring saved = documentHiringRepository.save(entity);
        return documentHiringConverter.toDto(saved);
    }

    /**
     * ROLE: Начальник
     * <p>
     * Отклоняет документ на прием на работу
     *
     * @param id - данные документа
     * @return созданный документ в статусе "В процессе"
     */
    @Transactional
    public DocumentHiringDto declineHiring(Long id, String username) {
        DocumentHiringDto dto = showById(id, username);
        dto.setIsApproved(Boolean.FALSE);
        dto.setDocumentStatus(DocumentStatusDict.IN_PROCESS.getStatus());
        DocumentHiring entity = documentHiringConverter.toEntity(dto);
        DocumentHiring saved = documentHiringRepository.save(entity);
        return documentHiringConverter.toDto(saved);
    }

    /**
     * ROLE: HR
     * <p>
     * Закрывает документ на прием на работу
     *
     * @param id - данные документа
     * @return созданный документ в статусе "Закрыт"
     */
    @Transactional
    public DocumentHiringDto closeDocument(Long id, String username) {
        DocumentHiringDto dto = showById(id, username);
        dto.setDocumentStatus(DocumentStatusDict.CLOSED.getStatus());
        dto.setHr(employeeRepository.findByTabelNumber(username).getId());
        DocumentHiring entity = documentHiringConverter.toEntity(dto);
        DocumentHiring saved = documentHiringRepository.save(entity);
        return documentHiringConverter.toDto(saved);
    }
}
