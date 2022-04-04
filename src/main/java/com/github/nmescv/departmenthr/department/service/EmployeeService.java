package com.github.nmescv.departmenthr.department.service;

import com.github.nmescv.departmenthr.department.converter.EmployeeConverter;
import com.github.nmescv.departmenthr.department.dto.EmployeeDto;
import com.github.nmescv.departmenthr.department.entity.Employee;
import com.github.nmescv.departmenthr.department.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeConverter employeeConverter;
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeConverter employeeConverter,
                           EmployeeRepository employeeRepository) {
        this.employeeConverter = employeeConverter;
        this.employeeRepository = employeeRepository;
    }

    public List<EmployeeDto> findAll() {
        return employeeRepository
                .findAll()
                .stream()
                .map(employeeConverter::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Отобаражет информацию сотрудника через табельный номер
     * @param tabelNumber табельный номер сотрудника
     * @return карточка сотрудника
     */
    public EmployeeDto showProfileByTableNumber(String tabelNumber) {

        Employee employee = employeeRepository.findByTabelNumber(tabelNumber);
        if (employee == null) {
            return null;
        }
        return employeeConverter.toDto(employee);
    }

    /**
     * Отобаражет информацию сотрудника через табельный номер
     * @param id идентификатор сотрудника
     * @return карточка сотрудника
     */
    public EmployeeDto showById(Long id) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee == null) {
            return null;
        }
        return employeeConverter.toDto(employee);
    }

    /**
     * Создает карточку с информацией о сотруднике
     * @param employeeDto данные для сотрудника
     * @return сохраненная информация о сотрудника
     */
    @Transactional
    public EmployeeDto createNewEmployee(EmployeeDto employeeDto) {
        employeeDto.setTabelNumber(UUID.randomUUID().toString().replace("-", "").substring(0, 10));
        Employee employee = employeeConverter.toEntity(employeeDto);
        Employee savedEmployee = employeeRepository.save(employee);
        return employeeConverter.toDto(savedEmployee);
    }
}
